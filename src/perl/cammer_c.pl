#!/usr/bin/env perl
# -*- mode: Perl -*-
#
# Created by Tobias Oetiker <oetiker@ee.ethz.ch>
#
# Updated by James Harris between Nov 2006 and Jan 2008.
#   - Added some devices to work in same way as Catalyst 2900
#   - Included voice vlans
#   - Now outputs CSV for easy import to spreadsheet
#   - Added columns
#     - OUI and NIC maker
#     - Port short names
#     - Port descriptions
#     - DNS hostnames and domain names
#     - Operational status - Up or Down
#     - Caller-supplied text (for grouping)
#   - Identify trunk (multi-vlan) ports
#   - Handle switches which report their own macs as visible
#
# This script is available at
#
#     http://codewiki.wikispaces.com/
#
# For information on how to install and use the script see
#
#     http://codewiki.wikispaces.com/cammer_c.pl
#
# A note is in order on the caller-supplied text column. Now that
# the script produces results in CSV format the output from
# multiple runs can be concatenated. The switch name allows a first
# level of grouping but sometimes the user may wish to group
# switches. The user-supplied text may be used for this. For
# example, we scan five sites concurrently and the user-supplied
# text indicates the site.
 
require 5.005;
use strict;
my $DEBUG = 0;
BEGIN {
    # Automatic OS detection ... do NOT touch
    if ( $^O =~ /^(?:(ms)?(dos|win(32|nt)?))/i ) {
        $main::OS = 'NT';
        $main::SL = '\\';
        $main::PS = ';';
    } elsif ( $^O =~ /^VMS$/i ) {
        $main::OS = 'VMS';
        $main::SL = '.';
        $main::PS = ':';
    } else {
        $main::OS = 'UNIX';
        $main::SL = '/';
        $main::PS = ':';
    }
}
 
use FindBin;
use lib "${FindBin::Bin}";
use lib "${FindBin::Bin}${main::SL}..${main::SL}lib${main::SL}mrtg2";
 
use SNMP_Session "0.78";
use BER "0.77";
use SNMP_util; # "0.77";
use Getopt::Long;
use Pod::Usage;
use Socket;
 
 
my %OID = (
 'vmVlan'                      => [1,3,6,1,4,1,9,9,68,1,2,2,1,2],
 'vmVoiceVlanId'               => [1,3,6,1,4,1,9,9,68,1,5,1,1,1],
 'vlanIndex'                   => [1,3,6,1,4,1,9,5,1,9,2,1,1],
 'ifPhysAddress'               => [1,3,6,1,2,1,2,2,1,6],
 'ifName'                      => [1,3,6,1,2,1,31,1,1,1,1],
 'ipNetToMediaPhysAddress'     => [1,3,6,1,2,1,4,22,1,2],
 'dot1dTpFdbPort'              => [1,3,6,1,2,1,17,4,3,1,2],
 'dot1dBasePortIfIndex'        => [1,3,6,1,2,1,17,1,4,1,2],
 'ifOperStatus'                => [1,3,6,1,2,1,2,2,1,8],
 'ifAlias'                     => [1,3,6,1,2,1,31,1,1,1,18],
 'vlanTrunkPortDynamicStatus'  => [1,3,6,1,4,1,9,9,46,1,6,1,1,14],
#unused          'sysObjectID' =>              [1,3,6,1,2,1,1,2,0],
#unused          'CiscolocIfDescr' =>          [1,3,6,1,4,1,9,2,2,1,1,28],
);
 
#Extract from the (base 16) lines from the IEEE download
my $ouibasefilename = "ouishort.txt";
 
#Some of the following descriptions are based on MIBs found by Cisco's
#SNMP Object Navigator.
 
 
#vmVlan .1.3.6.1.4.1.9.9.68.1.2.2.1.2
#
#For each port this identifies the Vlan to which the port has been assigned.
#
#The VLAN id of the VLAN the port is assigned to
#when vmVlanType is set to static or dynamic.
#This object is not instantiated if not applicable.
#
#The value may be 0 if the port is not assigned
#to a VLAN.
#
#If vmVlanType is static, the port is always
#assigned to a VLAN and the object may not be
#set to 0.
#
#If vmVlanType is dynamic the object's value is
#0 if the port is currently not assigned to a VLAN.
#In addition, the object may be set to 0 only.
 
 
 
 
#vmVoiceVlanId .1.3.6.1.4.1.9.9.68.1.5.1.1.1
#
#For each port with an auxilliary voice vlan this reports the vlan
#
#The Voice Vlan ID (VVID) to which this port belongs
 
 
 
#vlanIndex .1.3.6.1.4.1.9.5.1.9.2.1.1
#
#A list of the vlans known to the device whether there are ports assigned to
#them or not.
#
#An index value that uniquely identifies the
#Virtual LAN associated with this information.
 
 
 
 
#ifPhysAddress .1.3.6.1.2.1.2.2.1.6
#
#The mac address of the interface - this needs to be excluded as it is
#reported by some switches.
#
#The interface's address at its protocol sub-layer. For
#example, for an 802.x interface, this object normally
#contains a mac address. The interface's media-specific MIB
#must define the bit and byte ordering and the format of the
#value of this object. For interfaces which do not have such
#an address (e.g., a serial line), this object should contain
#an octet string of zero length.
 
 
 
 
#ifName .1.3.6.1.2.1.31.1.1.1.1
#
#A short form of the interface name such as "Fa0/1", "4/27", "VLAN-50". Note
#that this maps the interface 'number' used in MIB entries to a recognised
#name. Note, too, that this is under the public subtree and is not
#Cisco specific.
#
#The textual name of the interface. The value of this
#object should be the name of the interface as assigned by
#the local device and should be suitable for use in commands
#entered at the device's `console'. This might be a text
#name, such as `le0' or a simple port number, such as `1',
#depending on the interface naming syntax of the device. If
#several entries in the ifTable together represent a single
#interface as named by the device, then each will have the
#same value of ifName. Note that for an agent which responds
#to SNMP queries concerning an interface on some other
#(proxied) device, then the value of ifName for such an
#interface is the proxied device's local name for it.
#
#If there is no local name, or this object is otherwise not
#applicable, then this object contains a zero-length string.
 
 
 
 
 
#ipNetToMediaPhysAddress .1.3.6.1.2.1.4.22.1.2
#
#The router's ARP table.
#
#The media-dependent `physical' address. This object should
#return 0 when this entry is in the 'incomplete' state.
#
#As the entries in this table are typically not persistent
#when this object is written the entity should not save the
#change to non-volatile storage. Note: a stronger
#requirement is not used because this object was previously
#defined.
 
 
 
 
 
#dot1dTpFdbPort .1.3.6.1.2.1.17.4.3.1.2
#
#All known mac addresses and their corresponding output ports.
#N.B. if the community string value is given as comstring@vlan the
#returned data will apply to the specified vlan only.
#
#Either the value '0', or the port number of the
#port on which a frame having a source address
#equal to the value of the corresponding instance
#of dot1dTpFdbAddress has been seen. A value of
#'0' indicates that the port number has not been
#learned but that the bridge does have some
#forwarding/filtering information about this
#address (e.g. in the dot1dStaticTable).
#Implementors are encouraged to assign the port
#value to this object whenever it is learned even
#for addresses for which the corresponding value of
#dot1dTpFdbStatus is not learned(3).
 
 
 
#dot1dBasePortIfIndex .1.3.6.1.2.1.17.1.4.1.2
#
#For each port in the bridging tables this identifies the
#device "interface" which is associated thereto.
#
#The value of the instance of the ifIndex object,
#defined in MIB-II, for the interface corresponding
#to this port.
 
 
 
#vlanTrunkPortDynamicStatus .1.3.6.1.4.1.9.9.46.1.6.1.1.14
#
#Value is 1 if this port is known to VTP as a trunk.
#
#Indicates whether the specified interface is either
#acting as a trunk or not. This is a result of the
#vlanTrunkPortDynamicState and the ifOperStatus of the
#trunk port itself.
 
 
 
#Note, using snmp version 1 rather than the potentially faster snmpv2s
#as we may need to scan some devices which don't support getbulk
 
sub main {
    my %opt;
    options(\%opt);
    # which vlans do exist on the device
    my @vlans;
    my $vlani;
    my %vlan;
    my @local_mac; #mac addresses of self indexed by interface
 
 
    #Read in the OUI table - maps OUIs (leftmost three octets of mac) to
    #the company issuing the mac address
    #print "Env vars:$0";
    my $location = rindex($0, $main::SL);
    my $ouifilename = substr($0, 0, $location + 1) . $ouibasefilename;
    my %ouis;
    open (OUIFILE, $ouifilename) or die "Cannot open the oui file: $!\n";
    while (<OUIFILE>) {
      my ($oui, $owner) = split(' ',$_,2); #Split the line to two fields by spaces
      #OUI needs consistency for later matching - arbitrary preference for lower case
      $oui=lc($oui);
      chomp($owner); #Strip trailing newline
      $owner =~ s/"//g; #Remove any double quote marks
      if (length $oui != 6) {
        die "invalid line ($_) OUI length (length $oui) in $ouifilename\n";
      };
      $ouis{$oui} = $owner;
#  print "OUI line added: \$ouis\{$oui\}=$owner.\n";
    }
 
 
 
    my $sws = SNMPv1_Session->open ($opt{sw},$opt{swco},161)
#    my $sws = SNMPv2c_Session->open ($opt{sw},$opt{swco},161)
                || die "Opening SNMP_Session\n";
 
#    warn "* Gather VLAN index Table from Switch @($OID{'vlanIndex'})\n";
    warn "\n";
    warn "* Gather VLAN index Table from Switch $opt{sw}\n";
    my $sysdesc = (snmpget($opt{swco}.'@'.$opt{sw},'sysDescr'))[0];
    warn "$sysdesc\n\n";
 
 
    if ($sysdesc =~ /C2900|C2940|C3500|C3550|C3750|cat4000/){
        warn "* Going into Cisco vmVlan mode (special OID: $OID{'vmVlan'})\n";
        $sws->map_table_4 ( [$OID{'vmVlan'}],
           sub {    my($x,$value) = pretty(@_);
                    $vlan{$x} = $value; # catalyst 2900
                    warn "if: $x, vlan: $value" if $DEBUG;
                    if (!grep {$_ eq $value} @vlans) { #if no matches
                       push @vlans, $value;
                       warn "    vlan: $value" if $DEBUG;
                    }
               }
        ,100);
 
 
        warn "* Adding any voice vlans\n";
        $sws->map_table_4 ( [$OID{'vmVoiceVlanId'}],
           sub {    my($x,$value) = pretty(@_);
                    warn "if: $x, voice vlan: $value" if $DEBUG;
                    if ($value <= 4094) {
                       #Check Cisco's OID description for 1.3.6.1.4.1.9.9.68.1.5.1.1.1
                       $vlan{$x} = $value;
                       if (!grep {$_ eq $value} @vlans) { #if no matches
                         push @vlans, $value;
                         warn "vlan: $value added" if $DEBUG;
                       }
                    }
                }
          ,100);
 
# print "Vlans:\n";
# print "@vlans\n";
# print "--\n";
 
    } #We now have a list of vlans active on ports (in @vlans) and a note of which
      #vlan is on which port (in %vlan{<interface>})
    else { #Not a 2900 etc.
      warn "* Going into vlanIndex mode\n";
        $sws->map_table_4 ([$OID{'vlanIndex'}],
           sub {
               my($x,$value) = pretty(@_);
               push @vlans, $value;
               warn "vlan: $value" if $DEBUG;
           }
        ,100 );
    } #We now have a list of all vlans present on the switch - this includes
      #those active on ports and those with no port on the vlan
    warn "Vlans seen: @vlans" if $DEBUG;
 
    #Note that either of the above options will have populated @vlans with
    #simply a list of vlan numbers. The record of which ports are in which vlan,
    #the %vlan value, does not seem to be used further in this code.
 
 
    # which ifNames are on the switch?
    my %name;
    warn "* Gather Interface Name Table from Switch $OID{'ifName'}\n";
    $sws->map_table_4 ([$OID{'ifName'}],
        sub { my($if,$name) = pretty(@_);
              warn "if: $if, name: $name" if $DEBUG;
              $name{$if}=$name;
        }
    ,100);
    #Hash %name{interface number} gives the name of each numbered interface
 
    # What descriptions are on the interfaces - Cisco use ifAlias for the desc
    my @intf_alias;
    warn "* Fetching any interface descriptions";
    $sws->map_table_4 ([$OID{'ifAlias'}], sub {
      my ($x, $value) = pretty (@_);
      warn qq(Interface "$x" has description "$value"\n) if $DEBUG;
      $intf_alias[$x] = $value;
    }, 100);
 
 
    #Get the operational status of each port - i.e. Up or Down
    my @intf_oper_status;
    warn "* Fetching operational status of each interface\n";
    $sws->map_table_4 ([$OID{'ifOperStatus'}], sub {
      my ($x, $value) = pretty (@_);
      warn qq(Re. operational status I have "$x"=>"$name{$x}" and "$value")
        if $DEBUG;
      $intf_oper_status[$x] = $value == 2 ? 0 : 1; #0=>down, 1=>up
    }, 100);
 
 
 
    #Get the physical addresses of the switch ports (so that they can be
    # excluded from the report)
    warn "* Fetching interface physical addresses\n";
    $sws->map_table_4 ([$OID{'ifPhysAddress'}],
      sub {
        my ($x, $value) = pretty (@_);
# print "Length of value is " . length($value) . "\n";
# print unpack "H*", $value;
        $value = unpack "H*", $value;
# print "Value is $value\n";
        if ($value) {
# print "Substr 0, 2 is " . (substr $value, 0, 2);
# print "Substr 2, 2 is " . (substr $value, 2, 2);
# print "Substr 4, 2 is " . (substr $value, 4, 2);
# print "Substr 6, 2 is " . (substr $value, 6, 2);
# print "Substr 8, 2 is " . (substr $value, 8, 2);
# print "Substr 10, 2 is " . (substr $value, 10, 2);
 
          $value = join "-",
           (substr $value, 0, 2),
           (substr $value, 2, 2),
           (substr $value, 4, 2),
           (substr $value, 6, 2),
           (substr $value, 8, 2),
           (substr $value, 10, 2);
# print "Value is now $value\n";
# print "Found local mac $value (on interface $x, aka $name{$x})\n";
 
#          warn print qq( Mac on interface "$x" is "$value") if $DEBUG;
 
          $local_mac[$x] = $value; #The mac address for this interface is $value.
          warn "Added local_mac[$x] = $value" if $DEBUG;
        } #If there is a value - i.e. a mac address
          # (there won't be on a Null0 interface)
      }, 100);
 
 
    warn qq(* Check VTP MIB to find which interfaces are trunks);
    my @trunk_interfaces;
    my @intf_istrunk; #Booleans
    $sws->map_table_4 ([$OID{'vlanTrunkPortDynamicStatus'}],
     sub { my ($x, $value) = pretty (@_);
           if ($value == 1) { #If a trunk
             push @trunk_interfaces, $x;
             $intf_istrunk[$x] = 1; #Flag this as a trunk
             warn "  New trunk interface: $x" if $DEBUG > 2;
           }
     }, 100);
 
 
    $sws->close;
 
 
 
 
    # get mac to ip from router
    my %ip;             #To hold the IP to mac mappings
 
#    my $ros = SNMPv1_Session->open ($opt{ro},$opt{roco},161)
    my $ros = SNMPv2c_Session->open ($opt{ro},$opt{roco},161)                || die "Opening SNMP_Session\n";
 
    warn "* Gather Arp Table from Router $OID{'ipNetToMediaPhysAddress'}\n";
    $ros->map_table_4 ([$OID{'ipNetToMediaPhysAddress'}],
        sub {
             my($ip,$mac) = pretty(@_);
             $mac = unpack 'H*', pack 'a*',$mac;
             $mac =~ s/../$&-/g;
             $mac =~ s/.$//;
             $ip =~ s/^.+?\.//;
             push @{$ip{$mac}}, $ip;
             warn "ip: $ip, mac: $mac" if $DEBUG > 5;
         }
    ,100);
    $ros->close;
 
 
 
 
# --- Start of kludge ---
 
#NB Patched/kludged here to add arp entries from specific routers:
# Was used while one site was in a state of transition from one router to
# many for any given switch
 
#  Assumes same community string as specified on command line
 
    my @router_names = ($opt{ro}, ); #Add routers as needed
    my @router_names = (); #if extra routers are not wanted)
 
    foreach my $router_name (@router_names) {
 
      my $ros = SNMPv2c_Session->open ($router_name,$opt{roco},161)                  || die "Opening SNMP_Session\n";
      warn "* Gather Arp table from router $router_name";
      $ros->map_table_4 ([$OID{'ipNetToMediaPhysAddress'}],
          sub {
             my($ip,$mac) = pretty(@_);
               $mac = unpack 'H*', pack 'a*',$mac;
               $mac =~ s/../$&-/g;
               $mac =~ s/.$//;
               $ip =~ s/^.+?\.//;
               push @{$ip{$mac}}, $ip;
               warn "ip: $ip, mac: $mac" if $DEBUG > 5;
           }
      ,100);
      $ros->close;
 
    } #end foreach router name
 
# --- end of kludge ---
 
 
 
 
    warn "* Walk CAM table for each VLAN";
    my %if;
    my %port;
 
    my %maccount; #Count of macs per vlan and port
 
    warn "* Gather Mac to Port and Port to Intf table for all VLANS community\@vlan $OID{'dot1dTpFdbPort'}, $OID{'dot1dBasePortIfIndex'}\n";
    foreach my $vlan (@vlans){
        warn "  Gathering mac and port data for vlan $vlan" if $DEBUG;
 
        #Catalyst 2900 and similar do not use com@vlan hack
        #Open FDB for this vlan only
        my $sws = SNMPv1_Session->open ($opt{sw},$opt{swco}.'@'.$vlan,161);
#        my $sws = SNMPv2c_Session->open ($opt{sw},$opt{swco}.'@'.$vlan,161)                || die "Opening SNMP_Session\n";
 
        #warn qq(  Reading all the mac addresses known in this vlan to find the "port" each one is on) if $DEBUG;
        $sws->map_table_4 ([$OID{'dot1dTpFdbPort'}],
          sub {
             my($mac,$port) = pretty(@_);
             $mac = sprintf "%02x-%02x-%02x-%02x-%02x-%02x", (split /\./, $mac);
 
             warn qq(Mac "$mac" is on port "$port" for this vlan, "$vlan") if $DEBUG;
 
             ### next if $port == 0; #0 => not learned, maybe statically configured, ignore
 
             $maccount{$vlan}{$port} += 1;
 
             warn qq(Mac "$mac" is number $maccount{$vlan}{$port} on port "$port" in vlan "$vlan")
               if $DEBUG;
 
             if ($port != 0) { #0 => not learned, maybe statically configured, ignore
               $port{$vlan}{$mac}=$port;
               warn " Have set port($vlan,$mac) = $port" if $DEBUG > 1;
             }
          }
        ,100);
 
        warn qq(For each "port" in the CAM table find the "interface" number used in the rest of the MIB)
          if $DEBUG;
        $sws->map_table_4 ( [$OID{'dot1dBasePortIfIndex'}],
          sub {
              my($port,$if) = pretty(@_);
              if ($port != 0) {
                $if{$vlan}{$port} = $if;
                warn "  Have set interface($vlan,$port) = $if" if $DEBUG;
              }
          }
        ,100);
 
        $sws->close;
    }
 
 
 
 
    warn "* Process each vlan, mac, ip and hostname";
    my %output;
    warn "  Looking at these vlans: @vlans" if $DEBUG;
    vlan: foreach my $vlan (@vlans){
      warn "   Processing each mac in vlan $vlan" if $DEBUG;
      mac: while (my ($mac,$p) = each %{$port{$vlan}}){
        warn "    Processing mac $mac (in vlan $vlan) on port $p" if $DEBUG;
#      print "$mac, $p -- " if $DEBUG;
#      while ((my $k, my $v) = each (%{$port{$vlan}})) {
#        warn "$k=$v\n";
#      }
        my $qmacs = $maccount{$vlan}{$p};
        warn "      No. of macs ($mac) on vlan $vlan port $p is $qmacs"
          if $DEBUG;
#        if ($qmacs > 1) {
#          next;
#        }
        my $bridge_port_num = $p;
        my $intf_num = $if{$vlan}{$bridge_port_num};
        my $intf_name = $name{$intf_num};
        my $local_mac = $local_mac[$intf_num]; #The mac address of this interface, if any.
        warn qq(  Mac "$mac" is on port "$bridge_port_num" aka interface "$intf_num" called "$intf_name"\n)
          if $DEBUG;
        if (grep {$_ eq $if{$vlan}{$p}} @trunk_interfaces) { #If a trunk port
          warn "              is trunk, ignoring" if $DEBUG;
          next mac;
        }
        warn "     Comparing $mac with mac of port $intf_num, $local_mac[$intf_num]" if $DEBUG;
        if ($mac eq $local_mac) {
          warn "       a local mac so ignoring" if $DEBUG;;
####          if (grep {$_ eq $mac} @local_mac) { #If one of our own macs
          next mac;
        } else {
          warn "       not the same so continuing" if $DEBUG;
        }
#        if ($qmacs > 1) {
#          my $name = $name{$if{$vlan}{$p}};
#          my $truevlan = $vlan eq 'none' ? $vlan{$if{$vlan}{$p}} : $vlan;
#          push @{$output{$name}}, sprintf "%s,%s", $truevlan, $qmacs;
#          next mac;
#        }
#        else {
        my @ip = $ip{$mac} ? @{$ip{$mac}} : ();
        my @host;
        foreach my $ip (@ip) {
          warn " - ip $ip\n" if $DEBUG > 3;
          my $host = gethostbyaddr(pack('C4',split(/\./,$ip)),AF_INET);
          warn "- for ip $ip got host $host" if $DEBUG > 2;
          my @host_split = split("\\.", $host, 2);  #Separate out the hostname and domain parts
#          $host =~ s/\.ethz\.ch//;
          warn qq(- host splits into "$host_split[0]" and "$host_split[1]") if $DEBUG > 2;
          if ($host) {
            push @host, $host_split[0] . "," . $host_split[1];  #Host name and domain name
          } else {
            push @host, ($ip . ",");  #IP in place of the host; a blank domain name
          }
#          push @host, ($host or $ip);
        }
        if (!@host) {
          push @host, ",";  #Blank hostname and domain name
        }
        my $oper_status = ($intf_oper_status[$intf_num] ? "up" : "down");
        warn "- operational status of $intf_num is $oper_status" if $DEBUG > 2;
        my $oui = lc(join "", ((split /-/, $mac)[0..2])); #Lower case hex
        warn "- oui is $oui" if $DEBUG > 2;
        my $ouiname = $ouis{$oui} ? $ouis{$oui} : "";
        warn "- oui name is $ouiname" if $DEBUG > 2;
        my $name = $name{$if{$vlan}{$p}};
        warn "- name is $name" if $DEBUG > 2;
        my $truevlan = $vlan eq 'none' ? $vlan{$if{$vlan}{$p}} : $vlan;
        warn "- vlan is $truevlan" if $DEBUG;
        if ($intf_istrunk[$intf_num]) {
          my $truevlan = "trunk";
        }
        warn "- true vlan is $truevlan" if $DEBUG;
        my $quest = scalar @ip > 1 ? "(multi)":"";
        warn qq(- multi string is "$quest") if $DEBUG;
        my $intf_num_lz = sprintf("%08d", $intf_num); #Add leading zeros for sort
#             push @{$output{$intf_num_lz . "c"}},
#        sprintf qq("mac",%s,%s,"oui %6s",%s,%s,"%s","%s"),$truevlan,$mac,$oui,$ip[0],\
#$quest,$host[0],$ouiname;
        push @{$output{$intf_num_lz . "m"}},
        sprintf qq($truevlan,$oper_status,"$intf_alias[$intf_num]","$mac",$quest,$ip[0],$host[0],\
"oui $oui","$ouiname");
        warn "--- written for $intf_num_lz" if $DEBUG > 2;
##             push @{$output{$name}},
#                 sprintf "%s,%s,\"oui %6s\",%s,%s,%s,\"%s\"",
#                   $truevlan,$mac,$oui,$ip[0],$quest,$host[0],$ouiname;
#           } #if $qmacs
        warn "- end of mac" if $DEBUG > 2;
      } #foreach my $mac
      warn "- end of vlan" if $DEBUG > 2;
    } #foreach my $vlan
 
    warn "- highest interface number is $#intf_oper_status" if $DEBUG > 4;
 
 
#    foreach my $i (0 .. $#intf_oper_status) {           #Each interface
    foreach my $i (sort keys %name) {
      my $oper_status = ($intf_oper_status[$i] ? "up" : "down");
      warn " -> interface $i status $intf_oper_status[$i]" if $DEBUG > 4;
      my $intf_num_lz = sprintf("%08d", $i); #Iinterface number with leading zeros
      my $truevlan = $vlan{$i};
      warn qq(testing whether interface $i is a trunk: "$intf_istrunk[$i]") if $DEBUG;
      if ($intf_istrunk[$i]) {
        warn "     Yes" if $DEBUG;
        $truevlan = "trunk";
      } else {
        warn "     NO!" if $DEBUG;
      }
      push @{$output{$intf_num_lz . "p"}}, qq($truevlan,$oper_status,"$intf_alias[$i]");
#      if ($intf_oper_status[$i]) {
#        push @{$output{$intf_num_lz . "a"}}, qq("conn","up","$intf_alias[$i]");
##        push @{$output{$intf_num_lz . "a"}}, "up";
#      } else {
#        push @{$output{$intf_num_lz . "a"}}, qq("conn","down","$intf_alias[$i]");
##        push @{$output{$intf_num_lz . "a"}}, "down";
#      } #if operational status
#      if ($intf_istrunk[$i]) {
#        push @{$output{$intf_num_lz . "b"}}, qq("extra","trunk");
##        push @{$output{$intf_num_lz . "b"}}, "trunk";
#      }
    } #for each port
 
 
 
 
    my @macs_printed;           #Count of macs printed for each interface
    foreach my $ikey (sort keys %output) {
      warn " --- have key  $ikey" if $DEBUG;
      my $inum = $ikey;
      my $record_type = chop $inum; #Drop the last character suffix
      my $short_inum = 0 + $inum;
      if ($record_type eq "m") {
        foreach my $line (@{$output{$ikey}}) {
          warn qq(     --- have line "$line") if $DEBUG;
          if ($name{$short_inum} ne "") { #Filter out blank port names
            printf "%s,%s,\"interface %s\",%s\n", $opt{site}, $opt{sw}, $name{$short_inum}, $line;
            $macs_printed[$inum]++;
            warn "macs printed for line key $ikey is $macs_printed[$inum]" if $DEBUG;
          } #if $name...
        } #Foreach $line as one of the m records
      } #if record_type is m
      if ($record_type eq "p") {
        foreach my $line (@{$output{$ikey}}) {
          warn "Have $ikey p line $line" if $DEBUG;
#        my $line = $output{$ikey}[0];  #We assume only one p-record
          warn qq(now on p record, key $ikey. total macs printed for m records was "$macs_printed[$inum]")
            if $DEBUG;
          if ($macs_printed[$inum] == 0) {
            printf "%s,%s,\"interface %s\",%s\n", $opt{site}, $opt{sw}, $name{$short_inum}, $line;
          } #if macs_printed was 0
        } #foreach line
      } #if record_type is p
    } #Foreach $ikey
 
}
 
main;
exit 0;
 
 
sub options () {
   my $opt = shift;
   GetOptions( $opt,
        'help|?',
        'man') or pod2usage(2);
   pod2usage(-verbose => 1) if $$opt{help} or scalar @ARGV < 2 or scalar @ARGV > 3;
   $opt->{ro} = shift @ARGV;
   $opt->{sw} = shift @ARGV;
 
   $opt->{site} = shift @ARGV;
# print "Site is .$opt->{site}.\n";
 
   pod2usage(-exitstatus => 0, -verbose => 2) if $$opt{man};
 
   $opt->{sw} =~ /^(.+)@(.+?)$/;
   $opt->{sw} = $2;
   $opt->{swco} = $1;
   $opt->{ro} =~ /^(.+)@(.+?)$/;
   $opt->{ro} = $2;
   $opt->{roco} = $1;
}
 
sub pretty(@){
  my $index = shift;
  my @ret = ($index);
  foreach my $x (@_){
        push @ret, pretty_print($x);
  };
  return @ret;
}
 
__END__
 
=head1 NAME
 
cammer - list switch ports with associated IP-addresses
 
=head1 SYNOPSIS
 
cammer [--help|--man] community@router community@switch
 
 
=head1 DESCRIPTION
 
B<Cammer> is a script which polls a switch and a router in order to produce
a list of machines attached (and currently online) at each port of the
switch.
 
=head1 COPYRIGHT
 
Copyright (c) 2000 ETH Zurich, All rights reserved.
 
=head1 LICENSE
 
This script is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.
 
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
 
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
=head1 AUTHOR
 
Tobias Oetiker E<lt>oetiker@ee.ethz.chE<gt>
 
=cut
#!/usr/local/bin/perl

use Net::SNMP;

#

#use Time::HiRes qw(time);

#my %benchvalue;

$switchcount=120;

$requestcount=10;

%benchvalue;

for ($y=0;$y<$switchcount;$y++)
{
$timestart=Time::HiRes::gettimeofday ();
#print "START:".$timestart."\n";

for($i=0;$i<$requestcount;$i++)
{

# requires a hostname and a community string as its arguments
($session,$error) = Net::SNMP->session(Hostname => $ARGV[0],
                                       Community => $ARGV[1]);

die "session error: $error" unless ($session);

# iso.org.dod.internet.mgmt.mib-2.interfaces.ifNumber.0 = 
#   1.3.6.1.2.1.2.1.0
$result = $session->get_request("1.3.6.1.2.1.2.1.0");

die "request error: ".$session->error unless (defined $result);

$session->close;

}

#print "Number of interfaces: ".$result->{"1.3.6.1.2.1.2.1.0"}."\n";

$timestop=Time::HiRes::gettimeofday ();
#print "STOP:".$timestart."\n";
#print "DIF: ".($timestop-$timestart)."\n";

$benchvalue{$y}=($timestop-$timestart);

}

$sumbench=0;
$countbench=0;

foreach (keys %benchvalue){

$countbench++;

$sumbench+=$benchvalue{$_};

}

print "[".$sumbench."]/[".($requestcount*$countbench)."]\nAVG: [".($sumbench/$countbench)."] Requests/s: ".(($requestcount*$countbench)/($sumbench))."\n";
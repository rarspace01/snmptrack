var config = new Object();


//===================  GLOBAL TOOPTIP CONFIGURATION  =========================//
var  tt_Debug	= false		// false or true - recommended: false once you release your page to the public
var  tt_Enabled	= true		// Allows to (temporarily) suppress tooltips, e.g. by providing the user with a button that sets this global variable to false
var  TagsToTip	= true		// false or true - if true, the script is capable of converting HTML elements to tooltips

// For each of the following config variables there exists a command, which is
// just the variablename in uppercase, to be passed to Tip() or TagToTip() to
// configure tooltips individually. Individual commands override global
// configuration. Order of commands is arbitrary.
// Example: onmouseover="Tip('Tooltip text', LEFT, true, BGCOLOR, '#FF9900', FADEIN, 400)"

config. Above			= false 	// false or true - tooltip above mousepointer?
config. BgColor 		= '#F9F9F9' // Background color
config. BgImg			= ''		// Path to background image, none if empty string ''
config. BorderColor 	= '#002299'
config. BorderStyle 	= 'solid'	// Any permitted CSS value, but I recommend 'solid', 'dotted' or 'dashed'
config. BorderWidth 	= 0
config. CenterMouse 	= false 	// false or true - center the tip horizontally below (or above) the mousepointer
config. ClickClose		= true	 	// false or true - close tooltip if the user clicks somewhere
config. CloseBtn		= false 	// false or true - closebutton in titlebar
config. CloseBtnColors	= ['#990000', '#FFFFFF', '#DD3333', '#FFFFFF']	  // [Background, text, hovered background, hovered text] - use empty strings '' to inherit title colors
config. CloseBtnText	= '&nbsp;X&nbsp;'	// Close button text (may also be an image tag)
config. CopyContent		= true		// When converting a HTML element to a tooltip, copy only the element's content, rather than converting the element by its own
config. Delay			= 0		// Time span in ms until tooltip shows up
config. Duration		= 0 		// Time span in ms after which the tooltip disappears; 0 for infinite duration
config. FadeIn			= 0 		// Fade-in duration in ms, e.g. 400; 0 for no animation
config. FadeOut 		= 0
config. FadeInterval	= 30		// Duration of each fade step in ms (recommended: 30) - shorter is smoother but causes more CPU-load
config. Fix 			= null		// Fixated position - x- an y-oordinates in brackets, e.g. [210, 480], or null for no fixation
config. FollowMouse		= true		// false or true - tooltip follows the mouse
config. FontColor		= '#000'
config. FontFace		= 'Verdana,Geneva,sans-serif'
config. FontSize		= '9pt' 	// E.g. '9pt' or '12px' - unit is mandatory
config. FontWeight		= 'normal'	// 'normal' or 'bold';
config. Left			= false 	// false or true - tooltip on the left of the mouse
config. OffsetX 		= 10		// Horizontal offset of left-top corner from mousepointer
config. OffsetY 		= 10 		// Vertical offset
config. Opacity 		= 90		// Integer between 0 and 100 - opacity of tooltip in percent
config. Padding 		= 10		// Spacing between border and content
config. Shadow			= true	 	// false or true
config. ShadowColor 	= '#C0C0C0'
config. ShadowWidth 	= 3
config. Sticky			= false 	// Do NOT hide tooltip on mouseout? false or true
config. TextAlign		= 'left'	// 'left', 'right' or 'justify'
config. Title			= ''		// Default title text applied to all tips (no default title: empty string '')
config. TitleAlign		= 'left'	// 'left' or 'right' - text alignment inside the title bar
config. TitleBgColor	= ''		// If empty string '', BorderColor will be used
config. TitleFontColor	= '#ffffff'	// Color of title text - if '', BgColor (of tooltip body) will be used
config. TitleFontFace	= 'verdana'		// If '' use FontFace (boldified)
config. TitleFontSize	= '10px'		// If '' use FontSize
config. Width			= 0 		// Tooltip width; 0 for automatic adaption to tooltip content
//=======  END OF TOOLTIP CONFIG, DO NOT CHANGE ANYTHING BELOW  ==============//

// JavaScript Document
var mresize = function(){
		
		var innerWidth = window.innerWidth;

		if( !innerWidth ){ return false;}

		document.documentElement.style.fontSize = ( innerWidth*625/750 ) + '%';

	};
mresize();
window.addEventListener( 'resize' , mresize , false );
window.addEventListener( 'load' , mresize , false );
setTimeout(function(){
		mresize();
	},0)

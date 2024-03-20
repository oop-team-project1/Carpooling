if($("#rev_slider_477_1").length !== 0) {				
		var tpj=jQuery;
			
			var revapi477;
			tpj(document).ready(function() {
				if(tpj("#rev_slider_477_1").revolution == undefined){
					revslider_showDoubleJqueryError("#rev_slider_477_1");
				}else{
					revapi477 = tpj("#rev_slider_477_1").show().revolution({
						sliderType:"standard",
jsFileLocation:"revolution/js/",
						sliderLayout:"fullscreen",
						dottedOverlay:"none",
						delay:9000,
						navigation: {
							onHoverStop:"off",
						},
						responsiveLevels:[1240,1024,778,480],
						visibilityLevels:[1240,1024,778,480],
						gridwidth:[1240,1024,778,480],
						gridheight:[868,768,960,720],
						lazyType:"none",
						shadow:0,
						spinner:"off",
						stopLoop:"on",
						stopAfterLoops:0,
						stopAtSlide:1,
						shuffle:"off",
						autoHeight:"off",
						fullScreenAutoWidth:"off",
						fullScreenAlignForce:"off",
						fullScreenOffsetContainer: "",
						fullScreenOffset: "",
						disableProgressBar:"on",
						hideThumbsOnMobile:"off",
						hideSliderAtLimit:0,
						hideCaptionAtLimit:0,
						hideAllCaptionAtLilmit:0,
						debugMode:false,
						fallbacks: {
							simplifyAll:"off",
							nextSlideOnWindowFocus:"off",
							disableFocusListener:false,
						}
					});
				}
			});	/*ready*/


			// SET TARGET DATE TO START COUNT DOWN FROM
// SET UNLIMITED TIME STAMPS TO JUMP TO OTHER SLIDES BASED ON THE REST TIME VIA slidechanges
// SET THE JUMP AHEAD VIA THE QUICK JUMP  (15000 == 15 sec)
// DONT FORGET TO DEFINE THE CONTAINER ID 

var targetdate =  new Date().getTime() + 864000000 // i.e. '2015/12/31 24:00',
    slidechanges = [
                    { days:0, hours:0, minutes:0, seconds:12, slide:2},
                    { days:0, hours:0, minutes:0, seconds:0, slide:3}
                    ],
    quickjump = 15000,   
    api = revapi477;

    










// countdown.js jQuery Engine MADE BY HILIOS
// https://github.com/hilios/jQuery.countdown

}


var currentd,currenth,currentm,currents;

function animateAndUpdate(o,nt,ot) {
   if (ot==undefined) {    
     o.html(nt);
   } else {      
      if (o.css("opacity")>0) {
      punchgs.TweenLite.fromTo(o,0.45,
  		{autoAlpha:1,rotationY:0,scale:1},
  		{autoAlpha:0,rotationY:-180,scale:0.5,ease:punchgs.Back.easeIn,onComplete:function() { o.html(nt);} });

  punchgs.TweenLite.fromTo(o,0.45,
  		{autoAlpha:0,rotationY:180,scale:0.5},
  		{autoAlpha:1,rotationY:0,scale:1,ease:punchgs.Back.easeOut,delay:0.5 });
      } else {
         o.html(nt);
      }
   }
  return nt;
}

function countprocess(event) {

  var newd = event.strftime('%D'),
      newh = event.strftime('%H'),
      newm = event.strftime('%M'),
      news = event.strftime('%S');
  if (newd != currentd) currentd = animateAndUpdate(jQuery('#c_days'),newd,currentd);
  if (newh != currenth) currenth = animateAndUpdate(jQuery('#c_hours'),newh,currenth);
  if (newm != currentm) currentm = animateAndUpdate(jQuery('#c_minutes'),newm,currentm);
  if (news != currents) currents = animateAndUpdate(jQuery('#c_seconds'),news,currents);

  jQuery.each(slidechanges,function(i,obj) {
    var dr = obj.days==undefined || obj.days>=newd,
        hr = obj.hours==undefined || obj.hours>=newh,
        mr = obj.minutes==undefined || obj.minutes>=newm,
        sr = obj.seconds==undefined || obj.seconds>=news;
      if (dr && hr && mr && sr && !obj.changedown) {
         api.revshowslide(obj.slide);
         obj.changedown = true;
      }
  });
}

jQuery('#skipahead').click(function(){
  var smalloffset = new Date().getTime() + quickjump;
 api.countdown(smalloffset,countprocess);
});


			


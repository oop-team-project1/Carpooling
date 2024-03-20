if($("#rev_slider_1067_1").length !== 0) {				
		var tpj=jQuery;
			
			var revapi1067;
			tpj(document).ready(function() {
				if(tpj("#rev_slider_1067_1").revolution == undefined){
					revslider_showDoubleJqueryError("#rev_slider_1067_1");
				}else{
					revapi1067 = tpj("#rev_slider_1067_1").show().revolution({
						sliderType:"hero",
						jsFileLocation:"revolution/js/",
						sliderLayout:"auto",
						dottedOverlay:"none",
						delay:9000,
						navigation: {
						},
						visibilityLevels:[1350,1024,778,480],
						gridwidth:1350,
						gridheight:700,
						lazyType:"none",
						parallax: {
							type:"3D",
							origo:"slidercenter",
							speed:9000,
							/*levels:[5,10,20,30,25,30,35,40,45,50,47,48,49,50,51,55],*/
							ddd_shadow:"off",
							ddd_bgfreeze:"off",
							ddd_overflow:"hidden",
							ddd_layer_overflow:"visible",
							ddd_z_correction:65,
						},
						spinner:"on",
						spinner: 'spinner2',
						autoHeight:"off",
						disableProgressBar:"on",
						hideThumbsOnMobile:"off",
						hideSliderAtLimit:0,
						hideCaptionAtLimit:0,
						hideAllCaptionAtLilmit:0,
						debugMode:false,
						fallbacks: {
							simplifyAll:"off",
							disableFocusListener:false,
						}
					});
				}
			});	/*ready*/
}

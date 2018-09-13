//[Dashboard Javascript]

//Project:	BonitoPro Admin - Responsive Admin Template
//Primary use:   Used only for the main dashboard (index.html)


$(function () {

  'use strict';
	
	

		
		$("#baralc").sparkline([3,14,5,6,37,5,8,54,2,8,16.15,18,2,3,14,5,6,37,5,8,54,2,8,16.15,18,2,3,14], {
			type: 'bar',
			height: '80',
			width: '100%',
			barWidth: 6,
			barSpacing: 4,
			barColor: '#ef483e',
		});
		
		$('#revenue').sparkline([6,10,9,11,9,10,12,14,15,16,7,15,8,14,12], {
			type: 'bar',
			height: '150',
			barWidth: '5',
			width: '100%',
			resize: true,
			barSpacing: '7',
			barColor: '#ba69aa'
		});
	
		WeatherIcon.add('icon1'	, WeatherIcon.SLEET , {stroke:false , shadow:false , animated:true } );
	
// Sales Groth
	
  Morris.Area({
        element: 'morris-area-chart'
        , data: [{
                year: '2010'
                , online: 0
                , instore: 0
        }, {
                year: '2011'
                , online: 140
                , instore: 120
        }, {
                year: '2012'
                , online: 90
                , instore: 70
        }, {
                year: '2013'
                , online: 80
                , instore: 210
        }, {
                year: '2014'
                , online: 190
                , instore: 160
        }, {
                year: '2015'
                , online: 115
                , instore: 110
        }
            , {
                year: '2016'
                , online: 260
                , instore: 160
        }]
        , xkey: 'year'
        , ykeys: ['online', 'instore']
        , labels: ['Online', 'In Store']
        , pointSize: 0
        , fillOpacity: 0.1  
        , pointStrokeColors: ['#ba69aa', '#ef483e']
        , behaveLikeLine: true
        , gridLineColor: '#f6f6f6'
        , lineWidth:3
        , hideHover: 'auto'
        , lineColors: ['#ba69aa', '#ef483e']
        , resize: true
    });


}); // End of use strict


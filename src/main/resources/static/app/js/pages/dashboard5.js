//[Dashboard Javascript]

//Project:	BonitoPro Admin - Responsive Admin Template
//Primary use:   Used only for the main dashboard (index.html)


$(function () {

  'use strict';
	

// Sparkline charts
  var myvalues = [1300, 500, 1920, 927, 831, 1127, 719, 1930, 1221];
  $('#sparkline-1').sparkline(myvalues, {
    type     : 'line',
    lineColor: '#ba69aa',
    fillColor: '#ba69aa',
    height   : '50',
    width    : '70'
  });
  myvalues = [715, 319, 620, 342, 662, 990, 730, 467, 559, 340, 881];
  $('#sparkline-2').sparkline(myvalues, {
    type     : 'line',
    lineColor: '#69cce0',
    fillColor: '#69cce0',
    height   : '50',
    width    : '70'
  });
  myvalues = [88, 49, 22,35, 45, 72, 11, 55, 25, 19, 27];
  $('#sparkline-3').sparkline(myvalues, {
    type     : 'line',
    lineColor: '#ef483e',
    fillColor: '#ef483e',
    height   : '50',
    width    : '70'
  });	
		
// donut chart
		$('.donut').peity('donut');
	

  // bootstrap WYSIHTML5 - text editor
  $('.textarea').wysihtml5();
// world-map
// jvectormap data
  var visitorsData = {
    US: 398, // USA
    SA: 400, // Saudi Arabia
    CA: 1000, // Canada
    DE: 500, // Germany
    FR: 760, // France
    CN: 300, // China
    AU: 700, // Australia
    BR: 600, // Brazil
    IN: 800, // India
    GB: 320, // Great Britain
    RU: 2000 // Russia
  };
  // World map by jvectormap
  $('#world-map').vectorMap({
    map              : 'world_mill_en',
    backgroundColor  : 'transparent',
    regionStyle      : {
      initial: {
        fill            : '#c9d6de',
        'fill-opacity'  : 1,
        stroke          : 'none',
        'stroke-width'  : 0,
        'stroke-opacity': 1
      }
    },
    series           : {
      regions: [
        {
          values           : visitorsData,
          scale            : ['#b6d6ff', '#005ace'],
          normalizeFunction: 'polynomial'
        }
      ]
    },
    onRegionLabelShow: function (e, el, code) {
      if (typeof visitorsData[code] != 'undefined')
        el.html(el.html() + ': ' + visitorsData[code] + ' new visitors');
    }
  });	
	

//  chart
 Morris.Area({
        element: 'morris-area-chart2',
        data: [{
            period: '2012',
            SiteA: 0,
            SiteB: 0,
            SiteC: 0,
            SiteD: 0,
            
        }, {
            period: '2013',
            SiteA: 120,
            SiteB: 110,
            SiteC: 90,
            SiteD: 130,
            
        }, {
            period: '2014',
            SiteA: 90,
            SiteB: 70,
            SiteC: 80,
            SiteD: 50,
            
        }, {
            period: '2015',
            SiteA: 80,
            SiteB: 180,
            SiteC: 150,
            SiteD: 110,
            
        }, {
            period: '2016',
            SiteA: 200,
            SiteB: 160,
            SiteC: 110,
            SiteD: 180,
            
        }, {
            period: '2017',
            SiteA: 125,
            SiteB: 110,
            SiteC: 200,
            SiteD: 105,
            
        },
         {
            period: '2018',
            SiteA: 230,
            SiteB: 130,
            SiteC: 80,
            SiteD: 150,
           
        }],
        xkey: 'period',
        ykeys: ['SiteA', 'SiteB', 'SiteC', 'SiteD'],
        labels: ['Site A', 'Site B', 'Site C', 'Site D'],
        pointSize: 0,
        fillOpacity: 0,
        pointStrokeColors:['#ba69aa', '#ffd13f', '#69cce0', '#ef483e'],
        behaveLikeLine: true,
        gridLineColor: '#e0e0e0',
        lineWidth: 3,
        smooth: true,
        hideHover: 'auto',
        lineColors: ['#ba69aa', '#ffd13f', '#69cce0', '#ef483e'],
        resize: true
        
    });
	
	
}); // End of use strict

//[Dashboard Javascript]

//Project:	BonitoPro Admin - Responsive Admin Template
//Primary use:   Used only for the main dashboard (index.html)


$(function () {

  'use strict';
	

	//Daily-inquery
	
	var donut = new Morris.Donut({
      element: 'daily-inquery',
      resize: true,
      colors: ["#ba69aa", "#05b085", "#69cce0"],
      data: [
        {label: "On Site", value: 150},
        {label: "By eMail", value: 80},
        {label: "By Phone", value: 110}
      ],
      hideHover: 'auto'
    });

  // Property Stats
    var area = new Morris.Area({
      element: 'property-stats',
      resize: true,
      data: [
        { y: '2018-01', a: 140,  b: 250 },
		{ y: '2018-02', a: 220,  b: 300 },
		{ y: '2018-03', a: 140,  b: 255 },
		{ y: '2018-04', a: 330,  b: 425 },
		{ y: '2018-05', a: 555,  b: 435 },
		{ y: '2018-06', a: 356,  b: 280 },
		{ y: '2018-07', a: 200,  b: 140 }
      ],
		xkey: 'y',
		ykeys: ['a', 'b'],
		labels: ['Commercial Projects', 'Residential Projects'],
		fillOpacity: 1,
		lineWidth:0,
		lineColors: ['#69cce0', '#ef483e'],
		hideHover: 'auto',
		color: '#ffffff',
        pointSize: 5,
    });
	
	// AREA CHART
    if($('#morris_extra_line_chart').length > 0)
		Morris.Line({
        element: 'morris_extra_line_chart',
        data: [{
            period: '2010',
            direct: 50,
            referrals: 80,
            search: 90,
            social: 20
        }, {
            period: '2011',
            direct: 130,
            referrals: 100,
            search: 190,
            social: 80
        }, {
            period: '2012',
            direct: 80,
            referrals: 60,
            search: 90,
            social: 70
        }, {
            period: '2013',
            direct: 70,
            referrals: 200,
            search: 60,
            social: 140
        }, {
            period: '2014',
            direct: 180,
            referrals: 150,
            search: 80,
            social: 140
        }, {
            period: '2015',
            direct: 105,
            referrals: 100,
            search: 110,
            social: 80
        },
         {
            period: '2016',
            direct: 250,
            referrals: 150,
            search: 80,
            social: 200
        }],
        xkey: 'period',
        ykeys: ['direct', 'referrals', 'search', 'social'],
        labels: ['Direct', 'Referrals', 'Search', 'Social'],
        pointSize: 2,
        fillOpacity: 0,
		lineWidth:2,
		pointStrokeColors:['#ba69aa', '#05b085', '#69cce0', '#ffd13f'],
		behaveLikeLine: true,
		grid: false,
		hideHover: 'auto',
		lineColors: ['#ba69aa', '#05b085', '#69cce0', '#ffd13f'],
		resize: true,
		gridTextColor:'#878787',
		gridTextFamily:"Open Sans"
        
    });
	
	
	
}); // End of use strict

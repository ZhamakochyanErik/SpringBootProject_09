//[Dashboard Javascript]

//Project:	BonitoPro Admin - Responsive Admin Template
//Primary use:   Used only for the main dashboard (index.html)


$(function () {

  'use strict';
	
	
	
// Extra chart
 Morris.Area({
        element: 'extra-area-chart',
        data: [{
                    period: '2010',
                    iphone: 0,
                    ipad: 0,
                    itouch: 0
                }, {
                    period: '2011',
                    iphone: 70,
                    ipad: 35,
                    itouch: 25
                }, {
                    period: '2012',
                    iphone: 40,
                    ipad: 70,
                    itouch: 85
                }, {
                    period: '2013',
                    iphone: 80,
                    ipad: 32,
                    itouch: 7
                }, {
                    period: '2014',
                    iphone: 50,
                    ipad: 40,
                    itouch: 140
                }, {
                    period: '2015',
                    iphone: 55,
                    ipad: 100,
                    itouch: 50
                }, {
                    period: '2016',
                    iphone: 100,
                    ipad: 150,
                    itouch: 190
                }
                ],
                lineColors: ['#ba69aa', '#69cce0', '#ef483e'],
                xkey: 'period',
                ykeys: ['iphone', 'ipad', 'itouch'],
                labels: ['Site A', 'Site B', 'Site C'],
                pointSize: 0,
                lineWidth: 2,
                resize:true,
                fillOpacity: 0.1,
                behaveLikeLine: true,
                gridLineColor: '#e0e0e0',
                hideHover: 'auto'
        
    });
	
	
    //DONUT CHART
    var donut = new Morris.Donut({
      element: 'sales-chart',
      resize: true,
      colors: ["#ba69aa", "#ef483e", "#05b085"],
      data: [
        {label: "Online Sales", value: 50},
        {label: "In-Store Sales", value: 35},
        {label: "Mail-Order Sales", value: 15}
      ],
      hideHover: 'auto'
    });
	
		
	
	$('#invoice-list').DataTable({
	  'paging'      : true,
	  'lengthChange': false,
	  'searching'   : false,
	  'ordering'    : true,
	  'info'        : true,
	  'autoWidth'   : true,
	});	
	

// usa	
$('#usa').vectorMap({
	map : 'us_aea_en',
	backgroundColor : 'transparent',
	regionStyle : {
		initial : {
			fill : '#c9d6de'
		}
	}
});


}); // End of use strict

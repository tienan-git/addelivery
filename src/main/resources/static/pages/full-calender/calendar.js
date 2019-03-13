/*<![CDATA[*/
"use strict";

	$(document).ready(function() {

	    var date = new Date();
	    var d = date.getDate();
	    var m = date.getMonth();
	    var y = date.getFullYear();

	    var events_array = [{
//            title: 'Business Lunch',
	    	
            title: /*[[${title}]]*/ 'Business Lunch',
        	
            start: '2016-09-03T13:00:00',
            constraint: 'businessHours',
			borderColor: '#FC6180',
			backgroundColor: '#FC6180',
			textColor: '#fff'
        }, {
            title: 'Meeting',
            start: '2016-09-13T11:00:00',
            constraint: 'availableForMeeting',
            editable: true,
            borderColor: '#4680ff',
            backgroundColor: '#4680ff',
            textColor: '#fff'
        }, {
            title: 'Conference',
            start: '2016-09-18',
            end: '2016-09-20',
			borderColor: '#93BE52',
			backgroundColor: '#93BE52',
			textColor: '#fff'
        }, {
            title: 'Party',
            start: '2016-09-29T20:00:00',
			borderColor: '#FFB64D',
			backgroundColor: '#FFB64D',
			textColor: '#fff'
        },

        // areas where "Meeting" must be dropped
        {
            id: 'availableForMeeting',
            start: '2016-09-11T10:00:00',
            end: '2016-09-11T16:00:00',
            rendering: 'background',
			borderColor: '#ab7967',
			backgroundColor: '#ab7967',
			textColor: '#fff'
        }, {
            id: 'availableForMeeting',
            start: '2016-09-13T10:00:00',
            end: '2016-09-13T16:00:00',
            rendering: 'background',
			borderColor: '#39ADB5',
			backgroundColor: '#39ADB5',
			textColor: '#fff'
        },

        // red areas where no events can be dropped
        {
            start: '2016-09-24',
            end: '2016-09-28',
            overlap: false,
            rendering: 'background',
			borderColor: '#FFB64D',
			backgroundColor: '#FFB64D',
            color: '#d8d6d6'
        }, {
            start: '2016-09-06',
            end: '2016-09-08',
            overlap: false,
            rendering: 'background',
			borderColor: '#ab7967',
			backgroundColor: '#ab7967',
            color: '#d8d6d6'
        }
    ];

	    $('#calendar').fullCalendar({
	        header: {
	            left: 'prev,next today',
	            center: 'title',
	            right: 'month,agendaWeek,agendaDay'
	        },
	        defaultDate: '2016-09-12',
	        navLinks: true, // can click day/week names to navigate views
	        businessHours: true, // display business hours
	        selectable: true,
	        editable: true,
//	        droppable: true,
	        events: events_array,
	        eventRender: function(event, element) {
	            element.attr('title', event.tip);
	        },
	        select: function(start, end, allDay) {
	    var title = prompt('Event Title:');
	    if (title) {
	    	$('#calendar').fullCalendar('renderEvent',
	            {
	                title: title,
	                start: start,
	                end: end,
	                allDay: allDay
	            },
	            true // make the event "stick"
	        );
	        /**
	         * ajax call to store event in DB
	         */
	        jQuery.post(
	            "event/new" // your url
	            , { // re-use event's data
	                title: title,
	                start: start,
	                end: end,
	                allDay: allDay
	            }
	        );
	    }
	    $('#calendar').fullCalendar('unselect');
	},
	  eventClick: function(eventObj) {
	      if (eventObj.url) {
	        alert(
	          'Clicked ' + eventObj.title + '.\n' +
	          'Will open ' + eventObj.url + ' in a new tab'
	        );

	        window.open(eventObj.url);

	        return false; // prevents browser from following link in current tab.
	      } else {
	        alert('Clicked ' + eventObj.title);
	      }
	    }

	        
	    });
	});
	
	/*]]>*/	
/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.4.0
build: nightly
*/
YUI.add("dd-ddm",function(a){a.mix(a.DD.DDM,{_pg:null,_debugShim:false,_activateTargets:function(){},_deactivateTargets:function(){},_startDrag:function(){if(this.activeDrag&&this.activeDrag.get("useShim")){this._pg_activate();this._activateTargets();}},_endDrag:function(){this._pg_deactivate();this._deactivateTargets();},_pg_deactivate:function(){this._pg.setStyle("display","none");},_pg_activate:function(){var b=this.activeDrag.get("activeHandle"),c="auto";if(b){c=b.getStyle("cursor");}if(c=="auto"){c=this.get("dragCursor");}this._pg_size();this._pg.setStyles({top:0,left:0,display:"block",opacity:((this._debugShim)?".5":"0"),cursor:c});},_pg_size:function(){if(this.activeDrag){var c=a.one("body"),e=c.get("docHeight"),d=c.get("docWidth");this._pg.setStyles({height:e+"px",width:d+"px"});}},_createPG:function(){var d=a.Node.create("<div></div>"),b=a.one("body"),c;d.setStyles({top:"0",left:"0",position:"absolute",zIndex:"9999",overflow:"hidden",backgroundColor:"red",display:"none",height:"5px",width:"5px"});d.set("id",a.stamp(d));d.addClass(a.DD.DDM.CSS_PREFIX+"-shim");b.prepend(d);this._pg=d;this._pg.on("mousemove",a.throttle(a.bind(this._move,this),this.get("throttleTime")));this._pg.on("mouseup",a.bind(this._end,this));c=a.one("win");a.on("window:resize",a.bind(this._pg_size,this));c.on("scroll",a.bind(this._pg_size,this));}},true);},"3.4.0",{skinnable:false,requires:["dd-ddm-base","event-resize"]});
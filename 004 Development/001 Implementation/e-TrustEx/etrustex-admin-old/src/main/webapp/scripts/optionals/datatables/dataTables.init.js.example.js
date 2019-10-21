(function(a){a.extend({_initDataTable:function(g){var h=a.extend({tableId:null,displayType:"NORMAL",verticalFixedScrollHeight:250,isDisplayTitle:false,isDisplayPaginate:false,isStateSave:false,displayLength:-1,isDisplayFilter:false,isDisplayFilterInfoSpan:true,isDisplayExport:false,isDisplayInfo:false,isSortEnabled:true,aSortDirectionsArray:[[0,"asc"]],aSortDisabledColsArray:[],aCustomAoColumnDefsArray:[],fnDrawCallback:function(){},fnPreDrawCallback:function(){}},g);if(a._language=="fr"){if(a._getData("datatables_oLanguage_sLengthMenu")==null){a._setData("datatables_oLanguage_sLengthMenu","Display _MENU_ records per page")}if(a._getData("datatables_oLanguage_sZeroRecords")==null){a._setData("datatables_oLanguage_sZeroRecords","Nothing found - sorry")}if(a._getData("datatables_oLanguage_sInfoEmpty")==null){a._setData("datatables_oLanguage_sInfoEmpty","Showing 0 to 0 of 0 records")}if(a._getData("datatables_oLanguage_sInfoFiltered")==null){a._setData("datatables_oLanguage_sInfoFiltered","(total:_MAX_)")}if(a._getData("datatables_oLanguage_sProcessing")==null){a._setData("datatables_oLanguage_sProcessing","Please wait...")}if(a._getData("datatables_oLanguage_sInfoPostFix")==null){a._setData("datatables_oLanguage_sInfoPostFix","")}if(a._getData("datatables_oLanguage_sSearch")==null){a._setData("datatables_oLanguage_sSearch","filter")}if(a._getData("datatables_oLanguage_oPaginate_sFirst")==null){a._setData("datatables_oLanguage_oPaginate_sFirst","<<")}if(a._getData("datatables_oLanguage_oPaginate_sPrevious")==null){a._setData("datatables_oLanguage_oPaginate_sPrevious","<")}if(a._getData("datatables_oLanguage_oPaginate_sNext")==null){a._setData("datatables_oLanguage_oPaginate_sNext",">")}if(a._getData("datatables_oLanguage_oPaginate_sLast")==null){a._setData("datatables_oLanguage_oPaginate_sLast",">>")}}else{if(a._getData("datatables_oLanguage_sLengthMenu")==null){a._setData("datatables_oLanguage_sLengthMenu","Display _MENU_ records per page")}if(a._getData("datatables_oLanguage_sZeroRecords")==null){a._setData("datatables_oLanguage_sZeroRecords","Nothing found - sorry")}if(a._getData("datatables_oLanguage_sInfoEmpty")==null){a._setData("datatables_oLanguage_sInfoEmpty","Showing 0 to 0 of 0 records")}if(a._getData("datatables_oLanguage_sInfoFiltered")==null){a._setData("datatables_oLanguage_sInfoFiltered","(total:_MAX_)")}if(a._getData("datatables_oLanguage_sProcessing")==null){a._setData("datatables_oLanguage_sProcessing","Please wait...")}if(a._getData("datatables_oLanguage_sInfoPostFix")==null){a._setData("datatables_oLanguage_sInfoPostFix","")}if(a._getData("datatables_oLanguage_sSearch")==null){a._setData("datatables_oLanguage_sSearch","filter")}if(a._getData("datatables_oLanguage_oPaginate_sFirst")==null){a._setData("datatables_oLanguage_oPaginate_sFirst","<<")}if(a._getData("datatables_oLanguage_oPaginate_sPrevious")==null){a._setData("datatables_oLanguage_oPaginate_sPrevious","<")}if(a._getData("datatables_oLanguage_oPaginate_sNext")==null){a._setData("datatables_oLanguage_oPaginate_sNext",">")}if(a._getData("datatables_oLanguage_oPaginate_sLast")==null){a._setData("datatables_oLanguage_oPaginate_sLast",">>")}}var b=a._getContextPath();if(h.isSortEnabled){if(h.aSortDirectionsArray==null||h.aSortDirectionsArray=="undefined"){h.aSortDirectionsArray=[[0,"asc"]]}if(h.aSortDisabledColsArray==null||h.aSortDisabledColsArray=="undefined"){h.aSortDisabledColsArray=[]}}else{h.aSortDirectionsArray=[];h.aSortDisabledColsArray=[]}var d;if(h.isDisplayExport){if(h.isDisplayTitle){d='<"H"Tfrl>t<"F"ip>'}else{d='<"H"Tfrli>t<"F"p>'}}else{if(h.isDisplayTitle){d='<"H"frl>t<"F"ip>'}else{d='<"H"frli>t<"F"p>'}}var c;if(h.isDisplayPaginate){c=a._getData("datatables_oLanguage_sInfo_pagination")}else{c=a._getData("datatables_oLanguage_sInfo_no-pagination")}var e=[{bSortable:false,aTargets:h.aSortDisabledColsArray}];for(var f=0;f<h.aCustomAoColumnDefsArray.length;f++){e.push(h.aCustomAoColumnDefsArray[f])}if(h.displayType=="VERTICAL_SCROLL"){a("#"+h.tableId).dataTable({bDestroy:true,bSort:h.isSortEnabled,aaSorting:h.aSortDirectionsArray,aoColumnDefs:e,bJQueryUI:true,bPaginate:true,bStateSave:h.isStateSave,bLengthChange:h.isDisplayTitle,iDisplayLength:-1,sScrollY:h.verticalFixedScrollHeight,bFilter:h.isDisplayFilter,oLanguage:{sLengthMenu:a._getData("datatables_oLanguage_sLengthMenu"),sZeroRecords:a._getData("datatables_oLanguage_sZeroRecords"),sInfo:c,sInfoEmpty:a._getData("datatables_oLanguage_sInfoEmpty"),sInfoFiltered:a._getData("datatables_oLanguage_sInfoFiltered"),sProcessing:a._getData("datatables_oLanguage_sProcessing"),sInfoPostFix:a._getData("datatables_oLanguage_sInfoPostFix"),sSearch:a._getData("datatables_oLanguage_sSearch"),sUrl:"",oPaginate:{sFirst:a._getData("datatables_oLanguage_oPaginate_sFirst"),sPrevious:a._getData("datatables_oLanguage_oPaginate_sPrevious"),sNext:a._getData("datatables_oLanguage_oPaginate_sNext"),sLast:a._getData("datatables_oLanguage_oPaginate_sLast")}},bInfo:h.isDisplayInfo,sDom:d,fnInitComplete:function(){if(h.isDisplayTitle){a("#"+h.tableId+"_length").empty().html(a("#"+h.tableId+"_title").html())}a("#"+h.tableId+"_paginate").empty();a("#"+h.tableId+"_wrapper > div:last").hide();a("#"+h.tableId+"_wrapper > div:first").css({padding:"0",background:"none"})}})}if(h.displayType=="NORMAL"){if(!h.isDisplayPaginate){h.displayLength=-1}a("#"+h.tableId).dataTable({bDestroy:true,bSort:h.isSortEnabled,aaSorting:h.aSortDirectionsArray,aoColumnDefs:e,bJQueryUI:true,bLengthChange:h.isDisplayTitle,iDisplayLength:h.displayLength,bInfo:h.isDisplayInfo,bPaginate:true,bFilter:h.isDisplayFilter,oLanguage:{sLengthMenu:a._getData("datatables_oLanguage_sLengthMenu"),sZeroRecords:a._getData("datatables_oLanguage_sZeroRecords"),sInfo:c,sInfoEmpty:a._getData("datatables_oLanguage_sInfoEmpty"),sInfoFiltered:a._getData("datatables_oLanguage_sInfoFiltered"),sProcessing:a._getData("datatables_oLanguage_sProcessing"),sInfoPostFix:a._getData("datatables_oLanguage_sInfoPostFix"),sSearch:a._getData("datatables_oLanguage_sSearch"),sUrl:"",oPaginate:{sFirst:a._getData("datatables_oLanguage_oPaginate_sFirst"),sPrevious:a._getData("datatables_oLanguage_oPaginate_sPrevious"),sNext:a._getData("datatables_oLanguage_oPaginate_sNext"),sLast:a._getData("datatables_oLanguage_oPaginate_sLast")}},sPaginationType:"full_numbers",sDom:d,oTableTools:{sSwfPath:b+"/scripts/optionals/datatables/swf/copy_cvs_xls_pdf.swf",aButtons:[{sExtends:"copy",sButtonText:""},{sExtends:"xls",sButtonText:""},{sExtends:"pdf",sButtonText:"",sPdfOrientation:"landscape",sPdfMessage:"This is the title of the PDF"},{sExtends:"print",sButtonText:""}]},fnInitComplete:function(){if(h.isDisplayTitle){a("#"+h.tableId+"_length").empty().html(a("#"+h.tableId+"_title").html())}if(!h.isDisplayPaginate){a("#"+h.tableId+"_paginate").empty();if(!h.isDisplayInfo&&h.isDisplayTitle){a("#"+h.tableId+"_wrapper > div:last").hide()}if(h.isDisplayInfo&&!h.isDisplayTitle){a("#"+h.tableId+"_wrapper > div:last").hide()}}if(h.isDisplayInfo&&!h.isDisplayTitle){a("#"+h.tableId+"_info").addClass("topInfo")}if(!h.isDisplayTitle&&!h.isDisplayFilter&&!h.isDisplayExport){a("#"+h.tableId+"_wrapper > div:first").hide()}if(h.isDisplayTitle&&!h.isDisplayFilter&&!h.isDisplayExport){a("#"+h.tableId+"_wrapper > div:first").css({padding:"0",background:"none"})}if(h.isDisplayFilter){if(h.isDisplayFilterInfoSpan&&!a._isIE7()){var i=a(document.createElement("span")).css("float","right").css("padding-top","5px").addClass("icon-hint").addClass("icon").addClass("fl-space").attr("title",a._getData("datatables_oLanguage_sSearchTitle"));a("#"+h.tableId+"_filter input").addClass("field-value").parent("label").after(i)}else{a("#"+h.tableId+"_filter input").addClass("field-value")}}},fnDrawCallback:function(){var i=a("#"+h.tableId);i.find("tr").removeClass("zebra1").removeClass("zebra2");i.find("tr.odd:not(sub-row)").addClass("zebra1").removeClass("zebra2");i.find("tr.even:not(sub-row)").addClass("zebra2").removeClass("zebra1");h.fnDrawCallback()},fnPreDrawCallback:function(){h.fnPreDrawCallback()}})}}})})(jQuery);
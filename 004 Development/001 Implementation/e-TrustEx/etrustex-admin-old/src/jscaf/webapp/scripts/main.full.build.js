(function(b,a){if(typeof module==="object"&&typeof module.exports==="object"){module.exports=b.document?a(b,true):function(c){if(!c.document){throw new Error("jQuery requires a window with a document")}return a(c)}}else{a(b)}}(typeof window!=="undefined"?window:this,function(a4,au){var aO=[];var O=aO.slice;var ay=aO.concat;var w=aO.push;var bT=aO.indexOf;var ab={};var x=ab.toString;var J=ab.hasOwnProperty;var C={};var ah="1.11.2",bH=function(e,i){return new bH.fn.init(e,i)},D=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,bR=/^-ms-/,aV=/-([\da-z])/gi,N=function(e,i){return i.toUpperCase()};bH.fn=bH.prototype={jquery:ah,constructor:bH,selector:"",length:0,toArray:function(){return O.call(this)},get:function(e){return e!=null?(e<0?this[e+this.length]:this[e]):O.call(this)},pushStack:function(e){var i=bH.merge(this.constructor(),e);i.prevObject=this;i.context=this.context;return i},each:function(i,e){return bH.each(this,i,e)},map:function(e){return this.pushStack(bH.map(this,function(b6,b5){return e.call(b6,b5,b6)}))},slice:function(){return this.pushStack(O.apply(this,arguments))},first:function(){return this.eq(0)},last:function(){return this.eq(-1)},eq:function(b6){var e=this.length,b5=+b6+(b6<0?e:0);return this.pushStack(b5>=0&&b5<e?[this[b5]]:[])},end:function(){return this.prevObject||this.constructor(null)},push:w,sort:aO.sort,splice:aO.splice};bH.extend=bH.fn.extend=function(){var e,ca,b5,b6,cd,cb,b9=arguments[0]||{},b8=1,b7=arguments.length,cc=false;if(typeof b9==="boolean"){cc=b9;b9=arguments[b8]||{};b8++}if(typeof b9!=="object"&&!bH.isFunction(b9)){b9={}}if(b8===b7){b9=this;b8--}for(;b8<b7;b8++){if((cd=arguments[b8])!=null){for(b6 in cd){e=b9[b6];b5=cd[b6];if(b9===b5){continue}if(cc&&b5&&(bH.isPlainObject(b5)||(ca=bH.isArray(b5)))){if(ca){ca=false;cb=e&&bH.isArray(e)?e:[]}else{cb=e&&bH.isPlainObject(e)?e:{}}b9[b6]=bH.extend(cc,cb,b5)}else{if(b5!==undefined){b9[b6]=b5}}}}}return b9};bH.extend({expando:"jQuery"+(ah+Math.random()).replace(/\D/g,""),isReady:true,error:function(e){throw new Error(e)},noop:function(){},isFunction:function(e){return bH.type(e)==="function"},isArray:Array.isArray||function(e){return bH.type(e)==="array"},isWindow:function(e){return e!=null&&e==e.window},isNumeric:function(e){return !bH.isArray(e)&&(e-parseFloat(e)+1)>=0},isEmptyObject:function(i){var e;for(e in i){return false}return true},isPlainObject:function(b6){var i;if(!b6||bH.type(b6)!=="object"||b6.nodeType||bH.isWindow(b6)){return false}try{if(b6.constructor&&!J.call(b6,"constructor")&&!J.call(b6.constructor.prototype,"isPrototypeOf")){return false}}catch(b5){return false}if(C.ownLast){for(i in b6){return J.call(b6,i)}}for(i in b6){}return i===undefined||J.call(b6,i)},type:function(e){if(e==null){return e+""}return typeof e==="object"||typeof e==="function"?ab[x.call(e)]||"object":typeof e},globalEval:function(e){if(e&&bH.trim(e)){(a4.execScript||function(i){a4["eval"].call(a4,i)})(e)}},camelCase:function(e){return e.replace(bR,"ms-").replace(aV,N)},nodeName:function(i,e){return i.nodeName&&i.nodeName.toLowerCase()===e.toLowerCase()},each:function(b9,ca,b5){var b8,b6=0,b7=b9.length,e=ac(b9);if(b5){if(e){for(;b6<b7;b6++){b8=ca.apply(b9[b6],b5);if(b8===false){break}}}else{for(b6 in b9){b8=ca.apply(b9[b6],b5);if(b8===false){break}}}}else{if(e){for(;b6<b7;b6++){b8=ca.call(b9[b6],b6,b9[b6]);if(b8===false){break}}}else{for(b6 in b9){b8=ca.call(b9[b6],b6,b9[b6]);if(b8===false){break}}}}return b9},trim:function(e){return e==null?"":(e+"").replace(D,"")},makeArray:function(e,b5){var i=b5||[];if(e!=null){if(ac(Object(e))){bH.merge(i,typeof e==="string"?[e]:e)}else{w.call(i,e)}}return i},inArray:function(b7,b5,b6){var e;if(b5){if(bT){return bT.call(b5,b7,b6)}e=b5.length;b6=b6?b6<0?Math.max(0,e+b6):b6:0;for(;b6<e;b6++){if(b6 in b5&&b5[b6]===b7){return b6}}}return -1},merge:function(b8,b6){var e=+b6.length,b5=0,b7=b8.length;while(b5<e){b8[b7++]=b6[b5++]}if(e!==e){while(b6[b5]!==undefined){b8[b7++]=b6[b5++]}}b8.length=b7;return b8},grep:function(e,cb,b8){var ca,b7=[],b5=0,b6=e.length,b9=!b8;for(;b5<b6;b5++){ca=!cb(e[b5],b5);if(ca!==b9){b7.push(e[b5])}}return b7},map:function(b6,cb,e){var ca,b8=0,b9=b6.length,b5=ac(b6),b7=[];if(b5){for(;b8<b9;b8++){ca=cb(b6[b8],b8,e);if(ca!=null){b7.push(ca)}}}else{for(b8 in b6){ca=cb(b6[b8],b8,e);if(ca!=null){b7.push(ca)}}}return ay.apply([],b7)},guid:1,proxy:function(b7,b6){var e,b5,i;if(typeof b6==="string"){i=b7[b6];b6=b7;b7=i}if(!bH.isFunction(b7)){return undefined}e=O.call(arguments,2);b5=function(){return b7.apply(b6||this,e.concat(O.call(arguments)))};b5.guid=b7.guid=b7.guid||bH.guid++;return b5},now:function(){return +(new Date())},support:C});bH.each("Boolean Number String Function Array Date RegExp Object Error".split(" "),function(b5,e){ab["[object "+e+"]"]=e.toLowerCase()});function ac(b5){var i=b5.length,e=bH.type(b5);if(e==="function"||bH.isWindow(b5)){return false}if(b5.nodeType===1&&i){return true}return e==="array"||i===0||typeof i==="number"&&i>0&&(i-1) in b5}var m=
/*!
 * Sizzle CSS Selector Engine v2.2.0-pre
 * http://sizzlejs.com/
 *
 * Copyright 2008, 2014 jQuery Foundation, Inc. and other contributors
 * Released under the MIT license
 * http://jquery.org/license
 *
 * Date: 2014-12-16
 */
(function(dd){var cx,dg,cm,cG,cJ,ch,cV,df,dl,cH,cW,cY,cB,cn,c7,c2,de,cd,cE,c9="sizzle"+1*new Date(),cI=dd.document,dh=0,c3=0,b8=cz(),c8=cz(),cF=cz(),cD=function(i,e){if(i===e){cW=true}return 0},cP=1<<31,cN=({}).hasOwnProperty,db=[],dc=db.pop,cL=db.push,b6=db.push,cl=db.slice,cc=function(dp,dn){var dm=0,e=dp.length;for(;dm<e;dm++){if(dp[dm]===dn){return dm}}return -1},b7="checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",co="[\\x20\\t\\r\\n\\f]",b5="(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",cK=b5.replace("w","w#"),c5="\\["+co+"*("+b5+")(?:"+co+"*([*^$|!~]?=)"+co+"*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|("+cK+"))|)"+co+"*\\]",cj=":("+b5+")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|"+c5+")*)|.*)\\)|)",ct=new RegExp(co+"+","g"),cq=new RegExp("^"+co+"+|((?:^|[^\\\\])(?:\\\\.)*)"+co+"+$","g"),cu=new RegExp("^"+co+"*,"+co+"*"),cA=new RegExp("^"+co+"*([>+~]|"+co+")"+co+"*"),cs=new RegExp("="+co+"*([^\\]'\"]*?)"+co+"*\\]","g"),cR=new RegExp(cj),cT=new RegExp("^"+cK+"$"),c1={ID:new RegExp("^#("+b5+")"),CLASS:new RegExp("^\\.("+b5+")"),TAG:new RegExp("^("+b5.replace("w","w*")+")"),ATTR:new RegExp("^"+c5),PSEUDO:new RegExp("^"+cj),CHILD:new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\("+co+"*(even|odd|(([+-]|)(\\d*)n|)"+co+"*(?:([+-]|)"+co+"*(\\d+)|))"+co+"*\\)|)","i"),bool:new RegExp("^(?:"+b7+")$","i"),needsContext:new RegExp("^"+co+"*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\("+co+"*((?:-\\d)?\\d*)"+co+"*\\)|)(?=[^-]|$)","i")},cb=/^(?:input|select|textarea|button)$/i,ck=/^h\d$/i,cO=/^[^{]+\{\s*\[native \w/,cQ=/^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,c0=/[+~]/,cM=/'|\\/g,cr=new RegExp("\\\\([\\da-f]{1,6}"+co+"?|("+co+")|.)","ig"),c4=function(e,dn,i){var dm="0x"+dn-65536;return dm!==dm||i?dn:dm<0?String.fromCharCode(dm+65536):String.fromCharCode(dm>>10|55296,dm&1023|56320)},dk=function(){cY()};try{b6.apply((db=cl.call(cI.childNodes)),cI.childNodes);db[cI.childNodes.length].nodeType}catch(cC){b6={apply:db.length?function(i,e){cL.apply(i,cl.call(e))}:function(dp,dn){var e=dp.length,dm=0;while((dp[e++]=dn[dm++])){}dp.length=e-1}}}function cv(du,dm,dy,dA){var dz,dr,ds,dw,dx,dq,dp,e,dn,dv;if((dm?dm.ownerDocument||dm:cI)!==cB){cY(dm)}dm=dm||cB;dy=dy||[];dw=dm.nodeType;if(typeof du!=="string"||!du||dw!==1&&dw!==9&&dw!==11){return dy}if(!dA&&c7){if(dw!==11&&(dz=cQ.exec(du))){if((ds=dz[1])){if(dw===9){dr=dm.getElementById(ds);if(dr&&dr.parentNode){if(dr.id===ds){dy.push(dr);return dy}}else{return dy}}else{if(dm.ownerDocument&&(dr=dm.ownerDocument.getElementById(ds))&&cE(dm,dr)&&dr.id===ds){dy.push(dr);return dy}}}else{if(dz[2]){b6.apply(dy,dm.getElementsByTagName(du));return dy}else{if((ds=dz[3])&&dg.getElementsByClassName){b6.apply(dy,dm.getElementsByClassName(ds));return dy}}}}if(dg.qsa&&(!c2||!c2.test(du))){e=dp=c9;dn=dm;dv=dw!==1&&du;if(dw===1&&dm.nodeName.toLowerCase()!=="object"){dq=ch(du);if((dp=dm.getAttribute("id"))){e=dp.replace(cM,"\\$&")}else{dm.setAttribute("id",e)}e="[id='"+e+"'] ";dx=dq.length;while(dx--){dq[dx]=e+cg(dq[dx])}dn=c0.test(du)&&cS(dm.parentNode)||dm;dv=dq.join(",")}if(dv){try{b6.apply(dy,dn.querySelectorAll(dv));return dy}catch(dt){}finally{if(!dp){dm.removeAttribute("id")}}}}}return df(du.replace(cq,"$1"),dm,dy,dA)}function cz(){var i=[];function e(dm,dn){if(i.push(dm+" ")>cm.cacheLength){delete e[i.shift()]}return(e[dm+" "]=dn)}return e}function ci(e){e[c9]=true;return e}function ce(i){var dn=cB.createElement("div");try{return !!i(dn)}catch(dm){return false}finally{if(dn.parentNode){dn.parentNode.removeChild(dn)}dn=null}}function di(dm,dp){var e=dm.split("|"),dn=dm.length;while(dn--){cm.attrHandle[e[dn]]=dp}}function b9(i,e){var dn=e&&i,dm=dn&&i.nodeType===1&&e.nodeType===1&&(~e.sourceIndex||cP)-(~i.sourceIndex||cP);if(dm){return dm}if(dn){while((dn=dn.nextSibling)){if(dn===e){return -1}}}return i?1:-1}function cw(e){return function(dm){var i=dm.nodeName.toLowerCase();return i==="input"&&dm.type===e}}function ca(e){return function(dm){var i=dm.nodeName.toLowerCase();return(i==="input"||i==="button")&&dm.type===e}}function c6(e){return ci(function(i){i=+i;return ci(function(dm,dr){var dp,dn=e([],dm.length,i),dq=dn.length;while(dq--){if(dm[(dp=dn[dq])]){dm[dp]=!(dr[dp]=dm[dp])}}})})}function cS(e){return e&&typeof e.getElementsByTagName!=="undefined"&&e}dg=cv.support={};cJ=cv.isXML=function(e){var i=e&&(e.ownerDocument||e).documentElement;return i?i.nodeName!=="HTML":false};cY=cv.setDocument=function(dm){var e,i,dn=dm?dm.ownerDocument||dm:cI;if(dn===cB||dn.nodeType!==9||!dn.documentElement){return cB}cB=dn;cn=dn.documentElement;i=dn.defaultView;if(i&&i!==i.top){if(i.addEventListener){i.addEventListener("unload",dk,false)}else{if(i.attachEvent){i.attachEvent("onunload",dk)}}}c7=!cJ(dn);dg.attributes=ce(function(dp){dp.className="i";return !dp.getAttribute("className")});dg.getElementsByTagName=ce(function(dp){dp.appendChild(dn.createComment(""));return !dp.getElementsByTagName("*").length});dg.getElementsByClassName=cO.test(dn.getElementsByClassName);dg.getById=ce(function(dp){cn.appendChild(dp).id=c9;return !dn.getElementsByName||!dn.getElementsByName(c9).length});if(dg.getById){cm.find.ID=function(dr,dq){if(typeof dq.getElementById!=="undefined"&&c7){var dp=dq.getElementById(dr);return dp&&dp.parentNode?[dp]:[]}};cm.filter.ID=function(dq){var dp=dq.replace(cr,c4);return function(dr){return dr.getAttribute("id")===dp}}}else{delete cm.find.ID;cm.filter.ID=function(dq){var dp=dq.replace(cr,c4);return function(ds){var dr=typeof ds.getAttributeNode!=="undefined"&&ds.getAttributeNode("id");return dr&&dr.value===dp}}}cm.find.TAG=dg.getElementsByTagName?function(dp,dq){if(typeof dq.getElementsByTagName!=="undefined"){return dq.getElementsByTagName(dp)}else{if(dg.qsa){return dq.querySelectorAll(dp)}}}:function(dp,dt){var du,ds=[],dr=0,dq=dt.getElementsByTagName(dp);if(dp==="*"){while((du=dq[dr++])){if(du.nodeType===1){ds.push(du)}}return ds}return dq};cm.find.CLASS=dg.getElementsByClassName&&function(dq,dp){if(c7){return dp.getElementsByClassName(dq)}};de=[];c2=[];if((dg.qsa=cO.test(dn.querySelectorAll))){ce(function(dp){cn.appendChild(dp).innerHTML="<a id='"+c9+"'></a><select id='"+c9+"-\f]' msallowcapture=''><option selected=''></option></select>";if(dp.querySelectorAll("[msallowcapture^='']").length){c2.push("[*^$]="+co+"*(?:''|\"\")")}if(!dp.querySelectorAll("[selected]").length){c2.push("\\["+co+"*(?:value|"+b7+")")}if(!dp.querySelectorAll("[id~="+c9+"-]").length){c2.push("~=")}if(!dp.querySelectorAll(":checked").length){c2.push(":checked")}if(!dp.querySelectorAll("a#"+c9+"+*").length){c2.push(".#.+[+~]")}});ce(function(dq){var dp=dn.createElement("input");dp.setAttribute("type","hidden");dq.appendChild(dp).setAttribute("name","D");if(dq.querySelectorAll("[name=d]").length){c2.push("name"+co+"*[*^$|!~]?=")}if(!dq.querySelectorAll(":enabled").length){c2.push(":enabled",":disabled")}dq.querySelectorAll("*,:x");c2.push(",.*:")})}if((dg.matchesSelector=cO.test((cd=cn.matches||cn.webkitMatchesSelector||cn.mozMatchesSelector||cn.oMatchesSelector||cn.msMatchesSelector)))){ce(function(dp){dg.disconnectedMatch=cd.call(dp,"div");cd.call(dp,"[s!='']:x");de.push("!=",cj)})}c2=c2.length&&new RegExp(c2.join("|"));de=de.length&&new RegExp(de.join("|"));e=cO.test(cn.compareDocumentPosition);cE=e||cO.test(cn.contains)?function(dq,dp){var ds=dq.nodeType===9?dq.documentElement:dq,dr=dp&&dp.parentNode;return dq===dr||!!(dr&&dr.nodeType===1&&(ds.contains?ds.contains(dr):dq.compareDocumentPosition&&dq.compareDocumentPosition(dr)&16))}:function(dq,dp){if(dp){while((dp=dp.parentNode)){if(dp===dq){return true}}}return false};cD=e?function(dq,dp){if(dq===dp){cW=true;return 0}var dr=!dq.compareDocumentPosition-!dp.compareDocumentPosition;if(dr){return dr}dr=(dq.ownerDocument||dq)===(dp.ownerDocument||dp)?dq.compareDocumentPosition(dp):1;if(dr&1||(!dg.sortDetached&&dp.compareDocumentPosition(dq)===dr)){if(dq===dn||dq.ownerDocument===cI&&cE(cI,dq)){return -1}if(dp===dn||dp.ownerDocument===cI&&cE(cI,dp)){return 1}return cH?(cc(cH,dq)-cc(cH,dp)):0}return dr&4?-1:1}:function(dq,dp){if(dq===dp){cW=true;return 0}var dw,dt=0,dv=dq.parentNode,ds=dp.parentNode,dr=[dq],du=[dp];if(!dv||!ds){return dq===dn?-1:dp===dn?1:dv?-1:ds?1:cH?(cc(cH,dq)-cc(cH,dp)):0}else{if(dv===ds){return b9(dq,dp)}}dw=dq;while((dw=dw.parentNode)){dr.unshift(dw)}dw=dp;while((dw=dw.parentNode)){du.unshift(dw)}while(dr[dt]===du[dt]){dt++}return dt?b9(dr[dt],du[dt]):dr[dt]===cI?-1:du[dt]===cI?1:0};return dn};cv.matches=function(i,e){return cv(i,null,null,e)};cv.matchesSelector=function(dm,dp){if((dm.ownerDocument||dm)!==cB){cY(dm)}dp=dp.replace(cs,"='$1']");if(dg.matchesSelector&&c7&&(!de||!de.test(dp))&&(!c2||!c2.test(dp))){try{var i=cd.call(dm,dp);if(i||dg.disconnectedMatch||dm.document&&dm.document.nodeType!==11){return i}}catch(dn){}}return cv(dp,cB,null,[dm]).length>0};cv.contains=function(e,i){if((e.ownerDocument||e)!==cB){cY(e)}return cE(e,i)};cv.attr=function(dm,e){if((dm.ownerDocument||dm)!==cB){cY(dm)}var i=cm.attrHandle[e.toLowerCase()],dn=i&&cN.call(cm.attrHandle,e.toLowerCase())?i(dm,e,!c7):undefined;return dn!==undefined?dn:dg.attributes||!c7?dm.getAttribute(e):(dn=dm.getAttributeNode(e))&&dn.specified?dn.value:null};cv.error=function(e){throw new Error("Syntax error, unrecognized expression: "+e)};cv.uniqueSort=function(dn){var dp,dq=[],e=0,dm=0;cW=!dg.detectDuplicates;cH=!dg.sortStable&&dn.slice(0);dn.sort(cD);if(cW){while((dp=dn[dm++])){if(dp===dn[dm]){e=dq.push(dm)}}while(e--){dn.splice(dq[e],1)}}cH=null;return dn};cG=cv.getText=function(dq){var dp,dm="",dn=0,e=dq.nodeType;if(!e){while((dp=dq[dn++])){dm+=cG(dp)}}else{if(e===1||e===9||e===11){if(typeof dq.textContent==="string"){return dq.textContent}else{for(dq=dq.firstChild;dq;dq=dq.nextSibling){dm+=cG(dq)}}}else{if(e===3||e===4){return dq.nodeValue}}}return dm};cm=cv.selectors={cacheLength:50,createPseudo:ci,match:c1,attrHandle:{},find:{},relative:{">":{dir:"parentNode",first:true}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:true},"~":{dir:"previousSibling"}},preFilter:{ATTR:function(e){e[1]=e[1].replace(cr,c4);e[3]=(e[3]||e[4]||e[5]||"").replace(cr,c4);if(e[2]==="~="){e[3]=" "+e[3]+" "}return e.slice(0,4)},CHILD:function(e){e[1]=e[1].toLowerCase();if(e[1].slice(0,3)==="nth"){if(!e[3]){cv.error(e[0])}e[4]=+(e[4]?e[5]+(e[6]||1):2*(e[3]==="even"||e[3]==="odd"));e[5]=+((e[7]+e[8])||e[3]==="odd")}else{if(e[3]){cv.error(e[0])}}return e},PSEUDO:function(i){var e,dm=!i[6]&&i[2];if(c1.CHILD.test(i[0])){return null}if(i[3]){i[2]=i[4]||i[5]||""}else{if(dm&&cR.test(dm)&&(e=ch(dm,true))&&(e=dm.indexOf(")",dm.length-e)-dm.length)){i[0]=i[0].slice(0,e);i[2]=dm.slice(0,e)}}return i.slice(0,3)}},filter:{TAG:function(i){var e=i.replace(cr,c4).toLowerCase();return i==="*"?function(){return true}:function(dm){return dm.nodeName&&dm.nodeName.toLowerCase()===e}},CLASS:function(e){var i=b8[e+" "];return i||(i=new RegExp("(^|"+co+")"+e+"("+co+"|$)"))&&b8(e,function(dm){return i.test(typeof dm.className==="string"&&dm.className||typeof dm.getAttribute!=="undefined"&&dm.getAttribute("class")||"")})},ATTR:function(dm,i,e){return function(dp){var dn=cv.attr(dp,dm);if(dn==null){return i==="!="}if(!i){return true}dn+="";return i==="="?dn===e:i==="!="?dn!==e:i==="^="?e&&dn.indexOf(e)===0:i==="*="?e&&dn.indexOf(e)>-1:i==="$="?e&&dn.slice(-e.length)===e:i==="~="?(" "+dn.replace(ct," ")+" ").indexOf(e)>-1:i==="|="?dn===e||dn.slice(0,e.length+1)===e+"-":false}},CHILD:function(i,dp,dn,dq,dm){var ds=i.slice(0,3)!=="nth",e=i.slice(-4)!=="last",dr=dp==="of-type";return dq===1&&dm===0?function(dt){return !!dt.parentNode}:function(dz,dx,dC){var dt,dF,dA,dE,dB,dw,dy=ds!==e?"nextSibling":"previousSibling",dD=dz.parentNode,dv=dr&&dz.nodeName.toLowerCase(),du=!dC&&!dr;if(dD){if(ds){while(dy){dA=dz;while((dA=dA[dy])){if(dr?dA.nodeName.toLowerCase()===dv:dA.nodeType===1){return false}}dw=dy=i==="only"&&!dw&&"nextSibling"}return true}dw=[e?dD.firstChild:dD.lastChild];if(e&&du){dF=dD[c9]||(dD[c9]={});dt=dF[i]||[];dB=dt[0]===dh&&dt[1];dE=dt[0]===dh&&dt[2];dA=dB&&dD.childNodes[dB];while((dA=++dB&&dA&&dA[dy]||(dE=dB=0)||dw.pop())){if(dA.nodeType===1&&++dE&&dA===dz){dF[i]=[dh,dB,dE];break}}}else{if(du&&(dt=(dz[c9]||(dz[c9]={}))[i])&&dt[0]===dh){dE=dt[1]}else{while((dA=++dB&&dA&&dA[dy]||(dE=dB=0)||dw.pop())){if((dr?dA.nodeName.toLowerCase()===dv:dA.nodeType===1)&&++dE){if(du){(dA[c9]||(dA[c9]={}))[i]=[dh,dE]}if(dA===dz){break}}}}}dE-=dm;return dE===dq||(dE%dq===0&&dE/dq>=0)}}},PSEUDO:function(dn,dm){var e,i=cm.pseudos[dn]||cm.setFilters[dn.toLowerCase()]||cv.error("unsupported pseudo: "+dn);if(i[c9]){return i(dm)}if(i.length>1){e=[dn,dn,"",dm];return cm.setFilters.hasOwnProperty(dn.toLowerCase())?ci(function(dr,dt){var dq,dp=i(dr,dm),ds=dp.length;while(ds--){dq=cc(dr,dp[ds]);dr[dq]=!(dt[dq]=dp[ds])}}):function(dp){return i(dp,0,e)}}return i}},pseudos:{not:ci(function(e){var i=[],dm=[],dn=cV(e.replace(cq,"$1"));return dn[c9]?ci(function(dq,dv,dt,dr){var du,dp=dn(dq,null,dr,[]),ds=dq.length;while(ds--){if((du=dp[ds])){dq[ds]=!(dv[ds]=du)}}}):function(dr,dq,dp){i[0]=dr;dn(i,null,dp,dm);i[0]=null;return !dm.pop()}}),has:ci(function(e){return function(i){return cv(e,i).length>0}}),contains:ci(function(e){e=e.replace(cr,c4);return function(i){return(i.textContent||i.innerText||cG(i)).indexOf(e)>-1}}),lang:ci(function(e){if(!cT.test(e||"")){cv.error("unsupported lang: "+e)}e=e.replace(cr,c4).toLowerCase();return function(dm){var i;do{if((i=c7?dm.lang:dm.getAttribute("xml:lang")||dm.getAttribute("lang"))){i=i.toLowerCase();return i===e||i.indexOf(e+"-")===0}}while((dm=dm.parentNode)&&dm.nodeType===1);return false}}),target:function(e){var i=dd.location&&dd.location.hash;return i&&i.slice(1)===e.id},root:function(e){return e===cn},focus:function(e){return e===cB.activeElement&&(!cB.hasFocus||cB.hasFocus())&&!!(e.type||e.href||~e.tabIndex)},enabled:function(e){return e.disabled===false},disabled:function(e){return e.disabled===true},checked:function(e){var i=e.nodeName.toLowerCase();return(i==="input"&&!!e.checked)||(i==="option"&&!!e.selected)},selected:function(e){if(e.parentNode){e.parentNode.selectedIndex}return e.selected===true},empty:function(e){for(e=e.firstChild;e;e=e.nextSibling){if(e.nodeType<6){return false}}return true},parent:function(e){return !cm.pseudos.empty(e)},header:function(e){return ck.test(e.nodeName)},input:function(e){return cb.test(e.nodeName)},button:function(i){var e=i.nodeName.toLowerCase();return e==="input"&&i.type==="button"||e==="button"},text:function(i){var e;return i.nodeName.toLowerCase()==="input"&&i.type==="text"&&((e=i.getAttribute("type"))==null||e.toLowerCase()==="text")},first:c6(function(){return[0]}),last:c6(function(e,i){return[i-1]}),eq:c6(function(e,dm,i){return[i<0?i+dm:i]}),even:c6(function(e,dn){var dm=0;for(;dm<dn;dm+=2){e.push(dm)}return e}),odd:c6(function(e,dn){var dm=1;for(;dm<dn;dm+=2){e.push(dm)}return e}),lt:c6(function(e,dp,dn){var dm=dn<0?dn+dp:dn;for(;--dm>=0;){e.push(dm)}return e}),gt:c6(function(e,dp,dn){var dm=dn<0?dn+dp:dn;for(;++dm<dp;){e.push(dm)}return e})}};cm.pseudos.nth=cm.pseudos.eq;for(cx in {radio:true,checkbox:true,file:true,password:true,image:true}){cm.pseudos[cx]=cw(cx)}for(cx in {submit:true,reset:true}){cm.pseudos[cx]=ca(cx)}function cU(){}cU.prototype=cm.filters=cm.pseudos;cm.setFilters=new cU();ch=cv.tokenize=function(dp,du){var i,dq,ds,dt,dr,dm,e,dn=c8[dp+" "];if(dn){return du?0:dn.slice(0)}dr=dp;dm=[];e=cm.preFilter;while(dr){if(!i||(dq=cu.exec(dr))){if(dq){dr=dr.slice(dq[0].length)||dr}dm.push((ds=[]))}i=false;if((dq=cA.exec(dr))){i=dq.shift();ds.push({value:i,type:dq[0].replace(cq," ")});dr=dr.slice(i.length)}for(dt in cm.filter){if((dq=c1[dt].exec(dr))&&(!e[dt]||(dq=e[dt](dq)))){i=dq.shift();ds.push({value:i,type:dt,matches:dq});dr=dr.slice(i.length)}}if(!i){break}}return du?dr.length:dr?cv.error(dp):c8(dp,dm).slice(0)};function cg(dp){var dn=0,dm=dp.length,e="";for(;dn<dm;dn++){e+=dp[dn].value}return e}function cp(dp,dm,dn){var e=dm.dir,dq=dn&&e==="parentNode",i=c3++;return dm.first?function(dt,ds,dr){while((dt=dt[e])){if(dt.nodeType===1||dq){return dp(dt,ds,dr)}}}:function(dv,dt,ds){var dw,du,dr=[dh,i];if(ds){while((dv=dv[e])){if(dv.nodeType===1||dq){if(dp(dv,dt,ds)){return true}}}}else{while((dv=dv[e])){if(dv.nodeType===1||dq){du=dv[c9]||(dv[c9]={});if((dw=du[e])&&dw[0]===dh&&dw[1]===i){return(dr[2]=dw[2])}else{du[e]=dr;if((dr[2]=dp(dv,dt,ds))){return true}}}}}}}function dj(e){return e.length>1?function(dq,dp,dm){var dn=e.length;while(dn--){if(!e[dn](dq,dp,dm)){return false}}return true}:e[0]}function cy(dm,dq,dp){var dn=0,e=dq.length;for(;dn<e;dn++){cv(dm,dq[dn],dp)}return dp}function cZ(e,dm,dn,dp,ds){var dq,dv=[],dr=0,dt=e.length,du=dm!=null;for(;dr<dt;dr++){if((dq=e[dr])){if(!dn||dn(dq,dp,ds)){dv.push(dq);if(du){dm.push(dr)}}}}return dv}function cf(dm,i,dp,dn,dq,e){if(dn&&!dn[c9]){dn=cf(dn)}if(dq&&!dq[c9]){dq=cf(dq,e)}return ci(function(dB,dy,dt,dA){var dD,dz,dv,du=[],dC=[],ds=dy.length,dr=dB||cy(i||"*",dt.nodeType?[dt]:dt,[]),dw=dm&&(dB||!i)?cZ(dr,du,dm,dt,dA):dr,dx=dp?dq||(dB?dm:ds||dn)?[]:dy:dw;if(dp){dp(dw,dx,dt,dA)}if(dn){dD=cZ(dx,dC);dn(dD,[],dt,dA);dz=dD.length;while(dz--){if((dv=dD[dz])){dx[dC[dz]]=!(dw[dC[dz]]=dv)}}}if(dB){if(dq||dm){if(dq){dD=[];dz=dx.length;while(dz--){if((dv=dx[dz])){dD.push((dw[dz]=dv))}}dq(null,(dx=[]),dD,dA)}dz=dx.length;while(dz--){if((dv=dx[dz])&&(dD=dq?cc(dB,dv):du[dz])>-1){dB[dD]=!(dy[dD]=dv)}}}}else{dx=cZ(dx===dy?dx.splice(ds,dx.length):dx);if(dq){dq(null,dy,dx,dA)}else{b6.apply(dy,dx)}}})}function da(ds){var dm,dq,dn,dr=ds.length,dv=cm.relative[ds[0].type],dw=dv||cm.relative[" "],dp=dv?1:0,dt=cp(function(i){return i===dm},dw,true),du=cp(function(i){return cc(dm,i)>-1},dw,true),e=[function(dz,dy,dx){var i=(!dv&&(dx||dy!==dl))||((dm=dy).nodeType?dt(dz,dy,dx):du(dz,dy,dx));dm=null;return i}];for(;dp<dr;dp++){if((dq=cm.relative[ds[dp].type])){e=[cp(dj(e),dq)]}else{dq=cm.filter[ds[dp].type].apply(null,ds[dp].matches);if(dq[c9]){dn=++dp;for(;dn<dr;dn++){if(cm.relative[ds[dn].type]){break}}return cf(dp>1&&dj(e),dp>1&&cg(ds.slice(0,dp-1).concat({value:ds[dp-2].type===" "?"*":""})).replace(cq,"$1"),dq,dp<dn&&da(ds.slice(dp,dn)),dn<dr&&da((ds=ds.slice(dn))),dn<dr&&cg(ds))}e.push(dq)}}return dj(e)}function cX(dn,dm){var e=dm.length>0,dp=dn.length>0,i=function(dz,dt,dy,dx,dC){var du,dv,dA,dE=0,dw="0",dq=dz&&[],dF=[],dD=dl,ds=dz||dp&&cm.find.TAG("*",dC),dr=(dh+=dD==null?1:Math.random()||0.1),dB=ds.length;if(dC){dl=dt!==cB&&dt}for(;dw!==dB&&(du=ds[dw])!=null;dw++){if(dp&&du){dv=0;while((dA=dn[dv++])){if(dA(du,dt,dy)){dx.push(du);break}}if(dC){dh=dr}}if(e){if((du=!dA&&du)){dE--}if(dz){dq.push(du)}}}dE+=dw;if(e&&dw!==dE){dv=0;while((dA=dm[dv++])){dA(dq,dF,dt,dy)}if(dz){if(dE>0){while(dw--){if(!(dq[dw]||dF[dw])){dF[dw]=dc.call(dx)}}}dF=cZ(dF)}b6.apply(dx,dF);if(dC&&!dz&&dF.length>0&&(dE+dm.length)>1){cv.uniqueSort(dx)}}if(dC){dh=dr;dl=dD}return dq};return e?ci(i):i}cV=cv.compile=function(e,dn){var dp,dm=[],dr=[],dq=cF[e+" "];if(!dq){if(!dn){dn=ch(e)}dp=dn.length;while(dp--){dq=da(dn[dp]);if(dq[c9]){dm.push(dq)}else{dr.push(dq)}}dq=cF(e,cX(dr,dm));dq.selector=e}return dq};df=cv.select=function(dn,e,dp,ds){var dq,dv,dm,dw,dt,du=typeof dn==="function"&&dn,dr=!ds&&ch((dn=du.selector||dn));dp=dp||[];if(dr.length===1){dv=dr[0]=dr[0].slice(0);if(dv.length>2&&(dm=dv[0]).type==="ID"&&dg.getById&&e.nodeType===9&&c7&&cm.relative[dv[1].type]){e=(cm.find.ID(dm.matches[0].replace(cr,c4),e)||[])[0];if(!e){return dp}else{if(du){e=e.parentNode}}dn=dn.slice(dv.shift().value.length)}dq=c1.needsContext.test(dn)?0:dv.length;while(dq--){dm=dv[dq];if(cm.relative[(dw=dm.type)]){break}if((dt=cm.find[dw])){if((ds=dt(dm.matches[0].replace(cr,c4),c0.test(dv[0].type)&&cS(e.parentNode)||e))){dv.splice(dq,1);dn=ds.length&&cg(dv);if(!dn){b6.apply(dp,ds);return dp}break}}}}(du||cV(dn,dr))(ds,e,!c7,dp,c0.test(dn)&&cS(e.parentNode)||e);return dp};dg.sortStable=c9.split("").sort(cD).join("")===c9;dg.detectDuplicates=!!cW;cY();dg.sortDetached=ce(function(e){return e.compareDocumentPosition(cB.createElement("div"))&1});if(!ce(function(e){e.innerHTML="<a href='#'></a>";return e.firstChild.getAttribute("href")==="#"})){di("type|href|height|width",function(i,e,dm){if(!dm){return i.getAttribute(e,e.toLowerCase()==="type"?1:2)}})}if(!dg.attributes||!ce(function(e){e.innerHTML="<input/>";e.firstChild.setAttribute("value","");return e.firstChild.getAttribute("value")===""})){di("value",function(i,e,dm){if(!dm&&i.nodeName.toLowerCase()==="input"){return i.defaultValue}})}if(!ce(function(e){return e.getAttribute("disabled")==null})){di(b7,function(i,e,dn){var dm;if(!dn){return i[e]===true?e.toLowerCase():(dm=i.getAttributeNode(e))&&dm.specified?dm.value:null}})}return cv})(a4);bH.find=m;bH.expr=m.selectors;bH.expr[":"]=bH.expr.pseudos;bH.unique=m.uniqueSort;bH.text=m.getText;bH.isXMLDoc=m.isXML;bH.contains=m.contains;var z=bH.expr.match.needsContext;var a=(/^<(\w+)\s*\/?>(?:<\/\1>|)$/);var aK=/^.[^:#\[\.,]*$/;function aQ(b5,e,i){if(bH.isFunction(e)){return bH.grep(b5,function(b7,b6){return !!e.call(b7,b6,b7)!==i})}if(e.nodeType){return bH.grep(b5,function(b6){return(b6===e)!==i})}if(typeof e==="string"){if(aK.test(e)){return bH.filter(e,b5,i)}e=bH.filter(e,b5)}return bH.grep(b5,function(b6){return(bH.inArray(b6,e)>=0)!==i})}bH.filter=function(b6,e,b5){var i=e[0];if(b5){b6=":not("+b6+")"}return e.length===1&&i.nodeType===1?bH.find.matchesSelector(i,b6)?[i]:[]:bH.find.matches(b6,bH.grep(e,function(b7){return b7.nodeType===1}))};bH.fn.extend({find:function(b5){var b8,b7=[],b6=this,e=b6.length;if(typeof b5!=="string"){return this.pushStack(bH(b5).filter(function(){for(b8=0;b8<e;b8++){if(bH.contains(b6[b8],this)){return true}}}))}for(b8=0;b8<e;b8++){bH.find(b5,b6[b8],b7)}b7=this.pushStack(e>1?bH.unique(b7):b7);b7.selector=this.selector?this.selector+" "+b5:b5;return b7},filter:function(e){return this.pushStack(aQ(this,e||[],false))},not:function(e){return this.pushStack(aQ(this,e||[],true))},is:function(e){return !!aQ(this,typeof e==="string"&&z.test(e)?bH(e):e||[],false).length}});var y,n=a4.document,bs=/^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,bU=bH.fn.init=function(e,b5){var i,b6;if(!e){return this}if(typeof e==="string"){if(e.charAt(0)==="<"&&e.charAt(e.length-1)===">"&&e.length>=3){i=[null,e,null]}else{i=bs.exec(e)}if(i&&(i[1]||!b5)){if(i[1]){b5=b5 instanceof bH?b5[0]:b5;bH.merge(this,bH.parseHTML(i[1],b5&&b5.nodeType?b5.ownerDocument||b5:n,true));if(a.test(i[1])&&bH.isPlainObject(b5)){for(i in b5){if(bH.isFunction(this[i])){this[i](b5[i])}else{this.attr(i,b5[i])}}}return this}else{b6=n.getElementById(i[2]);if(b6&&b6.parentNode){if(b6.id!==i[2]){return y.find(e)}this.length=1;this[0]=b6}this.context=n;this.selector=e;return this}}else{if(!b5||b5.jquery){return(b5||y).find(e)}else{return this.constructor(b5).find(e)}}}else{if(e.nodeType){this.context=this[0]=e;this.length=1;return this}else{if(bH.isFunction(e)){return typeof y.ready!=="undefined"?y.ready(e):e(bH)}}}if(e.selector!==undefined){this.selector=e.selector;this.context=e.context}return bH.makeArray(e,this)};bU.prototype=bH.fn;y=bH(n);var bu=/^(?:parents|prev(?:Until|All))/,by={children:true,contents:true,next:true,prev:true};bH.extend({dir:function(b5,i,b7){var e=[],b6=b5[i];while(b6&&b6.nodeType!==9&&(b7===undefined||b6.nodeType!==1||!bH(b6).is(b7))){if(b6.nodeType===1){e.push(b6)}b6=b6[i]}return e},sibling:function(b5,i){var e=[];for(;b5;b5=b5.nextSibling){if(b5.nodeType===1&&b5!==i){e.push(b5)}}return e}});bH.fn.extend({has:function(b7){var b6,b5=bH(b7,this),e=b5.length;return this.filter(function(){for(b6=0;b6<e;b6++){if(bH.contains(this,b5[b6])){return true}}})},closest:function(b8,b7){var b9,b6=0,b5=this.length,e=[],ca=z.test(b8)||typeof b8!=="string"?bH(b8,b7||this.context):0;for(;b6<b5;b6++){for(b9=this[b6];b9&&b9!==b7;b9=b9.parentNode){if(b9.nodeType<11&&(ca?ca.index(b9)>-1:b9.nodeType===1&&bH.find.matchesSelector(b9,b8))){e.push(b9);break}}}return this.pushStack(e.length>1?bH.unique(e):e)},index:function(e){if(!e){return(this[0]&&this[0].parentNode)?this.first().prevAll().length:-1}if(typeof e==="string"){return bH.inArray(this[0],bH(e))}return bH.inArray(e.jquery?e[0]:e,this)},add:function(e,i){return this.pushStack(bH.unique(bH.merge(this.get(),bH(e,i))))},addBack:function(e){return this.add(e==null?this.prevObject:this.prevObject.filter(e))}});function aX(i,e){do{i=i[e]}while(i&&i.nodeType!==1);return i}bH.each({parent:function(i){var e=i.parentNode;return e&&e.nodeType!==11?e:null},parents:function(e){return bH.dir(e,"parentNode")},parentsUntil:function(b5,e,b6){return bH.dir(b5,"parentNode",b6)},next:function(e){return aX(e,"nextSibling")},prev:function(e){return aX(e,"previousSibling")},nextAll:function(e){return bH.dir(e,"nextSibling")},prevAll:function(e){return bH.dir(e,"previousSibling")},nextUntil:function(b5,e,b6){return bH.dir(b5,"nextSibling",b6)},prevUntil:function(b5,e,b6){return bH.dir(b5,"previousSibling",b6)},siblings:function(e){return bH.sibling((e.parentNode||{}).firstChild,e)},children:function(e){return bH.sibling(e.firstChild)},contents:function(e){return bH.nodeName(e,"iframe")?e.contentDocument||e.contentWindow.document:bH.merge([],e.childNodes)}},function(e,i){bH.fn[e]=function(b7,b5){var b6=bH.map(this,i,b7);if(e.slice(-5)!=="Until"){b5=b7}if(b5&&typeof b5==="string"){b6=bH.filter(b5,b6)}if(this.length>1){if(!by[e]){b6=bH.unique(b6)}if(bu.test(e)){b6=b6.reverse()}}return this.pushStack(b6)}});var aE=(/\S+/g);var b1={};function ae(i){var e=b1[i]={};bH.each(i.match(aE)||[],function(b6,b5){e[b5]=true});return e}bH.Callbacks=function(cd){cd=typeof cd==="string"?(b1[cd]||ae(cd)):bH.extend({},cd);var b7,b6,e,b8,b9,b5,ca=[],cb=!cd.once&&[],i=function(ce){b6=cd.memory&&ce;e=true;b9=b5||0;b5=0;b8=ca.length;b7=true;for(;ca&&b9<b8;b9++){if(ca[b9].apply(ce[0],ce[1])===false&&cd.stopOnFalse){b6=false;break}}b7=false;if(ca){if(cb){if(cb.length){i(cb.shift())}}else{if(b6){ca=[]}else{cc.disable()}}}},cc={add:function(){if(ca){var cf=ca.length;(function ce(cg){bH.each(cg,function(ci,ch){var cj=bH.type(ch);if(cj==="function"){if(!cd.unique||!cc.has(ch)){ca.push(ch)}}else{if(ch&&ch.length&&cj!=="string"){ce(ch)}}})})(arguments);if(b7){b8=ca.length}else{if(b6){b5=cf;i(b6)}}}return this},remove:function(){if(ca){bH.each(arguments,function(cg,ce){var cf;while((cf=bH.inArray(ce,ca,cf))>-1){ca.splice(cf,1);if(b7){if(cf<=b8){b8--}if(cf<=b9){b9--}}}})}return this},has:function(ce){return ce?bH.inArray(ce,ca)>-1:!!(ca&&ca.length)},empty:function(){ca=[];b8=0;return this},disable:function(){ca=cb=b6=undefined;return this},disabled:function(){return !ca},lock:function(){cb=undefined;if(!b6){cc.disable()}return this},locked:function(){return !cb},fireWith:function(cf,ce){if(ca&&(!e||cb)){ce=ce||[];ce=[cf,ce.slice?ce.slice():ce];if(b7){cb.push(ce)}else{i(ce)}}return this},fire:function(){cc.fireWith(this,arguments);return this},fired:function(){return !!e}};return cc};bH.extend({Deferred:function(b5){var i=[["resolve","done",bH.Callbacks("once memory"),"resolved"],["reject","fail",bH.Callbacks("once memory"),"rejected"],["notify","progress",bH.Callbacks("memory")]],b6="pending",b7={state:function(){return b6},always:function(){e.done(arguments).fail(arguments);return this},then:function(){var b8=arguments;return bH.Deferred(function(b9){bH.each(i,function(cb,ca){var cc=bH.isFunction(b8[cb])&&b8[cb];e[ca[1]](function(){var cd=cc&&cc.apply(this,arguments);if(cd&&bH.isFunction(cd.promise)){cd.promise().done(b9.resolve).fail(b9.reject).progress(b9.notify)}else{b9[ca[0]+"With"](this===b7?b9.promise():this,cc?[cd]:arguments)}})});b8=null}).promise()},promise:function(b8){return b8!=null?bH.extend(b8,b7):b7}},e={};b7.pipe=b7.then;bH.each(i,function(b9,b8){var cb=b8[2],ca=b8[3];b7[b8[1]]=cb.add;if(ca){cb.add(function(){b6=ca},i[b9^1][2].disable,i[2][2].lock)}e[b8[0]]=function(){e[b8[0]+"With"](this===e?b7:this,arguments);return this};e[b8[0]+"With"]=cb.fireWith});b7.promise(e);if(b5){b5.call(e,e)}return e},when:function(b8){var b6=0,ca=O.call(arguments),e=ca.length,b5=e!==1||(b8&&bH.isFunction(b8.promise))?e:0,cd=b5===1?b8:bH.Deferred(),b7=function(cf,cg,ce){return function(i){cg[cf]=this;ce[cf]=arguments.length>1?O.call(arguments):i;if(ce===cc){cd.notifyWith(cg,ce)}else{if(!(--b5)){cd.resolveWith(cg,ce)}}}},cc,b9,cb;if(e>1){cc=new Array(e);b9=new Array(e);cb=new Array(e);for(;b6<e;b6++){if(ca[b6]&&bH.isFunction(ca[b6].promise)){ca[b6].promise().done(b7(b6,cb,ca)).fail(cd.reject).progress(b7(b6,b9,cc))}else{--b5}}}if(!b5){cd.resolveWith(cb,ca)}return cd.promise()}});var aj;bH.fn.ready=function(e){bH.ready.promise().done(e);return this};bH.extend({isReady:false,readyWait:1,holdReady:function(e){if(e){bH.readyWait++}else{bH.ready(true)}},ready:function(e){if(e===true?--bH.readyWait:bH.isReady){return}if(!n.body){return setTimeout(bH.ready)}bH.isReady=true;if(e!==true&&--bH.readyWait>0){return}aj.resolveWith(n,[bH]);if(bH.fn.triggerHandler){bH(n).triggerHandler("ready");bH(n).off("ready")}}});function bl(){if(n.addEventListener){n.removeEventListener("DOMContentLoaded",bY,false);a4.removeEventListener("load",bY,false)}else{n.detachEvent("onreadystatechange",bY);a4.detachEvent("onload",bY)}}function bY(){if(n.addEventListener||event.type==="load"||n.readyState==="complete"){bl();bH.ready()}}bH.ready.promise=function(b7){if(!aj){aj=bH.Deferred();if(n.readyState==="complete"){setTimeout(bH.ready)}else{if(n.addEventListener){n.addEventListener("DOMContentLoaded",bY,false);a4.addEventListener("load",bY,false)}else{n.attachEvent("onreadystatechange",bY);a4.attachEvent("onload",bY);var b6=false;try{b6=a4.frameElement==null&&n.documentElement}catch(b5){}if(b6&&b6.doScroll){(function i(){if(!bH.isReady){try{b6.doScroll("left")}catch(b8){return setTimeout(i,50)}bl();bH.ready()}})()}}}}return aj.promise(b7)};var aB=typeof undefined;var bg;for(bg in bH(C)){break}C.ownLast=bg!=="0";C.inlineBlockNeedsLayout=false;bH(function(){var b5,b6,e,i;e=n.getElementsByTagName("body")[0];if(!e||!e.style){return}b6=n.createElement("div");i=n.createElement("div");i.style.cssText="position:absolute;border:0;width:0;height:0;top:0;left:-9999px";e.appendChild(i).appendChild(b6);if(typeof b6.style.zoom!==aB){b6.style.cssText="display:inline;margin:0;border:0;padding:1px;width:1px;zoom:1";C.inlineBlockNeedsLayout=b5=b6.offsetWidth===3;if(b5){e.style.zoom=1}}e.removeChild(i)});(function(){var b5=n.createElement("div");if(C.deleteExpando==null){C.deleteExpando=true;try{delete b5.test}catch(i){C.deleteExpando=false}}b5=null})();bH.acceptData=function(b5){var i=bH.noData[(b5.nodeName+" ").toLowerCase()],e=+b5.nodeType||1;return e!==1&&e!==9?false:!i||i!==true&&b5.getAttribute("classid")===i};var bx=/^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,aP=/([A-Z])/g;function bz(b6,b5,b7){if(b7===undefined&&b6.nodeType===1){var i="data-"+b5.replace(aP,"-$1").toLowerCase();b7=b6.getAttribute(i);if(typeof b7==="string"){try{b7=b7==="true"?true:b7==="false"?false:b7==="null"?null:+b7+""===b7?+b7:bx.test(b7)?bH.parseJSON(b7):b7}catch(b8){}bH.data(b6,b5,b7)}else{b7=undefined}}return b7}function P(i){var e;for(e in i){if(e==="data"&&bH.isEmptyObject(i[e])){continue}if(e!=="toJSON"){return false}}return true}function bb(b6,i,b8,b7){if(!bH.acceptData(b6)){return}var ca,b9,cb=bH.expando,cc=b6.nodeType,e=cc?bH.cache:b6,b5=cc?b6[cb]:b6[cb]&&cb;if((!b5||!e[b5]||(!b7&&!e[b5].data))&&b8===undefined&&typeof i==="string"){return}if(!b5){if(cc){b5=b6[cb]=aO.pop()||bH.guid++}else{b5=cb}}if(!e[b5]){e[b5]=cc?{}:{toJSON:bH.noop}}if(typeof i==="object"||typeof i==="function"){if(b7){e[b5]=bH.extend(e[b5],i)}else{e[b5].data=bH.extend(e[b5].data,i)}}b9=e[b5];if(!b7){if(!b9.data){b9.data={}}b9=b9.data}if(b8!==undefined){b9[bH.camelCase(i)]=b8}if(typeof i==="string"){ca=b9[i];if(ca==null){ca=b9[bH.camelCase(i)]}}else{ca=b9}return ca}function aa(b8,b6,e){if(!bH.acceptData(b8)){return}var ca,b7,b9=b8.nodeType,b5=b9?bH.cache:b8,cb=b9?b8[bH.expando]:bH.expando;if(!b5[cb]){return}if(b6){ca=e?b5[cb]:b5[cb].data;if(ca){if(!bH.isArray(b6)){if(b6 in ca){b6=[b6]}else{b6=bH.camelCase(b6);if(b6 in ca){b6=[b6]}else{b6=b6.split(" ")}}}else{b6=b6.concat(bH.map(b6,bH.camelCase))}b7=b6.length;while(b7--){delete ca[b6[b7]]}if(e?!P(ca):!bH.isEmptyObject(ca)){return}}}if(!e){delete b5[cb].data;if(!P(b5[cb])){return}}if(b9){bH.cleanData([b8],true)}else{if(C.deleteExpando||b5!=b5.window){delete b5[cb]}else{b5[cb]=null}}}bH.extend({cache:{},noData:{"applet ":true,"embed ":true,"object ":"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"},hasData:function(e){e=e.nodeType?bH.cache[e[bH.expando]]:e[bH.expando];return !!e&&!P(e)},data:function(i,e,b5){return bb(i,e,b5)},removeData:function(i,e){return aa(i,e)},_data:function(i,e,b5){return bb(i,e,b5,true)},_removeData:function(i,e){return aa(i,e,true)}});bH.fn.extend({data:function(b7,ca){var b6,b5,b9,b8=this[0],e=b8&&b8.attributes;if(b7===undefined){if(this.length){b9=bH.data(b8);if(b8.nodeType===1&&!bH._data(b8,"parsedAttrs")){b6=e.length;while(b6--){if(e[b6]){b5=e[b6].name;if(b5.indexOf("data-")===0){b5=bH.camelCase(b5.slice(5));bz(b8,b5,b9[b5])}}}bH._data(b8,"parsedAttrs",true)}}return b9}if(typeof b7==="object"){return this.each(function(){bH.data(this,b7)})}return arguments.length>1?this.each(function(){bH.data(this,b7,ca)}):b8?bz(b8,b7,bH.data(b8,b7)):undefined},removeData:function(e){return this.each(function(){bH.removeData(this,e)})}});bH.extend({queue:function(b5,i,b6){var e;if(b5){i=(i||"fx")+"queue";e=bH._data(b5,i);if(b6){if(!e||bH.isArray(b6)){e=bH._data(b5,i,bH.makeArray(b6))}else{e.push(b6)}}return e||[]}},dequeue:function(b8,b7){b7=b7||"fx";var i=bH.queue(b8,b7),b9=i.length,b6=i.shift(),e=bH._queueHooks(b8,b7),b5=function(){bH.dequeue(b8,b7)};if(b6==="inprogress"){b6=i.shift();b9--}if(b6){if(b7==="fx"){i.unshift("inprogress")}delete e.stop;b6.call(b8,b5,e)}if(!b9&&e){e.empty.fire()}},_queueHooks:function(b5,i){var e=i+"queueHooks";return bH._data(b5,e)||bH._data(b5,e,{empty:bH.Callbacks("once memory").add(function(){bH._removeData(b5,i+"queue");bH._removeData(b5,e)})})}});bH.fn.extend({queue:function(e,i){var b5=2;if(typeof e!=="string"){i=e;e="fx";b5--}if(arguments.length<b5){return bH.queue(this[0],e)}return i===undefined?this:this.each(function(){var b6=bH.queue(this,e,i);bH._queueHooks(this,e);if(e==="fx"&&b6[0]!=="inprogress"){bH.dequeue(this,e)}})},dequeue:function(e){return this.each(function(){bH.dequeue(this,e)})},clearQueue:function(e){return this.queue(e||"fx",[])},promise:function(b6,ca){var b5,b7=1,cb=bH.Deferred(),b9=this,e=this.length,b8=function(){if(!(--b7)){cb.resolveWith(b9,[b9])}};if(typeof b6!=="string"){ca=b6;b6=undefined}b6=b6||"fx";while(e--){b5=bH._data(b9[e],b6+"queueHooks");if(b5&&b5.empty){b7++;b5.empty.add(b8)}}b8();return cb.promise(ca)}});var aD=(/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/).source;var bS=["Top","Right","Bottom","Left"];var R=function(i,e){i=e||i;return bH.css(i,"display")==="none"||!bH.contains(i.ownerDocument,i)};var aA=bH.access=function(e,b9,cb,ca,b7,cd,cc){var b6=0,b5=e.length,b8=cb==null;if(bH.type(cb)==="object"){b7=true;for(b6 in cb){bH.access(e,b9,b6,cb[b6],true,cd,cc)}}else{if(ca!==undefined){b7=true;if(!bH.isFunction(ca)){cc=true}if(b8){if(cc){b9.call(e,ca);b9=null}else{b8=b9;b9=function(ce,i,cf){return b8.call(bH(ce),cf)}}}if(b9){for(;b6<b5;b6++){b9(e[b6],cb,cc?ca:ca.call(e[b6],b6,b9(e[b6],cb)))}}}}return b7?e:b8?b9.call(e):b5?b9(e[0],cb):cd};var aL=(/^(?:checkbox|radio)$/i);(function(){var i=n.createElement("input"),b7=n.createElement("div"),b5=n.createDocumentFragment();b7.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";C.leadingWhitespace=b7.firstChild.nodeType===3;C.tbody=!b7.getElementsByTagName("tbody").length;C.htmlSerialize=!!b7.getElementsByTagName("link").length;C.html5Clone=n.createElement("nav").cloneNode(true).outerHTML!=="<:nav></:nav>";i.type="checkbox";i.checked=true;b5.appendChild(i);C.appendChecked=i.checked;b7.innerHTML="<textarea>x</textarea>";C.noCloneChecked=!!b7.cloneNode(true).lastChild.defaultValue;b5.appendChild(b7);b7.innerHTML="<input type='radio' checked='checked' name='t'/>";C.checkClone=b7.cloneNode(true).cloneNode(true).lastChild.checked;C.noCloneEvent=true;if(b7.attachEvent){b7.attachEvent("onclick",function(){C.noCloneEvent=false});b7.cloneNode(true).click()}if(C.deleteExpando==null){C.deleteExpando=true;try{delete b7.test}catch(b6){C.deleteExpando=false}}})();(function(){var b5,e,b6=n.createElement("div");for(b5 in {submit:true,change:true,focusin:true}){e="on"+b5;if(!(C[b5+"Bubbles"]=e in a4)){b6.setAttribute(e,"t");C[b5+"Bubbles"]=b6.attributes[e].expando===false}}b6=null})();var bF=/^(?:input|select|textarea)$/i,a5=/^key/,bL=/^(?:mouse|pointer|contextmenu)|click/,bB=/^(?:focusinfocus|focusoutblur)$/,bw=/^([^.]*)(?:\.(.+)|)$/;function T(){return true}function Y(){return false}function al(){try{return n.activeElement}catch(e){}}bH.event={global:{},add:function(b7,cc,ch,b9,b8){var ca,ci,cj,b5,ce,cb,cg,b6,cf,e,i,cd=bH._data(b7);if(!cd){return}if(ch.handler){b5=ch;ch=b5.handler;b8=b5.selector}if(!ch.guid){ch.guid=bH.guid++}if(!(ci=cd.events)){ci=cd.events={}}if(!(cb=cd.handle)){cb=cd.handle=function(ck){return typeof bH!==aB&&(!ck||bH.event.triggered!==ck.type)?bH.event.dispatch.apply(cb.elem,arguments):undefined};cb.elem=b7}cc=(cc||"").match(aE)||[""];cj=cc.length;while(cj--){ca=bw.exec(cc[cj])||[];cf=i=ca[1];e=(ca[2]||"").split(".").sort();if(!cf){continue}ce=bH.event.special[cf]||{};cf=(b8?ce.delegateType:ce.bindType)||cf;ce=bH.event.special[cf]||{};cg=bH.extend({type:cf,origType:i,data:b9,handler:ch,guid:ch.guid,selector:b8,needsContext:b8&&bH.expr.match.needsContext.test(b8),namespace:e.join(".")},b5);if(!(b6=ci[cf])){b6=ci[cf]=[];b6.delegateCount=0;if(!ce.setup||ce.setup.call(b7,b9,e,cb)===false){if(b7.addEventListener){b7.addEventListener(cf,cb,false)}else{if(b7.attachEvent){b7.attachEvent("on"+cf,cb)}}}}if(ce.add){ce.add.call(b7,cg);if(!cg.handler.guid){cg.handler.guid=ch.guid}}if(b8){b6.splice(b6.delegateCount++,0,cg)}else{b6.push(cg)}bH.event.global[cf]=true}b7=null},remove:function(b6,cc,cj,b7,cb){var b9,cg,ca,b8,ci,ch,ce,b5,cf,e,i,cd=bH.hasData(b6)&&bH._data(b6);if(!cd||!(ch=cd.events)){return}cc=(cc||"").match(aE)||[""];ci=cc.length;while(ci--){ca=bw.exec(cc[ci])||[];cf=i=ca[1];e=(ca[2]||"").split(".").sort();if(!cf){for(cf in ch){bH.event.remove(b6,cf+cc[ci],cj,b7,true)}continue}ce=bH.event.special[cf]||{};cf=(b7?ce.delegateType:ce.bindType)||cf;b5=ch[cf]||[];ca=ca[2]&&new RegExp("(^|\\.)"+e.join("\\.(?:.*\\.|)")+"(\\.|$)");b8=b9=b5.length;while(b9--){cg=b5[b9];if((cb||i===cg.origType)&&(!cj||cj.guid===cg.guid)&&(!ca||ca.test(cg.namespace))&&(!b7||b7===cg.selector||b7==="**"&&cg.selector)){b5.splice(b9,1);if(cg.selector){b5.delegateCount--}if(ce.remove){ce.remove.call(b6,cg)}}}if(b8&&!b5.length){if(!ce.teardown||ce.teardown.call(b6,e,cd.handle)===false){bH.removeEvent(b6,cf,cd.handle)}delete ch[cf]}}if(bH.isEmptyObject(ch)){delete cd.handle;bH._removeData(b6,"events")}},trigger:function(b5,cc,b8,cj){var cd,b7,ch,ci,cf,cb,ca,b9=[b8||n],cg=J.call(b5,"type")?b5.type:b5,b6=J.call(b5,"namespace")?b5.namespace.split("."):[];ch=cb=b8=b8||n;if(b8.nodeType===3||b8.nodeType===8){return}if(bB.test(cg+bH.event.triggered)){return}if(cg.indexOf(".")>=0){b6=cg.split(".");cg=b6.shift();b6.sort()}b7=cg.indexOf(":")<0&&"on"+cg;b5=b5[bH.expando]?b5:new bH.Event(cg,typeof b5==="object"&&b5);b5.isTrigger=cj?2:3;b5.namespace=b6.join(".");b5.namespace_re=b5.namespace?new RegExp("(^|\\.)"+b6.join("\\.(?:.*\\.|)")+"(\\.|$)"):null;b5.result=undefined;if(!b5.target){b5.target=b8}cc=cc==null?[b5]:bH.makeArray(cc,[b5]);cf=bH.event.special[cg]||{};if(!cj&&cf.trigger&&cf.trigger.apply(b8,cc)===false){return}if(!cj&&!cf.noBubble&&!bH.isWindow(b8)){ci=cf.delegateType||cg;if(!bB.test(ci+cg)){ch=ch.parentNode}for(;ch;ch=ch.parentNode){b9.push(ch);cb=ch}if(cb===(b8.ownerDocument||n)){b9.push(cb.defaultView||cb.parentWindow||a4)}}ca=0;while((ch=b9[ca++])&&!b5.isPropagationStopped()){b5.type=ca>1?ci:cf.bindType||cg;cd=(bH._data(ch,"events")||{})[b5.type]&&bH._data(ch,"handle");if(cd){cd.apply(ch,cc)}cd=b7&&ch[b7];if(cd&&cd.apply&&bH.acceptData(ch)){b5.result=cd.apply(ch,cc);if(b5.result===false){b5.preventDefault()}}}b5.type=cg;if(!cj&&!b5.isDefaultPrevented()){if((!cf._default||cf._default.apply(b9.pop(),cc)===false)&&bH.acceptData(b8)){if(b7&&b8[cg]&&!bH.isWindow(b8)){cb=b8[b7];if(cb){b8[b7]=null}bH.event.triggered=cg;try{b8[cg]()}catch(ce){}bH.event.triggered=undefined;if(cb){b8[b7]=cb}}}}return b5.result},dispatch:function(e){e=bH.event.fix(e);var b8,b9,cd,b5,b7,cc=[],cb=O.call(arguments),b6=(bH._data(this,"events")||{})[e.type]||[],ca=bH.event.special[e.type]||{};cb[0]=e;e.delegateTarget=this;if(ca.preDispatch&&ca.preDispatch.call(this,e)===false){return}cc=bH.event.handlers.call(this,e,b6);b8=0;while((b5=cc[b8++])&&!e.isPropagationStopped()){e.currentTarget=b5.elem;b7=0;while((cd=b5.handlers[b7++])&&!e.isImmediatePropagationStopped()){if(!e.namespace_re||e.namespace_re.test(cd.namespace)){e.handleObj=cd;e.data=cd.data;b9=((bH.event.special[cd.origType]||{}).handle||cd.handler).apply(b5.elem,cb);if(b9!==undefined){if((e.result=b9)===false){e.preventDefault();e.stopPropagation()}}}}}if(ca.postDispatch){ca.postDispatch.call(this,e)}return e.result},handlers:function(e,b6){var b5,cb,b9,b8,ca=[],b7=b6.delegateCount,cc=e.target;if(b7&&cc.nodeType&&(!e.button||e.type!=="click")){for(;cc!=this;cc=cc.parentNode||this){if(cc.nodeType===1&&(cc.disabled!==true||e.type!=="click")){b9=[];for(b8=0;b8<b7;b8++){cb=b6[b8];b5=cb.selector+" ";if(b9[b5]===undefined){b9[b5]=cb.needsContext?bH(b5,this).index(cc)>=0:bH.find(b5,this,null,[cc]).length}if(b9[b5]){b9.push(cb)}}if(b9.length){ca.push({elem:cc,handlers:b9})}}}}if(b7<b6.length){ca.push({elem:this,handlers:b6.slice(b7)})}return ca},fix:function(b7){if(b7[bH.expando]){return b7}var b5,ca,b9,b6=b7.type,e=b7,b8=this.fixHooks[b6];if(!b8){this.fixHooks[b6]=b8=bL.test(b6)?this.mouseHooks:a5.test(b6)?this.keyHooks:{}}b9=b8.props?this.props.concat(b8.props):this.props;b7=new bH.Event(e);b5=b9.length;while(b5--){ca=b9[b5];b7[ca]=e[ca]}if(!b7.target){b7.target=e.srcElement||n}if(b7.target.nodeType===3){b7.target=b7.target.parentNode}b7.metaKey=!!b7.metaKey;return b8.filter?b8.filter(b7,e):b7},props:"altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(i,e){if(i.which==null){i.which=e.charCode!=null?e.charCode:e.keyCode}return i}},mouseHooks:{props:"button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(b6,b5){var e,b7,b8,i=b5.button,b9=b5.fromElement;if(b6.pageX==null&&b5.clientX!=null){b7=b6.target.ownerDocument||n;b8=b7.documentElement;e=b7.body;b6.pageX=b5.clientX+(b8&&b8.scrollLeft||e&&e.scrollLeft||0)-(b8&&b8.clientLeft||e&&e.clientLeft||0);b6.pageY=b5.clientY+(b8&&b8.scrollTop||e&&e.scrollTop||0)-(b8&&b8.clientTop||e&&e.clientTop||0)}if(!b6.relatedTarget&&b9){b6.relatedTarget=b9===b6.target?b5.toElement:b9}if(!b6.which&&i!==undefined){b6.which=(i&1?1:(i&2?3:(i&4?2:0)))}return b6}},special:{load:{noBubble:true},focus:{trigger:function(){if(this!==al()&&this.focus){try{this.focus();return false}catch(i){}}},delegateType:"focusin"},blur:{trigger:function(){if(this===al()&&this.blur){this.blur();return false}},delegateType:"focusout"},click:{trigger:function(){if(bH.nodeName(this,"input")&&this.type==="checkbox"&&this.click){this.click();return false}},_default:function(e){return bH.nodeName(e.target,"a")}},beforeunload:{postDispatch:function(e){if(e.result!==undefined&&e.originalEvent){e.originalEvent.returnValue=e.result}}}},simulate:function(b5,b7,b6,i){var b8=bH.extend(new bH.Event(),b6,{type:b5,isSimulated:true,originalEvent:{}});if(i){bH.event.trigger(b8,null,b7)}else{bH.event.dispatch.call(b7,b8)}if(b8.isDefaultPrevented()){b6.preventDefault()}}};bH.removeEvent=n.removeEventListener?function(i,e,b5){if(i.removeEventListener){i.removeEventListener(e,b5,false)}}:function(b5,i,b6){var e="on"+i;if(b5.detachEvent){if(typeof b5[e]===aB){b5[e]=null}b5.detachEvent(e,b6)}};bH.Event=function(i,e){if(!(this instanceof bH.Event)){return new bH.Event(i,e)}if(i&&i.type){this.originalEvent=i;this.type=i.type;this.isDefaultPrevented=i.defaultPrevented||i.defaultPrevented===undefined&&i.returnValue===false?T:Y}else{this.type=i}if(e){bH.extend(this,e)}this.timeStamp=i&&i.timeStamp||bH.now();this[bH.expando]=true};bH.Event.prototype={isDefaultPrevented:Y,isPropagationStopped:Y,isImmediatePropagationStopped:Y,preventDefault:function(){var i=this.originalEvent;this.isDefaultPrevented=T;if(!i){return}if(i.preventDefault){i.preventDefault()}else{i.returnValue=false}},stopPropagation:function(){var i=this.originalEvent;this.isPropagationStopped=T;if(!i){return}if(i.stopPropagation){i.stopPropagation()}i.cancelBubble=true},stopImmediatePropagation:function(){var i=this.originalEvent;this.isImmediatePropagationStopped=T;if(i&&i.stopImmediatePropagation){i.stopImmediatePropagation()}this.stopPropagation()}};bH.each({mouseenter:"mouseover",mouseleave:"mouseout",pointerenter:"pointerover",pointerleave:"pointerout"},function(i,e){bH.event.special[i]={delegateType:e,bindType:e,handle:function(b7){var b5,b9=this,b8=b7.relatedTarget,b6=b7.handleObj;if(!b8||(b8!==b9&&!bH.contains(b9,b8))){b7.type=b6.origType;b5=b6.handler.apply(this,arguments);b7.type=e}return b5}}});if(!C.submitBubbles){bH.event.special.submit={setup:function(){if(bH.nodeName(this,"form")){return false}bH.event.add(this,"click._submit keypress._submit",function(b6){var b5=b6.target,i=bH.nodeName(b5,"input")||bH.nodeName(b5,"button")?b5.form:undefined;if(i&&!bH._data(i,"submitBubbles")){bH.event.add(i,"submit._submit",function(e){e._submit_bubble=true});bH._data(i,"submitBubbles",true)}})},postDispatch:function(e){if(e._submit_bubble){delete e._submit_bubble;if(this.parentNode&&!e.isTrigger){bH.event.simulate("submit",this.parentNode,e,true)}}},teardown:function(){if(bH.nodeName(this,"form")){return false}bH.event.remove(this,"._submit")}}}if(!C.changeBubbles){bH.event.special.change={setup:function(){if(bF.test(this.nodeName)){if(this.type==="checkbox"||this.type==="radio"){bH.event.add(this,"propertychange._change",function(e){if(e.originalEvent.propertyName==="checked"){this._just_changed=true}});bH.event.add(this,"click._change",function(e){if(this._just_changed&&!e.isTrigger){this._just_changed=false}bH.event.simulate("change",this,e,true)})}return false}bH.event.add(this,"beforeactivate._change",function(b5){var i=b5.target;if(bF.test(i.nodeName)&&!bH._data(i,"changeBubbles")){bH.event.add(i,"change._change",function(e){if(this.parentNode&&!e.isSimulated&&!e.isTrigger){bH.event.simulate("change",this.parentNode,e,true)}});bH._data(i,"changeBubbles",true)}})},handle:function(i){var e=i.target;if(this!==e||i.isSimulated||i.isTrigger||(e.type!=="radio"&&e.type!=="checkbox")){return i.handleObj.handler.apply(this,arguments)}},teardown:function(){bH.event.remove(this,"._change");return !bF.test(this.nodeName)}}}if(!C.focusinBubbles){bH.each({focus:"focusin",blur:"focusout"},function(b5,e){var i=function(b6){bH.event.simulate(e,b6.target,bH.event.fix(b6),true)};bH.event.special[e]={setup:function(){var b7=this.ownerDocument||this,b6=bH._data(b7,e);if(!b6){b7.addEventListener(b5,i,true)}bH._data(b7,e,(b6||0)+1)},teardown:function(){var b7=this.ownerDocument||this,b6=bH._data(b7,e)-1;if(!b6){b7.removeEventListener(b5,i,true);bH._removeData(b7,e)}else{bH._data(b7,e,b6)}}}})}bH.fn.extend({on:function(b5,e,b8,b7,i){var b6,b9;if(typeof b5==="object"){if(typeof e!=="string"){b8=b8||e;e=undefined}for(b6 in b5){this.on(b6,e,b8,b5[b6],i)}return this}if(b8==null&&b7==null){b7=e;b8=e=undefined}else{if(b7==null){if(typeof e==="string"){b7=b8;b8=undefined}else{b7=b8;b8=e;e=undefined}}}if(b7===false){b7=Y}else{if(!b7){return this}}if(i===1){b9=b7;b7=function(ca){bH().off(ca);return b9.apply(this,arguments)};b7.guid=b9.guid||(b9.guid=bH.guid++)}return this.each(function(){bH.event.add(this,b5,b7,b8,e)})},one:function(i,e,b6,b5){return this.on(i,e,b6,b5,1)},off:function(b5,e,b7){var i,b6;if(b5&&b5.preventDefault&&b5.handleObj){i=b5.handleObj;bH(b5.delegateTarget).off(i.namespace?i.origType+"."+i.namespace:i.origType,i.selector,i.handler);return this}if(typeof b5==="object"){for(b6 in b5){this.off(b6,e,b5[b6])}return this}if(e===false||typeof e==="function"){b7=e;e=undefined}if(b7===false){b7=Y}return this.each(function(){bH.event.remove(this,b5,b7,e)})},trigger:function(e,i){return this.each(function(){bH.event.trigger(e,i,this)})},triggerHandler:function(e,b5){var i=this[0];if(i){return bH.event.trigger(e,b5,i,true)}}});function A(e){var b5=d.split("|"),i=e.createDocumentFragment();if(i.createElement){while(b5.length){i.createElement(b5.pop())}}return i}var d="abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",aC=/ jQuery\d+="(?:null|\d+)"/g,L=new RegExp("<(?:"+d+")[\\s/>]","i"),b4=/^\s+/,aG=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,o=/<([\w:]+)/,bZ=/<tbody/i,K=/<|&#?\w+;/,am=/<(?:script|style|link)/i,bV=/checked\s*(?:[^=]|=\s*.checked.)/i,bA=/^$|\/(?:java|ecma)script/i,aq=/^true\/(.*)/,aN=/^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,V={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],area:[1,"<map>","</map>"],param:[1,"<object>","</object>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],_default:C.htmlSerialize?[0,"",""]:[1,"X<div>","</div>"]},aS=A(n),k=aS.appendChild(n.createElement("div"));V.optgroup=V.option;V.tbody=V.tfoot=V.colgroup=V.caption=V.thead;V.th=V.td;function l(b7,e){var b5,b8,b6=0,b9=typeof b7.getElementsByTagName!==aB?b7.getElementsByTagName(e||"*"):typeof b7.querySelectorAll!==aB?b7.querySelectorAll(e||"*"):undefined;if(!b9){for(b9=[],b5=b7.childNodes||b7;(b8=b5[b6])!=null;b6++){if(!e||bH.nodeName(b8,e)){b9.push(b8)}else{bH.merge(b9,l(b8,e))}}}return e===undefined||e&&bH.nodeName(b7,e)?bH.merge([b7],b9):b9}function bX(e){if(aL.test(e.type)){e.defaultChecked=e.checked}}function a2(i,e){return bH.nodeName(i,"table")&&bH.nodeName(e.nodeType!==11?e:e.firstChild,"tr")?i.getElementsByTagName("tbody")[0]||i.appendChild(i.ownerDocument.createElement("tbody")):i}function t(e){e.type=(bH.find.attr(e,"type")!==null)+"/"+e.type;return e}function be(i){var e=aq.exec(i.type);if(e){i.type=e[1]}else{i.removeAttribute("type")}return i}function bt(e,b6){var b7,b5=0;for(;(b7=e[b5])!=null;b5++){bH._data(b7,"globalEval",!b6||bH._data(b6[b5],"globalEval"))}}function ar(cb,b5){if(b5.nodeType!==1||!bH.hasData(cb)){return}var b8,b7,e,ca=bH._data(cb),b9=bH._data(b5,ca),b6=ca.events;if(b6){delete b9.handle;b9.events={};for(b8 in b6){for(b7=0,e=b6[b8].length;b7<e;b7++){bH.event.add(b5,b8,b6[b8][b7])}}}if(b9.data){b9.data=bH.extend({},b9.data)}}function S(b7,i){var b8,b6,b5;if(i.nodeType!==1){return}b8=i.nodeName.toLowerCase();if(!C.noCloneEvent&&i[bH.expando]){b5=bH._data(i);for(b6 in b5.events){bH.removeEvent(i,b6,b5.handle)}i.removeAttribute(bH.expando)}if(b8==="script"&&i.text!==b7.text){t(i).text=b7.text;be(i)}else{if(b8==="object"){if(i.parentNode){i.outerHTML=b7.outerHTML}if(C.html5Clone&&(b7.innerHTML&&!bH.trim(i.innerHTML))){i.innerHTML=b7.innerHTML}}else{if(b8==="input"&&aL.test(b7.type)){i.defaultChecked=i.checked=b7.checked;if(i.value!==b7.value){i.value=b7.value}}else{if(b8==="option"){i.defaultSelected=i.selected=b7.defaultSelected}else{if(b8==="input"||b8==="textarea"){i.defaultValue=b7.defaultValue}}}}}}bH.extend({clone:function(b5,b7,e){var b9,b6,cc,b8,ca,cb=bH.contains(b5.ownerDocument,b5);if(C.html5Clone||bH.isXMLDoc(b5)||!L.test("<"+b5.nodeName+">")){cc=b5.cloneNode(true)}else{k.innerHTML=b5.outerHTML;k.removeChild(cc=k.firstChild)}if((!C.noCloneEvent||!C.noCloneChecked)&&(b5.nodeType===1||b5.nodeType===11)&&!bH.isXMLDoc(b5)){b9=l(cc);ca=l(b5);for(b8=0;(b6=ca[b8])!=null;++b8){if(b9[b8]){S(b6,b9[b8])}}}if(b7){if(e){ca=ca||l(b5);b9=b9||l(cc);for(b8=0;(b6=ca[b8])!=null;b8++){ar(b6,b9[b8])}}else{ar(b5,cc)}}b9=l(cc,"script");if(b9.length>0){bt(b9,!cb&&l(b5,"script"))}b9=ca=b6=null;return cc},buildFragment:function(b5,b7,cc,ch){var cd,b9,cb,cg,ci,cf,b6,ca=b5.length,b8=A(b7),e=[],ce=0;for(;ce<ca;ce++){b9=b5[ce];if(b9||b9===0){if(bH.type(b9)==="object"){bH.merge(e,b9.nodeType?[b9]:b9)}else{if(!K.test(b9)){e.push(b7.createTextNode(b9))}else{cg=cg||b8.appendChild(b7.createElement("div"));ci=(o.exec(b9)||["",""])[1].toLowerCase();b6=V[ci]||V._default;cg.innerHTML=b6[1]+b9.replace(aG,"<$1></$2>")+b6[2];cd=b6[0];while(cd--){cg=cg.lastChild}if(!C.leadingWhitespace&&b4.test(b9)){e.push(b7.createTextNode(b4.exec(b9)[0]))}if(!C.tbody){b9=ci==="table"&&!bZ.test(b9)?cg.firstChild:b6[1]==="<table>"&&!bZ.test(b9)?cg:0;cd=b9&&b9.childNodes.length;while(cd--){if(bH.nodeName((cf=b9.childNodes[cd]),"tbody")&&!cf.childNodes.length){b9.removeChild(cf)}}}bH.merge(e,cg.childNodes);cg.textContent="";while(cg.firstChild){cg.removeChild(cg.firstChild)}cg=b8.lastChild}}}}if(cg){b8.removeChild(cg)}if(!C.appendChecked){bH.grep(l(e,"input"),bX)}ce=0;while((b9=e[ce++])){if(ch&&bH.inArray(b9,ch)!==-1){continue}cb=bH.contains(b9.ownerDocument,b9);cg=l(b8.appendChild(b9),"script");if(cb){bt(cg)}if(cc){cd=0;while((b9=cg[cd++])){if(bA.test(b9.type||"")){cc.push(b9)}}}}cg=null;return b8},cleanData:function(b5,cd){var b7,cc,b6,b8,b9=0,ce=bH.expando,e=bH.cache,ca=C.deleteExpando,cb=bH.event.special;for(;(b7=b5[b9])!=null;b9++){if(cd||bH.acceptData(b7)){b6=b7[ce];b8=b6&&e[b6];if(b8){if(b8.events){for(cc in b8.events){if(cb[cc]){bH.event.remove(b7,cc)}else{bH.removeEvent(b7,cc,b8.handle)}}}if(e[b6]){delete e[b6];if(ca){delete b7[ce]}else{if(typeof b7.removeAttribute!==aB){b7.removeAttribute(ce)}else{b7[ce]=null}}aO.push(b6)}}}}}});bH.fn.extend({text:function(e){return aA(this,function(i){return i===undefined?bH.text(this):this.empty().append((this[0]&&this[0].ownerDocument||n).createTextNode(i))},null,e,arguments.length)},append:function(){return this.domManip(arguments,function(e){if(this.nodeType===1||this.nodeType===11||this.nodeType===9){var i=a2(this,e);i.appendChild(e)}})},prepend:function(){return this.domManip(arguments,function(e){if(this.nodeType===1||this.nodeType===11||this.nodeType===9){var i=a2(this,e);i.insertBefore(e,i.firstChild)}})},before:function(){return this.domManip(arguments,function(e){if(this.parentNode){this.parentNode.insertBefore(e,this)}})},after:function(){return this.domManip(arguments,function(e){if(this.parentNode){this.parentNode.insertBefore(e,this.nextSibling)}})},remove:function(e,b8){var b7,b5=e?bH.filter(e,this):this,b6=0;for(;(b7=b5[b6])!=null;b6++){if(!b8&&b7.nodeType===1){bH.cleanData(l(b7))}if(b7.parentNode){if(b8&&bH.contains(b7.ownerDocument,b7)){bt(l(b7,"script"))}b7.parentNode.removeChild(b7)}}return this},empty:function(){var b5,e=0;for(;(b5=this[e])!=null;e++){if(b5.nodeType===1){bH.cleanData(l(b5,false))}while(b5.firstChild){b5.removeChild(b5.firstChild)}if(b5.options&&bH.nodeName(b5,"select")){b5.options.length=0}}return this},clone:function(i,e){i=i==null?false:i;e=e==null?i:e;return this.map(function(){return bH.clone(this,i,e)})},html:function(e){return aA(this,function(b8){var b7=this[0]||{},b6=0,b5=this.length;if(b8===undefined){return b7.nodeType===1?b7.innerHTML.replace(aC,""):undefined}if(typeof b8==="string"&&!am.test(b8)&&(C.htmlSerialize||!L.test(b8))&&(C.leadingWhitespace||!b4.test(b8))&&!V[(o.exec(b8)||["",""])[1].toLowerCase()]){b8=b8.replace(aG,"<$1></$2>");try{for(;b6<b5;b6++){b7=this[b6]||{};if(b7.nodeType===1){bH.cleanData(l(b7,false));b7.innerHTML=b8}}b7=0}catch(b9){}}if(b7){this.empty().append(b8)}},null,e,arguments.length)},replaceWith:function(){var e=arguments[0];this.domManip(arguments,function(i){e=this.parentNode;bH.cleanData(l(this));if(e){e.replaceChild(i,this)}});return e&&(e.length||e.nodeType)?this:this.remove()},detach:function(e){return this.remove(e,true)},domManip:function(cc,ch){cc=ay.apply([],cc);var ca,b6,e,b8,cf,cb,b9=0,b7=this.length,ce=this,cg=b7-1,cd=cc[0],b5=bH.isFunction(cd);if(b5||(b7>1&&typeof cd==="string"&&!C.checkClone&&bV.test(cd))){return this.each(function(ci){var i=ce.eq(ci);if(b5){cc[0]=cd.call(this,ci,i.html())}i.domManip(cc,ch)})}if(b7){cb=bH.buildFragment(cc,this[0].ownerDocument,false,this);ca=cb.firstChild;if(cb.childNodes.length===1){cb=ca}if(ca){b8=bH.map(l(cb,"script"),t);e=b8.length;for(;b9<b7;b9++){b6=cb;if(b9!==cg){b6=bH.clone(b6,true,true);if(e){bH.merge(b8,l(b6,"script"))}}ch.call(this[b9],b6,b9)}if(e){cf=b8[b8.length-1].ownerDocument;bH.map(b8,be);for(b9=0;b9<e;b9++){b6=b8[b9];if(bA.test(b6.type||"")&&!bH._data(b6,"globalEval")&&bH.contains(cf,b6)){if(b6.src){if(bH._evalUrl){bH._evalUrl(b6.src)}}else{bH.globalEval((b6.text||b6.textContent||b6.innerHTML||"").replace(aN,""))}}}}cb=ca=null}}return this}});bH.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(e,i){bH.fn[e]=function(b5){var b6,b8=0,b7=[],ca=bH(b5),b9=ca.length-1;for(;b8<=b9;b8++){b6=b8===b9?this:this.clone(true);bH(ca[b8])[i](b6);w.apply(b7,b6.get())}return this.pushStack(b7)}});var aH,bk={};function a3(e,b7){var i,b5=bH(b7.createElement(e)).appendTo(b7.body),b6=a4.getDefaultComputedStyle&&(i=a4.getDefaultComputedStyle(b5[0]))?i.display:bH.css(b5[0],"display");b5.detach();return b6}function aZ(b5){var i=n,e=bk[b5];if(!e){e=a3(b5,i);if(e==="none"||!e){aH=(aH||bH("<iframe frameborder='0' width='0' height='0'/>")).appendTo(i.documentElement);i=(aH[0].contentWindow||aH[0].contentDocument).document;i.write();i.close();e=a3(b5,i);aH.detach()}bk[b5]=e}return e}(function(){var e;C.shrinkWrapBlocks=function(){if(e!=null){return e}e=false;var b6,i,b5;i=n.getElementsByTagName("body")[0];if(!i||!i.style){return}b6=n.createElement("div");b5=n.createElement("div");b5.style.cssText="position:absolute;border:0;width:0;height:0;top:0;left:-9999px";i.appendChild(b5).appendChild(b6);if(typeof b6.style.zoom!==aB){b6.style.cssText="-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:1px;width:1px;zoom:1";b6.appendChild(n.createElement("div")).style.width="5px";e=b6.offsetWidth!==3}i.removeChild(b5);return e}})();var aY=(/^margin/);var X=new RegExp("^("+aD+")(?!px)[a-z%]+$","i");var bp,F,bn=/^(top|right|bottom|left)$/;if(a4.getComputedStyle){bp=function(e){if(e.ownerDocument.defaultView.opener){return e.ownerDocument.defaultView.getComputedStyle(e,null)}return a4.getComputedStyle(e,null)};F=function(ca,i,b9){var b7,b6,b8,e,b5=ca.style;b9=b9||bp(ca);e=b9?b9.getPropertyValue(i)||b9[i]:undefined;if(b9){if(e===""&&!bH.contains(ca.ownerDocument,ca)){e=bH.style(ca,i)}if(X.test(e)&&aY.test(i)){b7=b5.width;b6=b5.minWidth;b8=b5.maxWidth;b5.minWidth=b5.maxWidth=b5.width=e;e=b9.width;b5.width=b7;b5.minWidth=b6;b5.maxWidth=b8}}return e===undefined?e:e+""}}else{if(n.documentElement.currentStyle){bp=function(e){return e.currentStyle};F=function(b9,b6,b8){var ca,i,e,b5,b7=b9.style;b8=b8||bp(b9);b5=b8?b8[b6]:undefined;if(b5==null&&b7&&b7[b6]){b5=b7[b6]}if(X.test(b5)&&!bn.test(b6)){ca=b7.left;i=b9.runtimeStyle;e=i&&i.left;if(e){i.left=b9.currentStyle.left}b7.left=b6==="fontSize"?"1em":b5;b5=b7.pixelLeft+"px";b7.left=ca;if(e){i.left=e}}return b5===undefined?b5:b5+""||"auto"}}}function a6(e,i){return{get:function(){var b5=e();if(b5==null){return}if(b5){delete this.get;return}return(this.get=i).apply(this,arguments)}}}(function(){var ca,b8,b6,b9,b5,b7,i;ca=n.createElement("div");ca.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";b6=ca.getElementsByTagName("a")[0];b8=b6&&b6.style;if(!b8){return}b8.cssText="float:left;opacity:.5";C.opacity=b8.opacity==="0.5";C.cssFloat=!!b8.cssFloat;ca.style.backgroundClip="content-box";ca.cloneNode(true).style.backgroundClip="";C.clearCloneStyle=ca.style.backgroundClip==="content-box";C.boxSizing=b8.boxSizing===""||b8.MozBoxSizing===""||b8.WebkitBoxSizing==="";bH.extend(C,{reliableHiddenOffsets:function(){if(b7==null){e()}return b7},boxSizingReliable:function(){if(b5==null){e()}return b5},pixelPosition:function(){if(b9==null){e()}return b9},reliableMarginRight:function(){if(i==null){e()}return i}});function e(){var ce,cb,cc,cd;cb=n.getElementsByTagName("body")[0];if(!cb||!cb.style){return}ce=n.createElement("div");cc=n.createElement("div");cc.style.cssText="position:absolute;border:0;width:0;height:0;top:0;left:-9999px";cb.appendChild(cc).appendChild(ce);ce.style.cssText="-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;margin-top:1%;top:1%;border:1px;padding:1px;width:4px;position:absolute";b9=b5=false;i=true;if(a4.getComputedStyle){b9=(a4.getComputedStyle(ce,null)||{}).top!=="1%";b5=(a4.getComputedStyle(ce,null)||{width:"4px"}).width==="4px";cd=ce.appendChild(n.createElement("div"));cd.style.cssText=ce.style.cssText="-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0";cd.style.marginRight=cd.style.width="0";ce.style.width="1px";i=!parseFloat((a4.getComputedStyle(cd,null)||{}).marginRight);ce.removeChild(cd)}ce.innerHTML="<table><tr><td></td><td>t</td></tr></table>";cd=ce.getElementsByTagName("td");cd[0].style.cssText="margin:0;border:0;padding:0;display:none";b7=cd[0].offsetHeight===0;if(b7){cd[0].style.display="";cd[1].style.display="none";b7=cd[0].offsetHeight===0}cb.removeChild(cc)}})();bH.swap=function(b8,b7,b9,b6){var b5,i,e={};for(i in b7){e[i]=b8.style[i];b8.style[i]=b7[i]}b5=b9.apply(b8,b6||[]);for(i in b7){b8.style[i]=e[i]}return b5};var bi=/alpha\([^)]*\)/i,aT=/opacity\s*=\s*([^)]*)/,G=/^(none|table(?!-c[ea]).+)/,ba=new RegExp("^("+aD+")(.*)$","i"),U=new RegExp("^([+-])=("+aD+")","i"),bd={position:"absolute",visibility:"hidden",display:"block"},bC={letterSpacing:"0",fontWeight:"400"},av=["Webkit","O","Moz","ms"];function c(b7,b5){if(b5 in b7){return b5}var b8=b5.charAt(0).toUpperCase()+b5.slice(1),e=b5,b6=av.length;while(b6--){b5=av[b6]+b8;if(b5 in b7){return b5}}return e}function r(b9,e){var ca,b7,b8,i=[],b5=0,b6=b9.length;for(;b5<b6;b5++){b7=b9[b5];if(!b7.style){continue}i[b5]=bH._data(b7,"olddisplay");ca=b7.style.display;if(e){if(!i[b5]&&ca==="none"){b7.style.display=""}if(b7.style.display===""&&R(b7)){i[b5]=bH._data(b7,"olddisplay",aZ(b7.nodeName))}}else{b8=R(b7);if(ca&&ca!=="none"||!b8){bH._data(b7,"olddisplay",b8?ca:bH.css(b7,"display"))}}}for(b5=0;b5<b6;b5++){b7=b9[b5];if(!b7.style){continue}if(!e||b7.style.display==="none"||b7.style.display===""){b7.style.display=e?i[b5]||"":"none"}}return b9}function aM(e,b5,b6){var i=ba.exec(b5);return i?Math.max(0,i[1]-(b6||0))+(i[2]||"px"):b5}function aw(b8,b5,e,ca,b7){var b6=e===(ca?"border":"content")?4:b5==="width"?1:0,b9=0;for(;b6<4;b6+=2){if(e==="margin"){b9+=bH.css(b8,e+bS[b6],true,b7)}if(ca){if(e==="content"){b9-=bH.css(b8,"padding"+bS[b6],true,b7)}if(e!=="margin"){b9-=bH.css(b8,"border"+bS[b6]+"Width",true,b7)}}else{b9+=bH.css(b8,"padding"+bS[b6],true,b7);if(e!=="padding"){b9+=bH.css(b8,"border"+bS[b6]+"Width",true,b7)}}}return b9}function u(b7,i,e){var b6=true,b8=i==="width"?b7.offsetWidth:b7.offsetHeight,b5=bp(b7),b9=C.boxSizing&&bH.css(b7,"boxSizing",false,b5)==="border-box";if(b8<=0||b8==null){b8=F(b7,i,b5);if(b8<0||b8==null){b8=b7.style[i]}if(X.test(b8)){return b8}b6=b9&&(C.boxSizingReliable()||b8===b7.style[i]);b8=parseFloat(b8)||0}return(b8+aw(b7,i,e||(b9?"border":"content"),b6,b5))+"px"}bH.extend({cssHooks:{opacity:{get:function(b5,i){if(i){var e=F(b5,"opacity");return e===""?"1":e}}}},cssNumber:{columnCount:true,fillOpacity:true,flexGrow:true,flexShrink:true,fontWeight:true,lineHeight:true,opacity:true,order:true,orphans:true,widows:true,zIndex:true,zoom:true},cssProps:{"float":C.cssFloat?"cssFloat":"styleFloat"},style:function(b6,b5,cc,b7){if(!b6||b6.nodeType===3||b6.nodeType===8||!b6.style){return}var ca,cb,cd,b8=bH.camelCase(b5),i=b6.style;b5=bH.cssProps[b8]||(bH.cssProps[b8]=c(i,b8));cd=bH.cssHooks[b5]||bH.cssHooks[b8];if(cc!==undefined){cb=typeof cc;if(cb==="string"&&(ca=U.exec(cc))){cc=(ca[1]+1)*ca[2]+parseFloat(bH.css(b6,b5));cb="number"}if(cc==null||cc!==cc){return}if(cb==="number"&&!bH.cssNumber[b8]){cc+="px"}if(!C.clearCloneStyle&&cc===""&&b5.indexOf("background")===0){i[b5]="inherit"}if(!cd||!("set" in cd)||(cc=cd.set(b6,cc,b7))!==undefined){try{i[b5]=cc}catch(b9){}}}else{if(cd&&"get" in cd&&(ca=cd.get(b6,false,b7))!==undefined){return ca}return i[b5]}},css:function(b9,b7,i,b8){var b6,ca,e,b5=bH.camelCase(b7);b7=bH.cssProps[b5]||(bH.cssProps[b5]=c(b9.style,b5));e=bH.cssHooks[b7]||bH.cssHooks[b5];if(e&&"get" in e){ca=e.get(b9,true,i)}if(ca===undefined){ca=F(b9,b7,b8)}if(ca==="normal"&&b7 in bC){ca=bC[b7]}if(i===""||i){b6=parseFloat(ca);return i===true||bH.isNumeric(b6)?b6||0:ca}return ca}});bH.each(["height","width"],function(b5,e){bH.cssHooks[e]={get:function(b7,b6,i){if(b6){return G.test(bH.css(b7,"display"))&&b7.offsetWidth===0?bH.swap(b7,bd,function(){return u(b7,e,i)}):u(b7,e,i)}},set:function(b7,b8,i){var b6=i&&bp(b7);return aM(b7,b8,i?aw(b7,e,i,C.boxSizing&&bH.css(b7,"boxSizing",false,b6)==="border-box",b6):0)}}});if(!C.opacity){bH.cssHooks.opacity={get:function(i,e){return aT.test((e&&i.currentStyle?i.currentStyle.filter:i.style.filter)||"")?(0.01*parseFloat(RegExp.$1))+"":e?"1":""},set:function(b7,b8){var b6=b7.style,i=b7.currentStyle,e=bH.isNumeric(b8)?"alpha(opacity="+b8*100+")":"",b5=i&&i.filter||b6.filter||"";b6.zoom=1;if((b8>=1||b8==="")&&bH.trim(b5.replace(bi,""))===""&&b6.removeAttribute){b6.removeAttribute("filter");if(b8===""||i&&!i.filter){return}}b6.filter=bi.test(b5)?b5.replace(bi,e):b5+" "+e}}}bH.cssHooks.marginRight=a6(C.reliableMarginRight,function(i,e){if(e){return bH.swap(i,{display:"inline-block"},F,[i,"marginRight"])}});bH.each({margin:"",padding:"",border:"Width"},function(e,i){bH.cssHooks[e+i]={expand:function(b7){var b6=0,b5={},b8=typeof b7==="string"?b7.split(" "):[b7];for(;b6<4;b6++){b5[e+bS[b6]+i]=b8[b6]||b8[b6-2]||b8[0]}return b5}};if(!aY.test(e)){bH.cssHooks[e+i].set=aM}});bH.fn.extend({css:function(e,i){return aA(this,function(b9,b6,ca){var b8,b5,cb={},b7=0;if(bH.isArray(b6)){b8=bp(b9);b5=b6.length;for(;b7<b5;b7++){cb[b6[b7]]=bH.css(b9,b6[b7],false,b8)}return cb}return ca!==undefined?bH.style(b9,b6,ca):bH.css(b9,b6)},e,i,arguments.length>1)},show:function(){return r(this,true)},hide:function(){return r(this)},toggle:function(e){if(typeof e==="boolean"){return e?this.show():this.hide()}return this.each(function(){if(R(this)){bH(this).show()}else{bH(this).hide()}})}});function I(b5,i,b7,e,b6){return new I.prototype.init(b5,i,b7,e,b6)}bH.Tween=I;I.prototype={constructor:I,init:function(b6,i,b8,e,b7,b5){this.elem=b6;this.prop=b8;this.easing=b7||"swing";this.options=i;this.start=this.now=this.cur();this.end=e;this.unit=b5||(bH.cssNumber[b8]?"":"px")},cur:function(){var e=I.propHooks[this.prop];return e&&e.get?e.get(this):I.propHooks._default.get(this)},run:function(b5){var i,e=I.propHooks[this.prop];if(this.options.duration){this.pos=i=bH.easing[this.easing](b5,this.options.duration*b5,0,1,this.options.duration)}else{this.pos=i=b5}this.now=(this.end-this.start)*i+this.start;if(this.options.step){this.options.step.call(this.elem,this.now,this)}if(e&&e.set){e.set(this)}else{I.propHooks._default.set(this)}return this}};I.prototype.init.prototype=I.prototype;I.propHooks={_default:{get:function(i){var e;if(i.elem[i.prop]!=null&&(!i.elem.style||i.elem.style[i.prop]==null)){return i.elem[i.prop]}e=bH.css(i.elem,i.prop,"");return !e||e==="auto"?0:e},set:function(e){if(bH.fx.step[e.prop]){bH.fx.step[e.prop](e)}else{if(e.elem.style&&(e.elem.style[bH.cssProps[e.prop]]!=null||bH.cssHooks[e.prop])){bH.style(e.elem,e.prop,e.now+e.unit)}else{e.elem[e.prop]=e.now}}}}};I.propHooks.scrollTop=I.propHooks.scrollLeft={set:function(e){if(e.elem.nodeType&&e.elem.parentNode){e.elem[e.prop]=e.now}}};bH.easing={linear:function(e){return e},swing:function(e){return 0.5-Math.cos(e*Math.PI)/2}};bH.fx=I.prototype.init;bH.fx.step={};var M,ad,bQ=/^(?:toggle|show|hide)$/,bI=new RegExp("^(?:([+-])=|)("+aD+")([a-z%]*)$","i"),bO=/queueHooks$/,aF=[h],a1={"*":[function(e,b9){var cb=this.createTween(e,b9),b7=cb.cur(),b6=bI.exec(b9),ca=b6&&b6[3]||(bH.cssNumber[e]?"":"px"),i=(bH.cssNumber[e]||ca!=="px"&&+b7)&&bI.exec(bH.css(cb.elem,e)),b5=1,b8=20;if(i&&i[3]!==ca){ca=ca||i[3];b6=b6||[];i=+b7||1;do{b5=b5||".5";i=i/b5;bH.style(cb.elem,e,i+ca)}while(b5!==(b5=cb.cur()/b7)&&b5!==1&&--b8)}if(b6){i=cb.start=+i||+b7||0;cb.unit=ca;cb.end=b6[1]?i+(b6[1]+1)*b6[2]:+b6[2]}return cb}]};function bm(){setTimeout(function(){M=undefined});return(M=bH.now())}function bG(b6,b8){var b7,e={height:b6},b5=0;b8=b8?1:0;for(;b5<4;b5+=2-b8){b7=bS[b5];e["margin"+b7]=e["padding"+b7]=b6}if(b8){e.opacity=e.width=b6}return e}function bc(b7,b9,b6){var i,b8=(a1[b9]||[]).concat(a1["*"]),e=0,b5=b8.length;for(;e<b5;e++){if((i=b8[e].call(b6,b9,b7))){return i}}}function h(b6,cb,e){var b5,ce,b8,ch,ci,cf,ca,cd,b7=this,cc={},i=b6.style,b9=b6.nodeType&&R(b6),cg=bH._data(b6,"fxshow");if(!e.queue){ci=bH._queueHooks(b6,"fx");if(ci.unqueued==null){ci.unqueued=0;cf=ci.empty.fire;ci.empty.fire=function(){if(!ci.unqueued){cf()}}}ci.unqueued++;b7.always(function(){b7.always(function(){ci.unqueued--;if(!bH.queue(b6,"fx").length){ci.empty.fire()}})})}if(b6.nodeType===1&&("height" in cb||"width" in cb)){e.overflow=[i.overflow,i.overflowX,i.overflowY];ca=bH.css(b6,"display");cd=ca==="none"?bH._data(b6,"olddisplay")||aZ(b6.nodeName):ca;if(cd==="inline"&&bH.css(b6,"float")==="none"){if(!C.inlineBlockNeedsLayout||aZ(b6.nodeName)==="inline"){i.display="inline-block"}else{i.zoom=1}}}if(e.overflow){i.overflow="hidden";if(!C.shrinkWrapBlocks()){b7.always(function(){i.overflow=e.overflow[0];i.overflowX=e.overflow[1];i.overflowY=e.overflow[2]})}}for(b5 in cb){ce=cb[b5];if(bQ.exec(ce)){delete cb[b5];b8=b8||ce==="toggle";if(ce===(b9?"hide":"show")){if(ce==="show"&&cg&&cg[b5]!==undefined){b9=true}else{continue}}cc[b5]=cg&&cg[b5]||bH.style(b6,b5)}else{ca=undefined}}if(!bH.isEmptyObject(cc)){if(cg){if("hidden" in cg){b9=cg.hidden}}else{cg=bH._data(b6,"fxshow",{})}if(b8){cg.hidden=!b9}if(b9){bH(b6).show()}else{b7.done(function(){bH(b6).hide()})}b7.done(function(){var cj;bH._removeData(b6,"fxshow");for(cj in cc){bH.style(b6,cj,cc[cj])}});for(b5 in cc){ch=bc(b9?cg[b5]:0,b5,b7);if(!(b5 in cg)){cg[b5]=ch.start;if(b9){ch.end=ch.start;ch.start=b5==="width"||b5==="height"?1:0}}}}else{if((ca==="none"?aZ(b6.nodeName):ca)==="inline"){i.display=ca}}}function an(b6,b8){var b5,i,b9,b7,e;for(b5 in b6){i=bH.camelCase(b5);b9=b8[i];b7=b6[b5];if(bH.isArray(b7)){b9=b7[1];b7=b6[b5]=b7[0]}if(b5!==i){b6[i]=b7;delete b6[b5]}e=bH.cssHooks[i];if(e&&"expand" in e){b7=e.expand(b7);delete b6[i];for(b5 in b7){if(!(b5 in b6)){b6[b5]=b7[b5];b8[b5]=b9}}}else{b8[i]=b9}}}function f(b5,b9,cc){var cd,e,b8=0,i=aF.length,cb=bH.Deferred().always(function(){delete b7.elem}),b7=function(){if(e){return false}var cj=M||bm(),cg=Math.max(0,b6.startTime+b6.duration-cj),ce=cg/b6.duration||0,ci=1-ce,cf=0,ch=b6.tweens.length;for(;cf<ch;cf++){b6.tweens[cf].run(ci)}cb.notifyWith(b5,[b6,ci,cg]);if(ci<1&&ch){return cg}else{cb.resolveWith(b5,[b6]);return false}},b6=cb.promise({elem:b5,props:bH.extend({},b9),opts:bH.extend(true,{specialEasing:{}},cc),originalProperties:b9,originalOptions:cc,startTime:M||bm(),duration:cc.duration,tweens:[],createTween:function(cg,ce){var cf=bH.Tween(b5,b6.opts,cg,ce,b6.opts.specialEasing[cg]||b6.opts.easing);b6.tweens.push(cf);return cf},stop:function(cf){var ce=0,cg=cf?b6.tweens.length:0;if(e){return this}e=true;for(;ce<cg;ce++){b6.tweens[ce].run(1)}if(cf){cb.resolveWith(b5,[b6,cf])}else{cb.rejectWith(b5,[b6,cf])}return this}}),ca=b6.props;an(ca,b6.opts.specialEasing);for(;b8<i;b8++){cd=aF[b8].call(b6,b5,ca,b6.opts);if(cd){return cd}}bH.map(ca,bc,b6);if(bH.isFunction(b6.opts.start)){b6.opts.start.call(b5,b6)}bH.fx.timer(bH.extend(b7,{elem:b5,anim:b6,queue:b6.opts.queue}));return b6.progress(b6.opts.progress).done(b6.opts.done,b6.opts.complete).fail(b6.opts.fail).always(b6.opts.always)}bH.Animation=bH.extend(f,{tweener:function(i,b7){if(bH.isFunction(i)){b7=i;i=["*"]}else{i=i.split(" ")}var b6,e=0,b5=i.length;for(;e<b5;e++){b6=i[e];a1[b6]=a1[b6]||[];a1[b6].unshift(b7)}},prefilter:function(i,e){if(e){aF.unshift(i)}else{aF.push(i)}}});bH.speed=function(b5,b6,i){var e=b5&&typeof b5==="object"?bH.extend({},b5):{complete:i||!i&&b6||bH.isFunction(b5)&&b5,duration:b5,easing:i&&b6||b6&&!bH.isFunction(b6)&&b6};e.duration=bH.fx.off?0:typeof e.duration==="number"?e.duration:e.duration in bH.fx.speeds?bH.fx.speeds[e.duration]:bH.fx.speeds._default;if(e.queue==null||e.queue===true){e.queue="fx"}e.old=e.complete;e.complete=function(){if(bH.isFunction(e.old)){e.old.call(this)}if(e.queue){bH.dequeue(this,e.queue)}};return e};bH.fn.extend({fadeTo:function(e,b6,b5,i){return this.filter(R).css("opacity",0).show().end().animate({opacity:b6},e,b5,i)},animate:function(b9,b6,b8,b7){var b5=bH.isEmptyObject(b9),e=bH.speed(b6,b8,b7),i=function(){var ca=f(this,bH.extend({},b9),e);if(b5||bH._data(this,"finish")){ca.stop(true)}};i.finish=i;return b5||e.queue===false?this.each(i):this.queue(e.queue,i)},stop:function(b5,i,e){var b6=function(b7){var b8=b7.stop;delete b7.stop;b8(e)};if(typeof b5!=="string"){e=i;i=b5;b5=undefined}if(i&&b5!==false){this.queue(b5||"fx",[])}return this.each(function(){var ca=true,b7=b5!=null&&b5+"queueHooks",b9=bH.timers,b8=bH._data(this);if(b7){if(b8[b7]&&b8[b7].stop){b6(b8[b7])}}else{for(b7 in b8){if(b8[b7]&&b8[b7].stop&&bO.test(b7)){b6(b8[b7])}}}for(b7=b9.length;b7--;){if(b9[b7].elem===this&&(b5==null||b9[b7].queue===b5)){b9[b7].anim.stop(e);ca=false;b9.splice(b7,1)}}if(ca||!e){bH.dequeue(this,b5)}})},finish:function(e){if(e!==false){e=e||"fx"}return this.each(function(){var b6,b9=bH._data(this),b5=b9[e+"queue"],i=b9[e+"queueHooks"],b8=bH.timers,b7=b5?b5.length:0;b9.finish=true;bH.queue(this,e,[]);if(i&&i.stop){i.stop.call(this,true)}for(b6=b8.length;b6--;){if(b8[b6].elem===this&&b8[b6].queue===e){b8[b6].anim.stop(true);b8.splice(b6,1)}}for(b6=0;b6<b7;b6++){if(b5[b6]&&b5[b6].finish){b5[b6].finish.call(this)}}delete b9.finish})}});bH.each(["toggle","show","hide"],function(b5,e){var b6=bH.fn[e];bH.fn[e]=function(i,b8,b7){return i==null||typeof i==="boolean"?b6.apply(this,arguments):this.animate(bG(e,true),i,b8,b7)}});bH.each({slideDown:bG("show"),slideUp:bG("hide"),slideToggle:bG("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(e,i){bH.fn[e]=function(b5,b7,b6){return this.animate(i,b5,b7,b6)}});bH.timers=[];bH.fx.tick=function(){var b6,b5=bH.timers,e=0;M=bH.now();for(;e<b5.length;e++){b6=b5[e];if(!b6()&&b5[e]===b6){b5.splice(e--,1)}}if(!b5.length){bH.fx.stop()}M=undefined};bH.fx.timer=function(e){bH.timers.push(e);if(e()){bH.fx.start()}else{bH.timers.pop()}};bH.fx.interval=13;bH.fx.start=function(){if(!ad){ad=setInterval(bH.fx.tick,bH.fx.interval)}};bH.fx.stop=function(){clearInterval(ad);ad=null};bH.fx.speeds={slow:600,fast:200,_default:400};bH.fn.delay=function(i,e){i=bH.fx?bH.fx.speeds[i]||i:i;e=e||"fx";return this.queue(e,function(b6,b5){var b7=setTimeout(b6,i);b5.stop=function(){clearTimeout(b7)}})};(function(){var b5,b7,e,i,b6;b7=n.createElement("div");b7.setAttribute("className","t");b7.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";i=b7.getElementsByTagName("a")[0];e=n.createElement("select");b6=e.appendChild(n.createElement("option"));b5=b7.getElementsByTagName("input")[0];i.style.cssText="top:1px";C.getSetAttribute=b7.className!=="t";C.style=/top/.test(i.getAttribute("style"));C.hrefNormalized=i.getAttribute("href")==="/a";C.checkOn=!!b5.value;C.optSelected=b6.selected;C.enctype=!!n.createElement("form").enctype;e.disabled=true;C.optDisabled=!b6.disabled;b5=n.createElement("input");b5.setAttribute("value","");C.input=b5.getAttribute("value")==="";b5.value="t";b5.setAttribute("type","radio");C.radioValue=b5.value==="t"})();var ak=/\r/g;bH.fn.extend({val:function(b6){var e,i,b7,b5=this[0];if(!arguments.length){if(b5){e=bH.valHooks[b5.type]||bH.valHooks[b5.nodeName.toLowerCase()];if(e&&"get" in e&&(i=e.get(b5,"value"))!==undefined){return i}i=b5.value;return typeof i==="string"?i.replace(ak,""):i==null?"":i}return}b7=bH.isFunction(b6);return this.each(function(b8){var b9;if(this.nodeType!==1){return}if(b7){b9=b6.call(this,b8,bH(this).val())}else{b9=b6}if(b9==null){b9=""}else{if(typeof b9==="number"){b9+=""}else{if(bH.isArray(b9)){b9=bH.map(b9,function(ca){return ca==null?"":ca+""})}}}e=bH.valHooks[this.type]||bH.valHooks[this.nodeName.toLowerCase()];if(!e||!("set" in e)||e.set(this,b9,"value")===undefined){this.value=b9}})}});bH.extend({valHooks:{option:{get:function(e){var i=bH.find.attr(e,"value");return i!=null?i:bH.trim(bH.text(e))}},select:{get:function(e){var ca,b6,cc=e.options,b8=e.selectedIndex,b7=e.type==="select-one"||b8<0,cb=b7?null:[],b9=b7?b8+1:cc.length,b5=b8<0?b9:b7?b8:0;for(;b5<b9;b5++){b6=cc[b5];if((b6.selected||b5===b8)&&(C.optDisabled?!b6.disabled:b6.getAttribute("disabled")===null)&&(!b6.parentNode.disabled||!bH.nodeName(b6.parentNode,"optgroup"))){ca=bH(b6).val();if(b7){return ca}cb.push(ca)}}return cb},set:function(b9,ca){var cb,b8,b6=b9.options,e=bH.makeArray(ca),b7=b6.length;while(b7--){b8=b6[b7];if(bH.inArray(bH.valHooks.option.get(b8),e)>=0){try{b8.selected=cb=true}catch(b5){b8.scrollHeight}}else{b8.selected=false}}if(!cb){b9.selectedIndex=-1}return b6}}}});bH.each(["radio","checkbox"],function(){bH.valHooks[this]={set:function(e,i){if(bH.isArray(i)){return(e.checked=bH.inArray(bH(e).val(),i)>=0)}}};if(!C.checkOn){bH.valHooks[this].get=function(e){return e.getAttribute("value")===null?"on":e.value}}});var a9,b2,bN=bH.expr.attrHandle,ap=/^(?:checked|selected)$/i,bM=C.getSetAttribute,bE=C.input;bH.fn.extend({attr:function(e,i){return aA(this,bH.attr,e,i,arguments.length>1)},removeAttr:function(e){return this.each(function(){bH.removeAttr(this,e)})}});bH.extend({attr:function(b7,b6,b8){var e,b5,i=b7.nodeType;if(!b7||i===3||i===8||i===2){return}if(typeof b7.getAttribute===aB){return bH.prop(b7,b6,b8)}if(i!==1||!bH.isXMLDoc(b7)){b6=b6.toLowerCase();e=bH.attrHooks[b6]||(bH.expr.match.bool.test(b6)?b2:a9)}if(b8!==undefined){if(b8===null){bH.removeAttr(b7,b6)}else{if(e&&"set" in e&&(b5=e.set(b7,b8,b6))!==undefined){return b5}else{b7.setAttribute(b6,b8+"");return b8}}}else{if(e&&"get" in e&&(b5=e.get(b7,b6))!==null){return b5}else{b5=bH.find.attr(b7,b6);return b5==null?undefined:b5}}},removeAttr:function(b6,b8){var e,b7,b5=0,b9=b8&&b8.match(aE);if(b9&&b6.nodeType===1){while((e=b9[b5++])){b7=bH.propFix[e]||e;if(bH.expr.match.bool.test(e)){if(bE&&bM||!ap.test(e)){b6[b7]=false}else{b6[bH.camelCase("default-"+e)]=b6[b7]=false}}else{bH.attr(b6,e,"")}b6.removeAttribute(bM?e:b7)}}},attrHooks:{type:{set:function(e,i){if(!C.radioValue&&i==="radio"&&bH.nodeName(e,"input")){var b5=e.value;e.setAttribute("type",i);if(b5){e.value=b5}return i}}}}});b2={set:function(i,b5,e){if(b5===false){bH.removeAttr(i,e)}else{if(bE&&bM||!ap.test(e)){i.setAttribute(!bM&&bH.propFix[e]||e,e)}else{i[bH.camelCase("default-"+e)]=i[e]=true}}return e}};bH.each(bH.expr.match.bool.source.match(/\w+/g),function(b6,b5){var e=bN[b5]||bH.find.attr;bN[b5]=bE&&bM||!ap.test(b5)?function(b8,b7,ca){var i,b9;if(!ca){b9=bN[b7];bN[b7]=i;i=e(b8,b7,ca)!=null?b7.toLowerCase():null;bN[b7]=b9}return i}:function(b7,i,b8){if(!b8){return b7[bH.camelCase("default-"+i)]?i.toLowerCase():null}}});if(!bE||!bM){bH.attrHooks.value={set:function(i,b5,e){if(bH.nodeName(i,"input")){i.defaultValue=b5}else{return a9&&a9.set(i,b5,e)}}}}if(!bM){a9={set:function(b5,b6,i){var e=b5.getAttributeNode(i);if(!e){b5.setAttributeNode((e=b5.ownerDocument.createAttribute(i)))}e.value=b6+="";if(i==="value"||b6===b5.getAttribute(i)){return b6}}};bN.id=bN.name=bN.coords=function(b5,i,b6){var e;if(!b6){return(e=b5.getAttributeNode(i))&&e.value!==""?e.value:null}};bH.valHooks.button={get:function(b5,i){var e=b5.getAttributeNode(i);if(e&&e.specified){return e.value}},set:a9.set};bH.attrHooks.contenteditable={set:function(i,b5,e){a9.set(i,b5===""?false:b5,e)}};bH.each(["width","height"],function(b5,e){bH.attrHooks[e]={set:function(i,b6){if(b6===""){i.setAttribute(e,"auto");return b6}}}})}if(!C.style){bH.attrHooks.style={get:function(e){return e.style.cssText||undefined},set:function(e,i){return(e.style.cssText=i+"")}}}var aI=/^(?:input|select|textarea|button|object)$/i,E=/^(?:a|area)$/i;bH.fn.extend({prop:function(e,i){return aA(this,bH.prop,e,i,arguments.length>1)},removeProp:function(e){e=bH.propFix[e]||e;return this.each(function(){try{this[e]=undefined;delete this[e]}catch(i){}})}});bH.extend({propFix:{"for":"htmlFor","class":"className"},prop:function(b8,b6,b9){var b5,e,b7,i=b8.nodeType;if(!b8||i===3||i===8||i===2){return}b7=i!==1||!bH.isXMLDoc(b8);if(b7){b6=bH.propFix[b6]||b6;e=bH.propHooks[b6]}if(b9!==undefined){return e&&"set" in e&&(b5=e.set(b8,b9,b6))!==undefined?b5:(b8[b6]=b9)}else{return e&&"get" in e&&(b5=e.get(b8,b6))!==null?b5:b8[b6]}},propHooks:{tabIndex:{get:function(i){var e=bH.find.attr(i,"tabindex");return e?parseInt(e,10):aI.test(i.nodeName)||E.test(i.nodeName)&&i.href?0:-1}}}});if(!C.hrefNormalized){bH.each(["href","src"],function(b5,e){bH.propHooks[e]={get:function(i){return i.getAttribute(e,4)}}})}if(!C.optSelected){bH.propHooks.selected={get:function(i){var e=i.parentNode;if(e){e.selectedIndex;if(e.parentNode){e.parentNode.selectedIndex}}return null}}}bH.each(["tabIndex","readOnly","maxLength","cellSpacing","cellPadding","rowSpan","colSpan","useMap","frameBorder","contentEditable"],function(){bH.propFix[this.toLowerCase()]=this});if(!C.enctype){bH.propFix.enctype="encoding"}var bK=/[\t\r\n\f]/g;bH.fn.extend({addClass:function(cc){var b6,b5,cd,ca,b7,e,b8=0,b9=this.length,cb=typeof cc==="string"&&cc;if(bH.isFunction(cc)){return this.each(function(i){bH(this).addClass(cc.call(this,i,this.className))})}if(cb){b6=(cc||"").match(aE)||[];for(;b8<b9;b8++){b5=this[b8];cd=b5.nodeType===1&&(b5.className?(" "+b5.className+" ").replace(bK," "):" ");if(cd){b7=0;while((ca=b6[b7++])){if(cd.indexOf(" "+ca+" ")<0){cd+=ca+" "}}e=bH.trim(cd);if(b5.className!==e){b5.className=e}}}}return this},removeClass:function(cc){var b6,b5,cd,ca,b7,e,b8=0,b9=this.length,cb=arguments.length===0||typeof cc==="string"&&cc;if(bH.isFunction(cc)){return this.each(function(i){bH(this).removeClass(cc.call(this,i,this.className))})}if(cb){b6=(cc||"").match(aE)||[];for(;b8<b9;b8++){b5=this[b8];cd=b5.nodeType===1&&(b5.className?(" "+b5.className+" ").replace(bK," "):"");if(cd){b7=0;while((ca=b6[b7++])){while(cd.indexOf(" "+ca+" ")>=0){cd=cd.replace(" "+ca+" "," ")}}e=cc?bH.trim(cd):"";if(b5.className!==e){b5.className=e}}}}return this},toggleClass:function(b5,e){var i=typeof b5;if(typeof e==="boolean"&&i==="string"){return e?this.addClass(b5):this.removeClass(b5)}if(bH.isFunction(b5)){return this.each(function(b6){bH(this).toggleClass(b5.call(this,b6,this.className,e),e)})}return this.each(function(){if(i==="string"){var b8,b7=0,b6=bH(this),b9=b5.match(aE)||[];while((b8=b9[b7++])){if(b6.hasClass(b8)){b6.removeClass(b8)}else{b6.addClass(b8)}}}else{if(i===aB||i==="boolean"){if(this.className){bH._data(this,"__className__",this.className)}this.className=this.className||b5===false?"":bH._data(this,"__className__")||""}}})},hasClass:function(e){var b7=" "+e+" ",b6=0,b5=this.length;for(;b6<b5;b6++){if(this[b6].nodeType===1&&(" "+this[b6].className+" ").replace(bK," ").indexOf(b7)>=0){return true}}return false}});bH.each(("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu").split(" "),function(b5,e){bH.fn[e]=function(b6,i){return arguments.length>0?this.on(e,null,b6,i):this.trigger(e)}});bH.fn.extend({hover:function(e,i){return this.mouseenter(e).mouseleave(i||e)},bind:function(e,b5,i){return this.on(e,null,b5,i)},unbind:function(e,i){return this.off(e,null,i)},delegate:function(e,i,b6,b5){return this.on(i,e,b6,b5)},undelegate:function(e,i,b5){return arguments.length===1?this.off(e,"**"):this.off(i,e||"**",b5)}});var bo=bH.now();var bP=(/\?/);var a0=/(,)|(\[|{)|(}|])|"(?:[^"\\\r\n]|\\["\\\/bfnrt]|\\u[\da-fA-F]{4})*"\s*:?|true|false|null|-?(?!0\d)\d+(?:\.\d+|)(?:[eE][+-]?\d+|)/g;bH.parseJSON=function(e){if(a4.JSON&&a4.JSON.parse){return a4.JSON.parse(e+"")}var b6,b5=null,i=bH.trim(e+"");return i&&!bH.trim(i.replace(a0,function(b9,b7,b8,ca){if(b6&&b7){b5=0}if(b5===0){return b9}b6=b8||b7;b5+=!ca-!b8;return""}))?(Function("return "+i))():bH.error("Invalid JSON: "+e)};bH.parseXML=function(b6){var i,b5;if(!b6||typeof b6!=="string"){return null}try{if(a4.DOMParser){b5=new DOMParser();i=b5.parseFromString(b6,"text/xml")}else{i=new ActiveXObject("Microsoft.XMLDOM");i.async="false";i.loadXML(b6)}}catch(b7){i=undefined}if(!i||!i.documentElement||i.getElementsByTagName("parsererror").length){bH.error("Invalid XML: "+b6)}return i};var b3,Z,ao=/#.*$/,Q=/([?&])_=[^&]*/,ag=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,B=/^(?:about|app|app-storage|.+-extension|file|res|widget):$/,q=/^(?:GET|HEAD)$/,aJ=/^\/\//,aU=/^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/,v={},a8={},aW="*/".concat("*");try{Z=location.href}catch(bh){Z=n.createElement("a");Z.href="";Z=Z.href}b3=aU.exec(Z.toLowerCase())||[];function bJ(e){return function(b8,b9){if(typeof b8!=="string"){b9=b8;b8="*"}var b5,b6=0,b7=b8.toLowerCase().match(aE)||[];if(bH.isFunction(b9)){while((b5=b7[b6++])){if(b5.charAt(0)==="+"){b5=b5.slice(1)||"*";(e[b5]=e[b5]||[]).unshift(b9)}else{(e[b5]=e[b5]||[]).push(b9)}}}}}function p(e,b5,b9,b6){var i={},b7=(e===a8);function b8(ca){var cb;i[ca]=true;bH.each(e[ca]||[],function(cd,cc){var ce=cc(b5,b9,b6);if(typeof ce==="string"&&!b7&&!i[ce]){b5.dataTypes.unshift(ce);b8(ce);return false}else{if(b7){return !(cb=ce)}}});return cb}return b8(b5.dataTypes[0])||!i["*"]&&b8("*")}function s(b5,b6){var e,i,b7=bH.ajaxSettings.flatOptions||{};for(i in b6){if(b6[i]!==undefined){(b7[i]?b5:(e||(e={})))[i]=b6[i]}}if(e){bH.extend(true,b5,e)}return b5}function g(cb,ca,b7){var e,b6,b5,b8,i=cb.contents,b9=cb.dataTypes;while(b9[0]==="*"){b9.shift();if(b6===undefined){b6=cb.mimeType||ca.getResponseHeader("Content-Type")}}if(b6){for(b8 in i){if(i[b8]&&i[b8].test(b6)){b9.unshift(b8);break}}}if(b9[0] in b7){b5=b9[0]}else{for(b8 in b7){if(!b9[0]||cb.converters[b8+" "+b9[0]]){b5=b8;break}if(!e){e=b8}}b5=b5||e}if(b5){if(b5!==b9[0]){b9.unshift(b5)}return b7[b5]}}function af(cf,b7,cc,b5){var i,ca,cd,b8,b6,ce={},cb=cf.dataTypes.slice();if(cb[1]){for(cd in cf.converters){ce[cd.toLowerCase()]=cf.converters[cd]}}ca=cb.shift();while(ca){if(cf.responseFields[ca]){cc[cf.responseFields[ca]]=b7}if(!b6&&b5&&cf.dataFilter){b7=cf.dataFilter(b7,cf.dataType)}b6=ca;ca=cb.shift();if(ca){if(ca==="*"){ca=b6}else{if(b6!=="*"&&b6!==ca){cd=ce[b6+" "+ca]||ce["* "+ca];if(!cd){for(i in ce){b8=i.split(" ");if(b8[1]===ca){cd=ce[b6+" "+b8[0]]||ce["* "+b8[0]];if(cd){if(cd===true){cd=ce[i]}else{if(ce[i]!==true){ca=b8[0];cb.unshift(b8[1])}}break}}}}if(cd!==true){if(cd&&cf["throws"]){b7=cd(b7)}else{try{b7=cd(b7)}catch(b9){return{state:"parsererror",error:cd?b9:"No conversion from "+b6+" to "+ca}}}}}}}}return{state:"success",data:b7}}bH.extend({active:0,lastModified:{},etag:{},ajaxSettings:{url:Z,type:"GET",isLocal:B.test(b3[1]),global:true,processData:true,async:true,contentType:"application/x-www-form-urlencoded; charset=UTF-8",accepts:{"*":aW,text:"text/plain",html:"text/html",xml:"application/xml, text/xml",json:"application/json, text/javascript"},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText",json:"responseJSON"},converters:{"* text":String,"text html":true,"text json":bH.parseJSON,"text xml":bH.parseXML},flatOptions:{url:true,context:true}},ajaxSetup:function(i,e){return e?s(s(i,bH.ajaxSettings),e):s(bH.ajaxSettings,i)},ajaxPrefilter:bJ(v),ajaxTransport:bJ(a8),ajax:function(b9,b6){if(typeof b9==="object"){b6=b9;b9=undefined}b6=b6||{};var ci,ck,ca,cp,ce,b5,cl,b7,cd=bH.ajaxSetup({},b6),cr=cd.context||cd,cg=cd.context&&(cr.nodeType||cr.jquery)?bH(cr):bH.event,cq=bH.Deferred(),cn=bH.Callbacks("once memory"),cb=cd.statusCode||{},ch={},co={},b8=0,cc="canceled",cj={readyState:0,getResponseHeader:function(i){var e;if(b8===2){if(!b7){b7={};while((e=ag.exec(cp))){b7[e[1].toLowerCase()]=e[2]}}e=b7[i.toLowerCase()]}return e==null?null:e},getAllResponseHeaders:function(){return b8===2?cp:null},setRequestHeader:function(i,cs){var e=i.toLowerCase();if(!b8){i=co[e]=co[e]||i;ch[i]=cs}return this},overrideMimeType:function(e){if(!b8){cd.mimeType=e}return this},statusCode:function(i){var e;if(i){if(b8<2){for(e in i){cb[e]=[cb[e],i[e]]}}else{cj.always(i[cj.status])}}return this},abort:function(i){var e=i||cc;if(cl){cl.abort(e)}cf(0,e);return this}};cq.promise(cj).complete=cn.add;cj.success=cj.done;cj.error=cj.fail;cd.url=((b9||cd.url||Z)+"").replace(ao,"").replace(aJ,b3[1]+"//");cd.type=b6.method||b6.type||cd.method||cd.type;cd.dataTypes=bH.trim(cd.dataType||"*").toLowerCase().match(aE)||[""];if(cd.crossDomain==null){ci=aU.exec(cd.url.toLowerCase());cd.crossDomain=!!(ci&&(ci[1]!==b3[1]||ci[2]!==b3[2]||(ci[3]||(ci[1]==="http:"?"80":"443"))!==(b3[3]||(b3[1]==="http:"?"80":"443"))))}if(cd.data&&cd.processData&&typeof cd.data!=="string"){cd.data=bH.param(cd.data,cd.traditional)}p(v,cd,b6,cj);if(b8===2){return cj}b5=bH.event&&cd.global;if(b5&&bH.active++===0){bH.event.trigger("ajaxStart")}cd.type=cd.type.toUpperCase();cd.hasContent=!q.test(cd.type);ca=cd.url;if(!cd.hasContent){if(cd.data){ca=(cd.url+=(bP.test(ca)?"&":"?")+cd.data);delete cd.data}if(cd.cache===false){cd.url=Q.test(ca)?ca.replace(Q,"$1_="+bo++):ca+(bP.test(ca)?"&":"?")+"_="+bo++}}if(cd.ifModified){if(bH.lastModified[ca]){cj.setRequestHeader("If-Modified-Since",bH.lastModified[ca])}if(bH.etag[ca]){cj.setRequestHeader("If-None-Match",bH.etag[ca])}}if(cd.data&&cd.hasContent&&cd.contentType!==false||b6.contentType){cj.setRequestHeader("Content-Type",cd.contentType)}cj.setRequestHeader("Accept",cd.dataTypes[0]&&cd.accepts[cd.dataTypes[0]]?cd.accepts[cd.dataTypes[0]]+(cd.dataTypes[0]!=="*"?", "+aW+"; q=0.01":""):cd.accepts["*"]);for(ck in cd.headers){cj.setRequestHeader(ck,cd.headers[ck])}if(cd.beforeSend&&(cd.beforeSend.call(cr,cj,cd)===false||b8===2)){return cj.abort()}cc="abort";for(ck in {success:1,error:1,complete:1}){cj[ck](cd[ck])}cl=p(a8,cd,b6,cj);if(!cl){cf(-1,"No Transport")}else{cj.readyState=1;if(b5){cg.trigger("ajaxSend",[cj,cd])}if(cd.async&&cd.timeout>0){ce=setTimeout(function(){cj.abort("timeout")},cd.timeout)}try{b8=1;cl.send(ch,cf)}catch(cm){if(b8<2){cf(-1,cm)}else{throw cm}}}function cf(cv,i,cw,ct){var e,cz,cx,cu,cy,cs=i;if(b8===2){return}b8=2;if(ce){clearTimeout(ce)}cl=undefined;cp=ct||"";cj.readyState=cv>0?4:0;e=cv>=200&&cv<300||cv===304;if(cw){cu=g(cd,cj,cw)}cu=af(cd,cu,cj,e);if(e){if(cd.ifModified){cy=cj.getResponseHeader("Last-Modified");if(cy){bH.lastModified[ca]=cy}cy=cj.getResponseHeader("etag");if(cy){bH.etag[ca]=cy}}if(cv===204||cd.type==="HEAD"){cs="nocontent"}else{if(cv===304){cs="notmodified"}else{cs=cu.state;cz=cu.data;cx=cu.error;e=!cx}}}else{cx=cs;if(cv||!cs){cs="error";if(cv<0){cv=0}}}cj.status=cv;cj.statusText=(i||cs)+"";if(e){cq.resolveWith(cr,[cz,cs,cj])}else{cq.rejectWith(cr,[cj,cs,cx])}cj.statusCode(cb);cb=undefined;if(b5){cg.trigger(e?"ajaxSuccess":"ajaxError",[cj,cd,e?cz:cx])}cn.fireWith(cr,[cj,cs]);if(b5){cg.trigger("ajaxComplete",[cj,cd]);if(!(--bH.active)){bH.event.trigger("ajaxStop")}}}return cj},getJSON:function(e,i,b5){return bH.get(e,i,b5,"json")},getScript:function(e,i){return bH.get(e,undefined,i,"script")}});bH.each(["get","post"],function(e,b5){bH[b5]=function(i,b7,b8,b6){if(bH.isFunction(b7)){b6=b6||b8;b8=b7;b7=undefined}return bH.ajax({url:i,type:b5,dataType:b6,data:b7,success:b8})}});bH._evalUrl=function(e){return bH.ajax({url:e,type:"GET",dataType:"script",async:false,global:false,"throws":true})};bH.fn.extend({wrapAll:function(e){if(bH.isFunction(e)){return this.each(function(b5){bH(this).wrapAll(e.call(this,b5))})}if(this[0]){var i=bH(e,this[0].ownerDocument).eq(0).clone(true);if(this[0].parentNode){i.insertBefore(this[0])}i.map(function(){var b5=this;while(b5.firstChild&&b5.firstChild.nodeType===1){b5=b5.firstChild}return b5}).append(this)}return this},wrapInner:function(e){if(bH.isFunction(e)){return this.each(function(b5){bH(this).wrapInner(e.call(this,b5))})}return this.each(function(){var i=bH(this),b5=i.contents();if(b5.length){b5.wrapAll(e)}else{i.append(e)}})},wrap:function(e){var i=bH.isFunction(e);return this.each(function(b5){bH(this).wrapAll(i?e.call(this,b5):e)})},unwrap:function(){return this.parent().each(function(){if(!bH.nodeName(this,"body")){bH(this).replaceWith(this.childNodes)}}).end()}});bH.expr.filters.hidden=function(e){return e.offsetWidth<=0&&e.offsetHeight<=0||(!C.reliableHiddenOffsets()&&((e.style&&e.style.display)||bH.css(e,"display"))==="none")};bH.expr.filters.visible=function(e){return !bH.expr.filters.hidden(e)};var bv=/%20/g,aR=/\[\]$/,W=/\r?\n/g,b=/^(?:submit|button|image|reset|file)$/i,at=/^(?:input|select|textarea|keygen)/i;function j(b5,b7,i,b6){var e;if(bH.isArray(b7)){bH.each(b7,function(b9,b8){if(i||aR.test(b5)){b6(b5,b8)}else{j(b5+"["+(typeof b8==="object"?b9:"")+"]",b8,i,b6)}})}else{if(!i&&bH.type(b7)==="object"){for(e in b7){j(b5+"["+e+"]",b7[e],i,b6)}}else{b6(b5,b7)}}}bH.param=function(e,b5){var b6,i=[],b7=function(b8,b9){b9=bH.isFunction(b9)?b9():(b9==null?"":b9);i[i.length]=encodeURIComponent(b8)+"="+encodeURIComponent(b9)};if(b5===undefined){b5=bH.ajaxSettings&&bH.ajaxSettings.traditional}if(bH.isArray(e)||(e.jquery&&!bH.isPlainObject(e))){bH.each(e,function(){b7(this.name,this.value)})}else{for(b6 in e){j(b6,e[b6],b5,b7)}}return i.join("&").replace(bv,"+")};bH.fn.extend({serialize:function(){return bH.param(this.serializeArray())},serializeArray:function(){return this.map(function(){var e=bH.prop(this,"elements");return e?bH.makeArray(e):this}).filter(function(){var e=this.type;return this.name&&!bH(this).is(":disabled")&&at.test(this.nodeName)&&!b.test(e)&&(this.checked||!aL.test(e))}).map(function(e,b5){var b6=bH(this).val();return b6==null?null:bH.isArray(b6)?bH.map(b6,function(i){return{name:b5.name,value:i.replace(W,"\r\n")}}):{name:b5.name,value:b6.replace(W,"\r\n")}}).get()}});bH.ajaxSettings.xhr=a4.ActiveXObject!==undefined?function(){return !this.isLocal&&/^(get|post|head|put|delete|options)$/i.test(this.type)&&bD()||bf()}:bD;var az=0,ai={},ax=bH.ajaxSettings.xhr();if(a4.attachEvent){a4.attachEvent("onunload",function(){for(var e in ai){ai[e](undefined,true)}})}C.cors=!!ax&&("withCredentials" in ax);ax=C.ajax=!!ax;if(ax){bH.ajaxTransport(function(e){if(!e.crossDomain||C.cors){var i;return{send:function(b8,b5){var b6,b7=e.xhr(),b9=++az;b7.open(e.type,e.url,e.async,e.username,e.password);if(e.xhrFields){for(b6 in e.xhrFields){b7[b6]=e.xhrFields[b6]}}if(e.mimeType&&b7.overrideMimeType){b7.overrideMimeType(e.mimeType)}if(!e.crossDomain&&!b8["X-Requested-With"]){b8["X-Requested-With"]="XMLHttpRequest"}for(b6 in b8){if(b8[b6]!==undefined){b7.setRequestHeader(b6,b8[b6]+"")}}b7.send((e.hasContent&&e.data)||null);i=function(cc,cb){var ca,cf,cd;if(i&&(cb||b7.readyState===4)){delete ai[b9];i=undefined;b7.onreadystatechange=bH.noop;if(cb){if(b7.readyState!==4){b7.abort()}}else{cd={};ca=b7.status;if(typeof b7.responseText==="string"){cd.text=b7.responseText}try{cf=b7.statusText}catch(ce){cf=""}if(!ca&&e.isLocal&&!e.crossDomain){ca=cd.text?200:404}else{if(ca===1223){ca=204}}}}if(cd){b5(ca,cf,cd,b7.getAllResponseHeaders())}};if(!e.async){i()}else{if(b7.readyState===4){setTimeout(i)}else{b7.onreadystatechange=ai[b9]=i}}},abort:function(){if(i){i(undefined,true)}}}}})}function bD(){try{return new a4.XMLHttpRequest()}catch(i){}}function bf(){try{return new a4.ActiveXObject("Microsoft.XMLHTTP")}catch(i){}}bH.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/(?:java|ecma)script/},converters:{"text script":function(e){bH.globalEval(e);return e}}});bH.ajaxPrefilter("script",function(e){if(e.cache===undefined){e.cache=false}if(e.crossDomain){e.type="GET";e.global=false}});bH.ajaxTransport("script",function(b5){if(b5.crossDomain){var e,i=n.head||bH("head")[0]||n.documentElement;return{send:function(b6,b7){e=n.createElement("script");e.async=true;if(b5.scriptCharset){e.charset=b5.scriptCharset}e.src=b5.url;e.onload=e.onreadystatechange=function(b9,b8){if(b8||!e.readyState||/loaded|complete/.test(e.readyState)){e.onload=e.onreadystatechange=null;if(e.parentNode){e.parentNode.removeChild(e)}e=null;if(!b8){b7(200,"success")}}};i.insertBefore(e,i.firstChild)},abort:function(){if(e){e.onload(undefined,true)}}}}});var br=[],a7=/(=)\?(?=&|$)|\?\?/;bH.ajaxSetup({jsonp:"callback",jsonpCallback:function(){var e=br.pop()||(bH.expando+"_"+(bo++));this[e]=true;return e}});bH.ajaxPrefilter("json jsonp",function(b6,e,b7){var b9,i,b5,b8=b6.jsonp!==false&&(a7.test(b6.url)?"url":typeof b6.data==="string"&&!(b6.contentType||"").indexOf("application/x-www-form-urlencoded")&&a7.test(b6.data)&&"data");if(b8||b6.dataTypes[0]==="jsonp"){b9=b6.jsonpCallback=bH.isFunction(b6.jsonpCallback)?b6.jsonpCallback():b6.jsonpCallback;if(b8){b6[b8]=b6[b8].replace(a7,"$1"+b9)}else{if(b6.jsonp!==false){b6.url+=(bP.test(b6.url)?"&":"?")+b6.jsonp+"="+b9}}b6.converters["script json"]=function(){if(!b5){bH.error(b9+" was not called")}return b5[0]};b6.dataTypes[0]="json";i=a4[b9];a4[b9]=function(){b5=arguments};b7.always(function(){a4[b9]=i;if(b6[b9]){b6.jsonpCallback=e.jsonpCallback;br.push(b9)}if(b5&&bH.isFunction(i)){i(b5[0])}b5=i=undefined});return"script"}});bH.parseHTML=function(b7,b5,b6){if(!b7||typeof b7!=="string"){return null}if(typeof b5==="boolean"){b6=b5;b5=false}b5=b5||n;var i=a.exec(b7),e=!b6&&[];if(i){return[b5.createElement(i[1])]}i=bH.buildFragment([b7],b5,e);if(e&&e.length){bH(e).remove()}return bH.merge([],i.childNodes)};var b0=bH.fn.load;bH.fn.load=function(b6,b9,ca){if(typeof b6!=="string"&&b0){return b0.apply(this,arguments)}var e,b5,b7,i=this,b8=b6.indexOf(" ");if(b8>=0){e=bH.trim(b6.slice(b8,b6.length));b6=b6.slice(0,b8)}if(bH.isFunction(b9)){ca=b9;b9=undefined}else{if(b9&&typeof b9==="object"){b7="POST"}}if(i.length>0){bH.ajax({url:b6,type:b7,dataType:"html",data:b9}).done(function(cb){b5=arguments;i.html(e?bH("<div>").append(bH.parseHTML(cb)).find(e):cb)}).complete(ca&&function(cc,cb){i.each(ca,b5||[cc.responseText,cb,cc])})}return this};bH.each(["ajaxStart","ajaxStop","ajaxComplete","ajaxError","ajaxSuccess","ajaxSend"],function(e,b5){bH.fn[b5]=function(i){return this.on(b5,i)}});bH.expr.filters.animated=function(e){return bH.grep(bH.timers,function(i){return e===i.elem}).length};var bW=a4.document.documentElement;function bq(e){return bH.isWindow(e)?e:e.nodeType===9?e.defaultView||e.parentWindow:false}bH.offset={setOffset:function(b6,cg,ca){var cc,b9,e,b7,b5,ce,cf,cb=bH.css(b6,"position"),b8=bH(b6),cd={};if(cb==="static"){b6.style.position="relative"}b5=b8.offset();e=bH.css(b6,"top");ce=bH.css(b6,"left");cf=(cb==="absolute"||cb==="fixed")&&bH.inArray("auto",[e,ce])>-1;if(cf){cc=b8.position();b7=cc.top;b9=cc.left}else{b7=parseFloat(e)||0;b9=parseFloat(ce)||0}if(bH.isFunction(cg)){cg=cg.call(b6,ca,b5)}if(cg.top!=null){cd.top=(cg.top-b5.top)+b7}if(cg.left!=null){cd.left=(cg.left-b5.left)+b9}if("using" in cg){cg.using.call(b6,cd)}else{b8.css(cd)}}};bH.fn.extend({offset:function(i){if(arguments.length){return i===undefined?this:this.each(function(b9){bH.offset.setOffset(this,i,b9)})}var e,b8,b6={top:0,left:0},b5=this[0],b7=b5&&b5.ownerDocument;if(!b7){return}e=b7.documentElement;if(!bH.contains(e,b5)){return b6}if(typeof b5.getBoundingClientRect!==aB){b6=b5.getBoundingClientRect()}b8=bq(b7);return{top:b6.top+(b8.pageYOffset||e.scrollTop)-(e.clientTop||0),left:b6.left+(b8.pageXOffset||e.scrollLeft)-(e.clientLeft||0)}},position:function(){if(!this[0]){return}var b5,b6,e={top:0,left:0},i=this[0];if(bH.css(i,"position")==="fixed"){b6=i.getBoundingClientRect()}else{b5=this.offsetParent();b6=this.offset();if(!bH.nodeName(b5[0],"html")){e=b5.offset()}e.top+=bH.css(b5[0],"borderTopWidth",true);e.left+=bH.css(b5[0],"borderLeftWidth",true)}return{top:b6.top-e.top-bH.css(i,"marginTop",true),left:b6.left-e.left-bH.css(i,"marginLeft",true)}},offsetParent:function(){return this.map(function(){var e=this.offsetParent||bW;while(e&&(!bH.nodeName(e,"html")&&bH.css(e,"position")==="static")){e=e.offsetParent}return e||bW})}});bH.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(b5,i){var e=/Y/.test(i);bH.fn[b5]=function(b6){return aA(this,function(b7,ca,b9){var b8=bq(b7);if(b9===undefined){return b8?(i in b8)?b8[i]:b8.document.documentElement[ca]:b7[ca]}if(b8){b8.scrollTo(!e?b9:bH(b8).scrollLeft(),e?b9:bH(b8).scrollTop())}else{b7[ca]=b9}},b5,b6,arguments.length,null)}});bH.each(["top","left"],function(e,b5){bH.cssHooks[b5]=a6(C.pixelPosition,function(b6,i){if(i){i=F(b6,b5);return X.test(i)?bH(b6).position()[b5]+"px":i}})});bH.each({Height:"height",Width:"width"},function(e,i){bH.each({padding:"inner"+e,content:i,"":"outer"+e},function(b5,b6){bH.fn[b6]=function(ca,b9){var b8=arguments.length&&(b5||typeof ca!=="boolean"),b7=b5||(ca===true||b9===true?"margin":"border");return aA(this,function(cc,cb,cd){var ce;if(bH.isWindow(cc)){return cc.document.documentElement["client"+e]}if(cc.nodeType===9){ce=cc.documentElement;return Math.max(cc.body["scroll"+e],ce["scroll"+e],cc.body["offset"+e],ce["offset"+e],ce["client"+e])}return cd===undefined?bH.css(cc,cb,b7):bH.style(cc,cb,cd,b7)},i,b8?ca:undefined,b8,null)}})});bH.fn.size=function(){return this.length};bH.fn.andSelf=bH.fn.addBack;if(typeof define==="function"&&define.amd){define("jquery",[],function(){return bH})}var bj=a4.jQuery,H=a4.$;bH.noConflict=function(e){if(a4.$===bH){a4.$=H}if(e&&a4.jQuery===bH){a4.jQuery=bj}return bH};if(typeof au===aB){a4.jQuery=a4.$=bH}return bH}));
/* ===========================================================
 * bootstrap-tooltip.js v2.0.4
 * http://twitter.github.com/bootstrap/javascript.html#tooltips
 * Inspired by the original jQuery.tipsy by Jason Frame
 * ===========================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */


!function ($) {

    "use strict"; // jshint ;_;


    /* TOOLTIP PUBLIC CLASS DEFINITION
     * =============================== */

    var Tooltip = function (element, options) {
        this.init('tooltip', element, options)
    }

    Tooltip.prototype = {

        constructor: Tooltip

        , init: function (type, element, options) {
            var eventIn
                , eventOut

            this.type = type
            this.$element = $(element)
            this.options = this.getOptions(options)
            this.enabled = true

            if (this.options.trigger != 'manual') {
                eventIn  = this.options.trigger == 'hover' ? 'mouseenter' : 'focus'
                eventOut = this.options.trigger == 'hover' ? 'mouseleave' : 'blur'
                this.$element.on(eventIn, this.options.selector, $.proxy(this.enter, this))
                this.$element.on(eventOut, this.options.selector, $.proxy(this.leave, this))
            }

            this.options.selector ?
                (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
                this.fixTitle()
        }

        , getOptions: function (options) {
            options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data())

            if (options.delay && typeof options.delay == 'number') {
                options.delay = {
                    show: options.delay
                    , hide: options.delay
                }
            }

            return options
        }

        , enter: function (e) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (!self.options.delay || !self.options.delay.show) return self.show()

            clearTimeout(this.timeout)
            self.hoverState = 'in'
            this.timeout = setTimeout(function() {
                if (self.hoverState == 'in') self.show()
            }, self.options.delay.show)
        }

        , leave: function (e) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (this.timeout) clearTimeout(this.timeout)
            if (!self.options.delay || !self.options.delay.hide) return self.hide()

            self.hoverState = 'out'
            this.timeout = setTimeout(function() {
                if (self.hoverState == 'out') self.hide()
            }, self.options.delay.hide)
        }

        , show: function () {
            var $tip
                , inside
                , pos
                , actualWidth
                , actualHeight
                , placement
                , tp

            if (this.hasContent() && this.enabled) {
                $tip = this.tip()
                this.setContent()

                if (this.options.animation) {
                    $tip.addClass('fade')
                }

                placement = typeof this.options.placement == 'function' ?
                    this.options.placement.call(this, $tip[0], this.$element[0]) :
                    this.options.placement

                inside = /in/.test(placement)

                $tip
                    .remove()
                    .css({ top: 0, left: 0, display: 'block' })
                    .appendTo(inside ? this.$element : document.body)

                pos = this.getPosition(inside)

                actualWidth = $tip[0].offsetWidth
                actualHeight = $tip[0].offsetHeight

                switch (inside ? placement.split(' ')[1] : placement) {
                    case 'bottom':
                        tp = {top: pos.top + pos.height, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'top':
                        tp = {top: pos.top - actualHeight, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'left':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left - actualWidth}
                        break
                    case 'right':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left + pos.width}
                        break
                }

                $tip
                    .css(tp)
                    .addClass(placement)
                    .addClass('in')
            }
        }

        , isHTML: function(text) {
            // html string detection logic adapted from jQuery
            return typeof text != 'string'
                || ( text.charAt(0) === "<"
                && text.charAt( text.length - 1 ) === ">"
                && text.length >= 3
                ) || /^(?:[^<]*<[\w\W]+>[^>]*$)/.exec(text)
        }

        , setContent: function () {
            var $tip = this.tip()
                , title = this.getTitle()

            $tip.find('.tooltip-inner')[this.isHTML(title) ? 'html' : 'text'](title)
            $tip.removeClass('fade in top bottom left right')
        }

        , hide: function () {
            var that = this
                , $tip = this.tip()

            $tip.removeClass('in')

            function removeWithAnimation() {
                var timeout = setTimeout(function () {
                    $tip.off($.support.transition.end).remove()
                }, 500)

                $tip.one($.support.transition.end, function () {
                    clearTimeout(timeout)
                    $tip.remove()
                })
            }

            $.support.transition && this.$tip.hasClass('fade') ?
                removeWithAnimation() :
                $tip.remove()
        }

        , fixTitle: function () {
            var $e = this.$element
            if ($e.attr('title') || typeof($e.attr('data-original-title')) != 'string') {
                $e.attr('data-original-title', $e.attr('title') || '').removeAttr('title')
            }
        }

        , hasContent: function () {
            return this.getTitle()
        }

        , getPosition: function (inside) {
            return $.extend({}, (inside ? {top: 0, left: 0} : this.$element.offset()), {
                width: this.$element[0].offsetWidth
                , height: this.$element[0].offsetHeight
            })
        }

        , getTitle: function () {
            var title
                , $e = this.$element
                , o = this.options

            title = $e.attr('data-original-title')
                || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title)

            return title
        }

        , tip: function () {
            return this.$tip = this.$tip || $(this.options.template)
        }

        , validate: function () {
            if (!this.$element[0].parentNode) {
                this.hide()
                this.$element = null
                this.options = null
            }
        }

        , enable: function () {
            this.enabled = true
        }

        , disable: function () {
            this.enabled = false
        }

        , toggleEnabled: function () {
            this.enabled = !this.enabled
        }

        , toggle: function () {
            this[this.tip().hasClass('in') ? 'hide' : 'show']()
        }

    }


    /* TOOLTIP PLUGIN DEFINITION
     * ========================= */

    $.fn.tooltip = function ( option ) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('tooltip')
                , options = typeof option == 'object' && option
            if (!data) $this.data('tooltip', (data = new Tooltip(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tooltip.Constructor = Tooltip

    $.fn.tooltip.defaults = {
        animation: true
        , placement: 'top'
        , selector: false
        , template: '<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
        , trigger: 'hover'
        , title: ''
        , delay: 0
    }

}(window.jQuery);

/* ===========================================================
 * bootstrap-popover.js v2.0.4
 * http://twitter.github.com/bootstrap/javascript.html#popovers
 * ===========================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================================================== */


!function ($) {

    "use strict"; // jshint ;_;


    /* POPOVER PUBLIC CLASS DEFINITION
     * =============================== */

    var Popover = function ( element, options ) {
        this.init('popover', element, options)
    }


    /* NOTE: POPOVER EXTENDS BOOTSTRAP-TOOLTIP.js
     ========================================== */

    Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype, {

        constructor: Popover

        , setContent: function () {
            var $tip = this.tip()
                , title = this.getTitle()
                , content = this.getContent()
                , width = this.getWidth()   //UPDATED

            $tip.find('.popover-inner').css({'width':width}) //UPDATED
            $tip.find('.popover-title')[this.isHTML(title) ? 'html' : 'text'](title)
            $tip.find('.popover-content > *')[this.isHTML(content) ? 'html' : 'text'](content)

            $tip.removeClass('fade top bottom left right in')
        }

        , hasContent: function () {
            return this.getTitle() || this.getContent()
        }

        , getContent: function () {
            var content
                , $e = this.$element
                , o = this.options

            content = $e.attr('data-content')
                || (typeof o.content == 'function' ? o.content.call($e[0]) :  o.content)
                || ($e.attr('data-content-id')!='undefined' ? $('#' + $e.attr('data-content-id')).html()  : o.content) //UPDATED

            return content
        }

        //UPDATED-start
        , getWidth: function () {

            var o = this.options

            return o.width

        }
        //UPDATED-end

        , tip: function () {
            if (!this.$tip) {
                this.$tip = $(this.options.template)
            }
            return this.$tip
        }

    })


    /* POPOVER PLUGIN DEFINITION
     * ======================= */

    $.fn.popover = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('popover')
                , options = typeof option == 'object' && option
            if (!data) $this.data('popover', (data = new Popover(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.popover.Constructor = Popover

    $.fn.popover.defaults = $.extend({} , $.fn.tooltip.defaults, {
        placement: 'right'
        , content: ''
        , template: '<div class="popover"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>'
    })

}(window.jQuery);
/* jQuery UI - v1.11.2 - 2014-10-16
* http://jqueryui.com
* Includes: core.js, widget.js, mouse.js, position.js, accordion.js, autocomplete.js, button.js, datepicker.js, dialog.js, draggable.js, droppable.js, effect.js, effect-blind.js, effect-bounce.js, effect-clip.js, effect-drop.js, effect-explode.js, effect-fade.js, effect-fold.js, effect-highlight.js, effect-puff.js, effect-pulsate.js, effect-scale.js, effect-shake.js, effect-size.js, effect-slide.js, effect-transfer.js, menu.js, progressbar.js, resizable.js, selectable.js, selectmenu.js, slider.js, sortable.js, spinner.js, tabs.js, tooltip.js
* Copyright 2014 jQuery Foundation and other contributors; Licensed MIT */

(function(e){"function"==typeof define&&define.amd?define(["jquery"],e):e(jQuery)})(function(e){function t(t,s){var n,a,o,r=t.nodeName.toLowerCase();return"area"===r?(n=t.parentNode,a=n.name,t.href&&a&&"map"===n.nodeName.toLowerCase()?(o=e("img[usemap='#"+a+"']")[0],!!o&&i(o)):!1):(/input|select|textarea|button|object/.test(r)?!t.disabled:"a"===r?t.href||s:s)&&i(t)}function i(t){return e.expr.filters.visible(t)&&!e(t).parents().addBack().filter(function(){return"hidden"===e.css(this,"visibility")}).length}function s(e){for(var t,i;e.length&&e[0]!==document;){if(t=e.css("position"),("absolute"===t||"relative"===t||"fixed"===t)&&(i=parseInt(e.css("zIndex"),10),!isNaN(i)&&0!==i))return i;e=e.parent()}return 0}function n(){this._curInst=null,this._keyEvent=!1,this._disabledInputs=[],this._datepickerShowing=!1,this._inDialog=!1,this._mainDivId="ui-datepicker-div",this._inlineClass="ui-datepicker-inline",this._appendClass="ui-datepicker-append",this._triggerClass="ui-datepicker-trigger",this._dialogClass="ui-datepicker-dialog",this._disableClass="ui-datepicker-disabled",this._unselectableClass="ui-datepicker-unselectable",this._currentClass="ui-datepicker-current-day",this._dayOverClass="ui-datepicker-days-cell-over",this.regional=[],this.regional[""]={closeText:"Done",prevText:"Prev",nextText:"Next",currentText:"Today",monthNames:["January","February","March","April","May","June","July","August","September","October","November","December"],monthNamesShort:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],dayNames:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],dayNamesShort:["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],dayNamesMin:["Su","Mo","Tu","We","Th","Fr","Sa"],weekHeader:"Wk",dateFormat:"mm/dd/yy",firstDay:0,isRTL:!1,showMonthAfterYear:!1,yearSuffix:""},this._defaults={showOn:"focus",showAnim:"fadeIn",showOptions:{},defaultDate:null,appendText:"",buttonText:"...",buttonImage:"",buttonImageOnly:!1,hideIfNoPrevNext:!1,navigationAsDateFormat:!1,gotoCurrent:!1,changeMonth:!1,changeYear:!1,yearRange:"c-10:c+10",showOtherMonths:!1,selectOtherMonths:!1,showWeek:!1,calculateWeek:this.iso8601Week,shortYearCutoff:"+10",minDate:null,maxDate:null,duration:"fast",beforeShowDay:null,beforeShow:null,onSelect:null,onChangeMonthYear:null,onClose:null,numberOfMonths:1,showCurrentAtPos:0,stepMonths:1,stepBigMonths:12,altField:"",altFormat:"",constrainInput:!0,showButtonPanel:!1,autoSize:!1,disabled:!1},e.extend(this._defaults,this.regional[""]),this.regional.en=e.extend(!0,{},this.regional[""]),this.regional["en-US"]=e.extend(!0,{},this.regional.en),this.dpDiv=a(e("<div id='"+this._mainDivId+"' class='ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>"))}function a(t){var i="button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a";return t.delegate(i,"mouseout",function(){e(this).removeClass("ui-state-hover"),-1!==this.className.indexOf("ui-datepicker-prev")&&e(this).removeClass("ui-datepicker-prev-hover"),-1!==this.className.indexOf("ui-datepicker-next")&&e(this).removeClass("ui-datepicker-next-hover")}).delegate(i,"mouseover",o)}function o(){e.datepicker._isDisabledDatepicker(v.inline?v.dpDiv.parent()[0]:v.input[0])||(e(this).parents(".ui-datepicker-calendar").find("a").removeClass("ui-state-hover"),e(this).addClass("ui-state-hover"),-1!==this.className.indexOf("ui-datepicker-prev")&&e(this).addClass("ui-datepicker-prev-hover"),-1!==this.className.indexOf("ui-datepicker-next")&&e(this).addClass("ui-datepicker-next-hover"))}function r(t,i){e.extend(t,i);for(var s in i)null==i[s]&&(t[s]=i[s]);return t}function h(e){return function(){var t=this.element.val();e.apply(this,arguments),this._refresh(),t!==this.element.val()&&this._trigger("change")}}e.ui=e.ui||{},e.extend(e.ui,{version:"1.11.2",keyCode:{BACKSPACE:8,COMMA:188,DELETE:46,DOWN:40,END:35,ENTER:13,ESCAPE:27,HOME:36,LEFT:37,PAGE_DOWN:34,PAGE_UP:33,PERIOD:190,RIGHT:39,SPACE:32,TAB:9,UP:38}}),e.fn.extend({scrollParent:function(t){var i=this.css("position"),s="absolute"===i,n=t?/(auto|scroll|hidden)/:/(auto|scroll)/,a=this.parents().filter(function(){var t=e(this);return s&&"static"===t.css("position")?!1:n.test(t.css("overflow")+t.css("overflow-y")+t.css("overflow-x"))}).eq(0);return"fixed"!==i&&a.length?a:e(this[0].ownerDocument||document)},uniqueId:function(){var e=0;return function(){return this.each(function(){this.id||(this.id="ui-id-"+ ++e)})}}(),removeUniqueId:function(){return this.each(function(){/^ui-id-\d+$/.test(this.id)&&e(this).removeAttr("id")})}}),e.extend(e.expr[":"],{data:e.expr.createPseudo?e.expr.createPseudo(function(t){return function(i){return!!e.data(i,t)}}):function(t,i,s){return!!e.data(t,s[3])},focusable:function(i){return t(i,!isNaN(e.attr(i,"tabindex")))},tabbable:function(i){var s=e.attr(i,"tabindex"),n=isNaN(s);return(n||s>=0)&&t(i,!n)}}),e("<a>").outerWidth(1).jquery||e.each(["Width","Height"],function(t,i){function s(t,i,s,a){return e.each(n,function(){i-=parseFloat(e.css(t,"padding"+this))||0,s&&(i-=parseFloat(e.css(t,"border"+this+"Width"))||0),a&&(i-=parseFloat(e.css(t,"margin"+this))||0)}),i}var n="Width"===i?["Left","Right"]:["Top","Bottom"],a=i.toLowerCase(),o={innerWidth:e.fn.innerWidth,innerHeight:e.fn.innerHeight,outerWidth:e.fn.outerWidth,outerHeight:e.fn.outerHeight};e.fn["inner"+i]=function(t){return void 0===t?o["inner"+i].call(this):this.each(function(){e(this).css(a,s(this,t)+"px")})},e.fn["outer"+i]=function(t,n){return"number"!=typeof t?o["outer"+i].call(this,t):this.each(function(){e(this).css(a,s(this,t,!0,n)+"px")})}}),e.fn.addBack||(e.fn.addBack=function(e){return this.add(null==e?this.prevObject:this.prevObject.filter(e))}),e("<a>").data("a-b","a").removeData("a-b").data("a-b")&&(e.fn.removeData=function(t){return function(i){return arguments.length?t.call(this,e.camelCase(i)):t.call(this)}}(e.fn.removeData)),e.ui.ie=!!/msie [\w.]+/.exec(navigator.userAgent.toLowerCase()),e.fn.extend({focus:function(t){return function(i,s){return"number"==typeof i?this.each(function(){var t=this;setTimeout(function(){e(t).focus(),s&&s.call(t)},i)}):t.apply(this,arguments)}}(e.fn.focus),disableSelection:function(){var e="onselectstart"in document.createElement("div")?"selectstart":"mousedown";return function(){return this.bind(e+".ui-disableSelection",function(e){e.preventDefault()})}}(),enableSelection:function(){return this.unbind(".ui-disableSelection")},zIndex:function(t){if(void 0!==t)return this.css("zIndex",t);if(this.length)for(var i,s,n=e(this[0]);n.length&&n[0]!==document;){if(i=n.css("position"),("absolute"===i||"relative"===i||"fixed"===i)&&(s=parseInt(n.css("zIndex"),10),!isNaN(s)&&0!==s))return s;n=n.parent()}return 0}}),e.ui.plugin={add:function(t,i,s){var n,a=e.ui[t].prototype;for(n in s)a.plugins[n]=a.plugins[n]||[],a.plugins[n].push([i,s[n]])},call:function(e,t,i,s){var n,a=e.plugins[t];if(a&&(s||e.element[0].parentNode&&11!==e.element[0].parentNode.nodeType))for(n=0;a.length>n;n++)e.options[a[n][0]]&&a[n][1].apply(e.element,i)}};var l=0,u=Array.prototype.slice;e.cleanData=function(t){return function(i){var s,n,a;for(a=0;null!=(n=i[a]);a++)try{s=e._data(n,"events"),s&&s.remove&&e(n).triggerHandler("remove")}catch(o){}t(i)}}(e.cleanData),e.widget=function(t,i,s){var n,a,o,r,h={},l=t.split(".")[0];return t=t.split(".")[1],n=l+"-"+t,s||(s=i,i=e.Widget),e.expr[":"][n.toLowerCase()]=function(t){return!!e.data(t,n)},e[l]=e[l]||{},a=e[l][t],o=e[l][t]=function(e,t){return this._createWidget?(arguments.length&&this._createWidget(e,t),void 0):new o(e,t)},e.extend(o,a,{version:s.version,_proto:e.extend({},s),_childConstructors:[]}),r=new i,r.options=e.widget.extend({},r.options),e.each(s,function(t,s){return e.isFunction(s)?(h[t]=function(){var e=function(){return i.prototype[t].apply(this,arguments)},n=function(e){return i.prototype[t].apply(this,e)};return function(){var t,i=this._super,a=this._superApply;return this._super=e,this._superApply=n,t=s.apply(this,arguments),this._super=i,this._superApply=a,t}}(),void 0):(h[t]=s,void 0)}),o.prototype=e.widget.extend(r,{widgetEventPrefix:a?r.widgetEventPrefix||t:t},h,{constructor:o,namespace:l,widgetName:t,widgetFullName:n}),a?(e.each(a._childConstructors,function(t,i){var s=i.prototype;e.widget(s.namespace+"."+s.widgetName,o,i._proto)}),delete a._childConstructors):i._childConstructors.push(o),e.widget.bridge(t,o),o},e.widget.extend=function(t){for(var i,s,n=u.call(arguments,1),a=0,o=n.length;o>a;a++)for(i in n[a])s=n[a][i],n[a].hasOwnProperty(i)&&void 0!==s&&(t[i]=e.isPlainObject(s)?e.isPlainObject(t[i])?e.widget.extend({},t[i],s):e.widget.extend({},s):s);return t},e.widget.bridge=function(t,i){var s=i.prototype.widgetFullName||t;e.fn[t]=function(n){var a="string"==typeof n,o=u.call(arguments,1),r=this;return n=!a&&o.length?e.widget.extend.apply(null,[n].concat(o)):n,a?this.each(function(){var i,a=e.data(this,s);return"instance"===n?(r=a,!1):a?e.isFunction(a[n])&&"_"!==n.charAt(0)?(i=a[n].apply(a,o),i!==a&&void 0!==i?(r=i&&i.jquery?r.pushStack(i.get()):i,!1):void 0):e.error("no such method '"+n+"' for "+t+" widget instance"):e.error("cannot call methods on "+t+" prior to initialization; "+"attempted to call method '"+n+"'")}):this.each(function(){var t=e.data(this,s);t?(t.option(n||{}),t._init&&t._init()):e.data(this,s,new i(n,this))}),r}},e.Widget=function(){},e.Widget._childConstructors=[],e.Widget.prototype={widgetName:"widget",widgetEventPrefix:"",defaultElement:"<div>",options:{disabled:!1,create:null},_createWidget:function(t,i){i=e(i||this.defaultElement||this)[0],this.element=e(i),this.uuid=l++,this.eventNamespace="."+this.widgetName+this.uuid,this.bindings=e(),this.hoverable=e(),this.focusable=e(),i!==this&&(e.data(i,this.widgetFullName,this),this._on(!0,this.element,{remove:function(e){e.target===i&&this.destroy()}}),this.document=e(i.style?i.ownerDocument:i.document||i),this.window=e(this.document[0].defaultView||this.document[0].parentWindow)),this.options=e.widget.extend({},this.options,this._getCreateOptions(),t),this._create(),this._trigger("create",null,this._getCreateEventData()),this._init()},_getCreateOptions:e.noop,_getCreateEventData:e.noop,_create:e.noop,_init:e.noop,destroy:function(){this._destroy(),this.element.unbind(this.eventNamespace).removeData(this.widgetFullName).removeData(e.camelCase(this.widgetFullName)),this.widget().unbind(this.eventNamespace).removeAttr("aria-disabled").removeClass(this.widgetFullName+"-disabled "+"ui-state-disabled"),this.bindings.unbind(this.eventNamespace),this.hoverable.removeClass("ui-state-hover"),this.focusable.removeClass("ui-state-focus")},_destroy:e.noop,widget:function(){return this.element},option:function(t,i){var s,n,a,o=t;if(0===arguments.length)return e.widget.extend({},this.options);if("string"==typeof t)if(o={},s=t.split("."),t=s.shift(),s.length){for(n=o[t]=e.widget.extend({},this.options[t]),a=0;s.length-1>a;a++)n[s[a]]=n[s[a]]||{},n=n[s[a]];if(t=s.pop(),1===arguments.length)return void 0===n[t]?null:n[t];n[t]=i}else{if(1===arguments.length)return void 0===this.options[t]?null:this.options[t];o[t]=i}return this._setOptions(o),this},_setOptions:function(e){var t;for(t in e)this._setOption(t,e[t]);return this},_setOption:function(e,t){return this.options[e]=t,"disabled"===e&&(this.widget().toggleClass(this.widgetFullName+"-disabled",!!t),t&&(this.hoverable.removeClass("ui-state-hover"),this.focusable.removeClass("ui-state-focus"))),this},enable:function(){return this._setOptions({disabled:!1})},disable:function(){return this._setOptions({disabled:!0})},_on:function(t,i,s){var n,a=this;"boolean"!=typeof t&&(s=i,i=t,t=!1),s?(i=n=e(i),this.bindings=this.bindings.add(i)):(s=i,i=this.element,n=this.widget()),e.each(s,function(s,o){function r(){return t||a.options.disabled!==!0&&!e(this).hasClass("ui-state-disabled")?("string"==typeof o?a[o]:o).apply(a,arguments):void 0}"string"!=typeof o&&(r.guid=o.guid=o.guid||r.guid||e.guid++);var h=s.match(/^([\w:-]*)\s*(.*)$/),l=h[1]+a.eventNamespace,u=h[2];u?n.delegate(u,l,r):i.bind(l,r)})},_off:function(t,i){i=(i||"").split(" ").join(this.eventNamespace+" ")+this.eventNamespace,t.unbind(i).undelegate(i),this.bindings=e(this.bindings.not(t).get()),this.focusable=e(this.focusable.not(t).get()),this.hoverable=e(this.hoverable.not(t).get())},_delay:function(e,t){function i(){return("string"==typeof e?s[e]:e).apply(s,arguments)}var s=this;return setTimeout(i,t||0)},_hoverable:function(t){this.hoverable=this.hoverable.add(t),this._on(t,{mouseenter:function(t){e(t.currentTarget).addClass("ui-state-hover")},mouseleave:function(t){e(t.currentTarget).removeClass("ui-state-hover")}})},_focusable:function(t){this.focusable=this.focusable.add(t),this._on(t,{focusin:function(t){e(t.currentTarget).addClass("ui-state-focus")},focusout:function(t){e(t.currentTarget).removeClass("ui-state-focus")}})},_trigger:function(t,i,s){var n,a,o=this.options[t];if(s=s||{},i=e.Event(i),i.type=(t===this.widgetEventPrefix?t:this.widgetEventPrefix+t).toLowerCase(),i.target=this.element[0],a=i.originalEvent)for(n in a)n in i||(i[n]=a[n]);return this.element.trigger(i,s),!(e.isFunction(o)&&o.apply(this.element[0],[i].concat(s))===!1||i.isDefaultPrevented())}},e.each({show:"fadeIn",hide:"fadeOut"},function(t,i){e.Widget.prototype["_"+t]=function(s,n,a){"string"==typeof n&&(n={effect:n});var o,r=n?n===!0||"number"==typeof n?i:n.effect||i:t;n=n||{},"number"==typeof n&&(n={duration:n}),o=!e.isEmptyObject(n),n.complete=a,n.delay&&s.delay(n.delay),o&&e.effects&&e.effects.effect[r]?s[t](n):r!==t&&s[r]?s[r](n.duration,n.easing,a):s.queue(function(i){e(this)[t](),a&&a.call(s[0]),i()})}}),e.widget;var d=!1;e(document).mouseup(function(){d=!1}),e.widget("ui.mouse",{version:"1.11.2",options:{cancel:"input,textarea,button,select,option",distance:1,delay:0},_mouseInit:function(){var t=this;this.element.bind("mousedown."+this.widgetName,function(e){return t._mouseDown(e)}).bind("click."+this.widgetName,function(i){return!0===e.data(i.target,t.widgetName+".preventClickEvent")?(e.removeData(i.target,t.widgetName+".preventClickEvent"),i.stopImmediatePropagation(),!1):void 0}),this.started=!1},_mouseDestroy:function(){this.element.unbind("."+this.widgetName),this._mouseMoveDelegate&&this.document.unbind("mousemove."+this.widgetName,this._mouseMoveDelegate).unbind("mouseup."+this.widgetName,this._mouseUpDelegate)},_mouseDown:function(t){if(!d){this._mouseMoved=!1,this._mouseStarted&&this._mouseUp(t),this._mouseDownEvent=t;var i=this,s=1===t.which,n="string"==typeof this.options.cancel&&t.target.nodeName?e(t.target).closest(this.options.cancel).length:!1;return s&&!n&&this._mouseCapture(t)?(this.mouseDelayMet=!this.options.delay,this.mouseDelayMet||(this._mouseDelayTimer=setTimeout(function(){i.mouseDelayMet=!0},this.options.delay)),this._mouseDistanceMet(t)&&this._mouseDelayMet(t)&&(this._mouseStarted=this._mouseStart(t)!==!1,!this._mouseStarted)?(t.preventDefault(),!0):(!0===e.data(t.target,this.widgetName+".preventClickEvent")&&e.removeData(t.target,this.widgetName+".preventClickEvent"),this._mouseMoveDelegate=function(e){return i._mouseMove(e)},this._mouseUpDelegate=function(e){return i._mouseUp(e)},this.document.bind("mousemove."+this.widgetName,this._mouseMoveDelegate).bind("mouseup."+this.widgetName,this._mouseUpDelegate),t.preventDefault(),d=!0,!0)):!0}},_mouseMove:function(t){if(this._mouseMoved){if(e.ui.ie&&(!document.documentMode||9>document.documentMode)&&!t.button)return this._mouseUp(t);if(!t.which)return this._mouseUp(t)}return(t.which||t.button)&&(this._mouseMoved=!0),this._mouseStarted?(this._mouseDrag(t),t.preventDefault()):(this._mouseDistanceMet(t)&&this._mouseDelayMet(t)&&(this._mouseStarted=this._mouseStart(this._mouseDownEvent,t)!==!1,this._mouseStarted?this._mouseDrag(t):this._mouseUp(t)),!this._mouseStarted)},_mouseUp:function(t){return this.document.unbind("mousemove."+this.widgetName,this._mouseMoveDelegate).unbind("mouseup."+this.widgetName,this._mouseUpDelegate),this._mouseStarted&&(this._mouseStarted=!1,t.target===this._mouseDownEvent.target&&e.data(t.target,this.widgetName+".preventClickEvent",!0),this._mouseStop(t)),d=!1,!1},_mouseDistanceMet:function(e){return Math.max(Math.abs(this._mouseDownEvent.pageX-e.pageX),Math.abs(this._mouseDownEvent.pageY-e.pageY))>=this.options.distance},_mouseDelayMet:function(){return this.mouseDelayMet},_mouseStart:function(){},_mouseDrag:function(){},_mouseStop:function(){},_mouseCapture:function(){return!0}}),function(){function t(e,t,i){return[parseFloat(e[0])*(p.test(e[0])?t/100:1),parseFloat(e[1])*(p.test(e[1])?i/100:1)]}function i(t,i){return parseInt(e.css(t,i),10)||0}function s(t){var i=t[0];return 9===i.nodeType?{width:t.width(),height:t.height(),offset:{top:0,left:0}}:e.isWindow(i)?{width:t.width(),height:t.height(),offset:{top:t.scrollTop(),left:t.scrollLeft()}}:i.preventDefault?{width:0,height:0,offset:{top:i.pageY,left:i.pageX}}:{width:t.outerWidth(),height:t.outerHeight(),offset:t.offset()}}e.ui=e.ui||{};var n,a,o=Math.max,r=Math.abs,h=Math.round,l=/left|center|right/,u=/top|center|bottom/,d=/[\+\-]\d+(\.[\d]+)?%?/,c=/^\w+/,p=/%$/,f=e.fn.position;e.position={scrollbarWidth:function(){if(void 0!==n)return n;var t,i,s=e("<div style='display:block;position:absolute;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>"),a=s.children()[0];return e("body").append(s),t=a.offsetWidth,s.css("overflow","scroll"),i=a.offsetWidth,t===i&&(i=s[0].clientWidth),s.remove(),n=t-i},getScrollInfo:function(t){var i=t.isWindow||t.isDocument?"":t.element.css("overflow-x"),s=t.isWindow||t.isDocument?"":t.element.css("overflow-y"),n="scroll"===i||"auto"===i&&t.width<t.element[0].scrollWidth,a="scroll"===s||"auto"===s&&t.height<t.element[0].scrollHeight;return{width:a?e.position.scrollbarWidth():0,height:n?e.position.scrollbarWidth():0}},getWithinInfo:function(t){var i=e(t||window),s=e.isWindow(i[0]),n=!!i[0]&&9===i[0].nodeType;return{element:i,isWindow:s,isDocument:n,offset:i.offset()||{left:0,top:0},scrollLeft:i.scrollLeft(),scrollTop:i.scrollTop(),width:s||n?i.width():i.outerWidth(),height:s||n?i.height():i.outerHeight()}}},e.fn.position=function(n){if(!n||!n.of)return f.apply(this,arguments);n=e.extend({},n);var p,m,g,v,y,b,_=e(n.of),x=e.position.getWithinInfo(n.within),w=e.position.getScrollInfo(x),k=(n.collision||"flip").split(" "),T={};return b=s(_),_[0].preventDefault&&(n.at="left top"),m=b.width,g=b.height,v=b.offset,y=e.extend({},v),e.each(["my","at"],function(){var e,t,i=(n[this]||"").split(" ");1===i.length&&(i=l.test(i[0])?i.concat(["center"]):u.test(i[0])?["center"].concat(i):["center","center"]),i[0]=l.test(i[0])?i[0]:"center",i[1]=u.test(i[1])?i[1]:"center",e=d.exec(i[0]),t=d.exec(i[1]),T[this]=[e?e[0]:0,t?t[0]:0],n[this]=[c.exec(i[0])[0],c.exec(i[1])[0]]}),1===k.length&&(k[1]=k[0]),"right"===n.at[0]?y.left+=m:"center"===n.at[0]&&(y.left+=m/2),"bottom"===n.at[1]?y.top+=g:"center"===n.at[1]&&(y.top+=g/2),p=t(T.at,m,g),y.left+=p[0],y.top+=p[1],this.each(function(){var s,l,u=e(this),d=u.outerWidth(),c=u.outerHeight(),f=i(this,"marginLeft"),b=i(this,"marginTop"),D=d+f+i(this,"marginRight")+w.width,S=c+b+i(this,"marginBottom")+w.height,M=e.extend({},y),C=t(T.my,u.outerWidth(),u.outerHeight());"right"===n.my[0]?M.left-=d:"center"===n.my[0]&&(M.left-=d/2),"bottom"===n.my[1]?M.top-=c:"center"===n.my[1]&&(M.top-=c/2),M.left+=C[0],M.top+=C[1],a||(M.left=h(M.left),M.top=h(M.top)),s={marginLeft:f,marginTop:b},e.each(["left","top"],function(t,i){e.ui.position[k[t]]&&e.ui.position[k[t]][i](M,{targetWidth:m,targetHeight:g,elemWidth:d,elemHeight:c,collisionPosition:s,collisionWidth:D,collisionHeight:S,offset:[p[0]+C[0],p[1]+C[1]],my:n.my,at:n.at,within:x,elem:u})}),n.using&&(l=function(e){var t=v.left-M.left,i=t+m-d,s=v.top-M.top,a=s+g-c,h={target:{element:_,left:v.left,top:v.top,width:m,height:g},element:{element:u,left:M.left,top:M.top,width:d,height:c},horizontal:0>i?"left":t>0?"right":"center",vertical:0>a?"top":s>0?"bottom":"middle"};d>m&&m>r(t+i)&&(h.horizontal="center"),c>g&&g>r(s+a)&&(h.vertical="middle"),h.important=o(r(t),r(i))>o(r(s),r(a))?"horizontal":"vertical",n.using.call(this,e,h)}),u.offset(e.extend(M,{using:l}))})},e.ui.position={fit:{left:function(e,t){var i,s=t.within,n=s.isWindow?s.scrollLeft:s.offset.left,a=s.width,r=e.left-t.collisionPosition.marginLeft,h=n-r,l=r+t.collisionWidth-a-n;t.collisionWidth>a?h>0&&0>=l?(i=e.left+h+t.collisionWidth-a-n,e.left+=h-i):e.left=l>0&&0>=h?n:h>l?n+a-t.collisionWidth:n:h>0?e.left+=h:l>0?e.left-=l:e.left=o(e.left-r,e.left)},top:function(e,t){var i,s=t.within,n=s.isWindow?s.scrollTop:s.offset.top,a=t.within.height,r=e.top-t.collisionPosition.marginTop,h=n-r,l=r+t.collisionHeight-a-n;t.collisionHeight>a?h>0&&0>=l?(i=e.top+h+t.collisionHeight-a-n,e.top+=h-i):e.top=l>0&&0>=h?n:h>l?n+a-t.collisionHeight:n:h>0?e.top+=h:l>0?e.top-=l:e.top=o(e.top-r,e.top)}},flip:{left:function(e,t){var i,s,n=t.within,a=n.offset.left+n.scrollLeft,o=n.width,h=n.isWindow?n.scrollLeft:n.offset.left,l=e.left-t.collisionPosition.marginLeft,u=l-h,d=l+t.collisionWidth-o-h,c="left"===t.my[0]?-t.elemWidth:"right"===t.my[0]?t.elemWidth:0,p="left"===t.at[0]?t.targetWidth:"right"===t.at[0]?-t.targetWidth:0,f=-2*t.offset[0];0>u?(i=e.left+c+p+f+t.collisionWidth-o-a,(0>i||r(u)>i)&&(e.left+=c+p+f)):d>0&&(s=e.left-t.collisionPosition.marginLeft+c+p+f-h,(s>0||d>r(s))&&(e.left+=c+p+f))},top:function(e,t){var i,s,n=t.within,a=n.offset.top+n.scrollTop,o=n.height,h=n.isWindow?n.scrollTop:n.offset.top,l=e.top-t.collisionPosition.marginTop,u=l-h,d=l+t.collisionHeight-o-h,c="top"===t.my[1],p=c?-t.elemHeight:"bottom"===t.my[1]?t.elemHeight:0,f="top"===t.at[1]?t.targetHeight:"bottom"===t.at[1]?-t.targetHeight:0,m=-2*t.offset[1];0>u?(s=e.top+p+f+m+t.collisionHeight-o-a,e.top+p+f+m>u&&(0>s||r(u)>s)&&(e.top+=p+f+m)):d>0&&(i=e.top-t.collisionPosition.marginTop+p+f+m-h,e.top+p+f+m>d&&(i>0||d>r(i))&&(e.top+=p+f+m))}},flipfit:{left:function(){e.ui.position.flip.left.apply(this,arguments),e.ui.position.fit.left.apply(this,arguments)},top:function(){e.ui.position.flip.top.apply(this,arguments),e.ui.position.fit.top.apply(this,arguments)}}},function(){var t,i,s,n,o,r=document.getElementsByTagName("body")[0],h=document.createElement("div");t=document.createElement(r?"div":"body"),s={visibility:"hidden",width:0,height:0,border:0,margin:0,background:"none"},r&&e.extend(s,{position:"absolute",left:"-1000px",top:"-1000px"});for(o in s)t.style[o]=s[o];t.appendChild(h),i=r||document.documentElement,i.insertBefore(t,i.firstChild),h.style.cssText="position: absolute; left: 10.7432222px;",n=e(h).offset().left,a=n>10&&11>n,t.innerHTML="",i.removeChild(t)}()}(),e.ui.position,e.widget("ui.accordion",{version:"1.11.2",options:{active:0,animate:{},collapsible:!1,event:"click",header:"> li > :first-child,> :not(li):even",heightStyle:"auto",icons:{activeHeader:"ui-icon-triangle-1-s",header:"ui-icon-triangle-1-e"},activate:null,beforeActivate:null},hideProps:{borderTopWidth:"hide",borderBottomWidth:"hide",paddingTop:"hide",paddingBottom:"hide",height:"hide"},showProps:{borderTopWidth:"show",borderBottomWidth:"show",paddingTop:"show",paddingBottom:"show",height:"show"},_create:function(){var t=this.options;this.prevShow=this.prevHide=e(),this.element.addClass("ui-accordion ui-widget ui-helper-reset").attr("role","tablist"),t.collapsible||t.active!==!1&&null!=t.active||(t.active=0),this._processPanels(),0>t.active&&(t.active+=this.headers.length),this._refresh()},_getCreateEventData:function(){return{header:this.active,panel:this.active.length?this.active.next():e()}},_createIcons:function(){var t=this.options.icons;t&&(e("<span>").addClass("ui-accordion-header-icon ui-icon "+t.header).prependTo(this.headers),this.active.children(".ui-accordion-header-icon").removeClass(t.header).addClass(t.activeHeader),this.headers.addClass("ui-accordion-icons"))},_destroyIcons:function(){this.headers.removeClass("ui-accordion-icons").children(".ui-accordion-header-icon").remove()},_destroy:function(){var e;this.element.removeClass("ui-accordion ui-widget ui-helper-reset").removeAttr("role"),this.headers.removeClass("ui-accordion-header ui-accordion-header-active ui-state-default ui-corner-all ui-state-active ui-state-disabled ui-corner-top").removeAttr("role").removeAttr("aria-expanded").removeAttr("aria-selected").removeAttr("aria-controls").removeAttr("tabIndex").removeUniqueId(),this._destroyIcons(),e=this.headers.next().removeClass("ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content ui-accordion-content-active ui-state-disabled").css("display","").removeAttr("role").removeAttr("aria-hidden").removeAttr("aria-labelledby").removeUniqueId(),"content"!==this.options.heightStyle&&e.css("height","")},_setOption:function(e,t){return"active"===e?(this._activate(t),void 0):("event"===e&&(this.options.event&&this._off(this.headers,this.options.event),this._setupEvents(t)),this._super(e,t),"collapsible"!==e||t||this.options.active!==!1||this._activate(0),"icons"===e&&(this._destroyIcons(),t&&this._createIcons()),"disabled"===e&&(this.element.toggleClass("ui-state-disabled",!!t).attr("aria-disabled",t),this.headers.add(this.headers.next()).toggleClass("ui-state-disabled",!!t)),void 0)},_keydown:function(t){if(!t.altKey&&!t.ctrlKey){var i=e.ui.keyCode,s=this.headers.length,n=this.headers.index(t.target),a=!1;switch(t.keyCode){case i.RIGHT:case i.DOWN:a=this.headers[(n+1)%s];break;case i.LEFT:case i.UP:a=this.headers[(n-1+s)%s];break;case i.SPACE:case i.ENTER:this._eventHandler(t);break;case i.HOME:a=this.headers[0];break;case i.END:a=this.headers[s-1]}a&&(e(t.target).attr("tabIndex",-1),e(a).attr("tabIndex",0),a.focus(),t.preventDefault())}},_panelKeyDown:function(t){t.keyCode===e.ui.keyCode.UP&&t.ctrlKey&&e(t.currentTarget).prev().focus()},refresh:function(){var t=this.options;this._processPanels(),t.active===!1&&t.collapsible===!0||!this.headers.length?(t.active=!1,this.active=e()):t.active===!1?this._activate(0):this.active.length&&!e.contains(this.element[0],this.active[0])?this.headers.length===this.headers.find(".ui-state-disabled").length?(t.active=!1,this.active=e()):this._activate(Math.max(0,t.active-1)):t.active=this.headers.index(this.active),this._destroyIcons(),this._refresh()},_processPanels:function(){var e=this.headers,t=this.panels;this.headers=this.element.find(this.options.header).addClass("ui-accordion-header ui-state-default ui-corner-all"),this.panels=this.headers.next().addClass("ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom").filter(":not(.ui-accordion-content-active)").hide(),t&&(this._off(e.not(this.headers)),this._off(t.not(this.panels)))},_refresh:function(){var t,i=this.options,s=i.heightStyle,n=this.element.parent();this.active=this._findActive(i.active).addClass("ui-accordion-header-active ui-state-active ui-corner-top").removeClass("ui-corner-all"),this.active.next().addClass("ui-accordion-content-active").show(),this.headers.attr("role","tab").each(function(){var t=e(this),i=t.uniqueId().attr("id"),s=t.next(),n=s.uniqueId().attr("id");t.attr("aria-controls",n),s.attr("aria-labelledby",i)}).next().attr("role","tabpanel"),this.headers.not(this.active).attr({"aria-selected":"false","aria-expanded":"false",tabIndex:-1}).next().attr({"aria-hidden":"true"}).hide(),this.active.length?this.active.attr({"aria-selected":"true","aria-expanded":"true",tabIndex:0}).next().attr({"aria-hidden":"false"}):this.headers.eq(0).attr("tabIndex",0),this._createIcons(),this._setupEvents(i.event),"fill"===s?(t=n.height(),this.element.siblings(":visible").each(function(){var i=e(this),s=i.css("position");"absolute"!==s&&"fixed"!==s&&(t-=i.outerHeight(!0))}),this.headers.each(function(){t-=e(this).outerHeight(!0)}),this.headers.next().each(function(){e(this).height(Math.max(0,t-e(this).innerHeight()+e(this).height()))}).css("overflow","auto")):"auto"===s&&(t=0,this.headers.next().each(function(){t=Math.max(t,e(this).css("height","").height())}).height(t))},_activate:function(t){var i=this._findActive(t)[0];i!==this.active[0]&&(i=i||this.active[0],this._eventHandler({target:i,currentTarget:i,preventDefault:e.noop}))},_findActive:function(t){return"number"==typeof t?this.headers.eq(t):e()},_setupEvents:function(t){var i={keydown:"_keydown"};t&&e.each(t.split(" "),function(e,t){i[t]="_eventHandler"}),this._off(this.headers.add(this.headers.next())),this._on(this.headers,i),this._on(this.headers.next(),{keydown:"_panelKeyDown"}),this._hoverable(this.headers),this._focusable(this.headers)},_eventHandler:function(t){var i=this.options,s=this.active,n=e(t.currentTarget),a=n[0]===s[0],o=a&&i.collapsible,r=o?e():n.next(),h=s.next(),l={oldHeader:s,oldPanel:h,newHeader:o?e():n,newPanel:r};t.preventDefault(),a&&!i.collapsible||this._trigger("beforeActivate",t,l)===!1||(i.active=o?!1:this.headers.index(n),this.active=a?e():n,this._toggle(l),s.removeClass("ui-accordion-header-active ui-state-active"),i.icons&&s.children(".ui-accordion-header-icon").removeClass(i.icons.activeHeader).addClass(i.icons.header),a||(n.removeClass("ui-corner-all").addClass("ui-accordion-header-active ui-state-active ui-corner-top"),i.icons&&n.children(".ui-accordion-header-icon").removeClass(i.icons.header).addClass(i.icons.activeHeader),n.next().addClass("ui-accordion-content-active")))},_toggle:function(t){var i=t.newPanel,s=this.prevShow.length?this.prevShow:t.oldPanel;this.prevShow.add(this.prevHide).stop(!0,!0),this.prevShow=i,this.prevHide=s,this.options.animate?this._animate(i,s,t):(s.hide(),i.show(),this._toggleComplete(t)),s.attr({"aria-hidden":"true"}),s.prev().attr("aria-selected","false"),i.length&&s.length?s.prev().attr({tabIndex:-1,"aria-expanded":"false"}):i.length&&this.headers.filter(function(){return 0===e(this).attr("tabIndex")}).attr("tabIndex",-1),i.attr("aria-hidden","false").prev().attr({"aria-selected":"true",tabIndex:0,"aria-expanded":"true"})},_animate:function(e,t,i){var s,n,a,o=this,r=0,h=e.length&&(!t.length||e.index()<t.index()),l=this.options.animate||{},u=h&&l.down||l,d=function(){o._toggleComplete(i)};return"number"==typeof u&&(a=u),"string"==typeof u&&(n=u),n=n||u.easing||l.easing,a=a||u.duration||l.duration,t.length?e.length?(s=e.show().outerHeight(),t.animate(this.hideProps,{duration:a,easing:n,step:function(e,t){t.now=Math.round(e)}}),e.hide().animate(this.showProps,{duration:a,easing:n,complete:d,step:function(e,i){i.now=Math.round(e),"height"!==i.prop?r+=i.now:"content"!==o.options.heightStyle&&(i.now=Math.round(s-t.outerHeight()-r),r=0)}}),void 0):t.animate(this.hideProps,a,n,d):e.animate(this.showProps,a,n,d)},_toggleComplete:function(e){var t=e.oldPanel;t.removeClass("ui-accordion-content-active").prev().removeClass("ui-corner-top").addClass("ui-corner-all"),t.length&&(t.parent()[0].className=t.parent()[0].className),this._trigger("activate",null,e)}}),e.widget("ui.menu",{version:"1.11.2",defaultElement:"<ul>",delay:300,options:{icons:{submenu:"ui-icon-carat-1-e"},items:"> *",menus:"ul",position:{my:"left-1 top",at:"right top"},role:"menu",blur:null,focus:null,select:null},_create:function(){this.activeMenu=this.element,this.mouseHandled=!1,this.element.uniqueId().addClass("ui-menu ui-widget ui-widget-content").toggleClass("ui-menu-icons",!!this.element.find(".ui-icon").length).attr({role:this.options.role,tabIndex:0}),this.options.disabled&&this.element.addClass("ui-state-disabled").attr("aria-disabled","true"),this._on({"mousedown .ui-menu-item":function(e){e.preventDefault()},"click .ui-menu-item":function(t){var i=e(t.target);!this.mouseHandled&&i.not(".ui-state-disabled").length&&(this.select(t),t.isPropagationStopped()||(this.mouseHandled=!0),i.has(".ui-menu").length?this.expand(t):!this.element.is(":focus")&&e(this.document[0].activeElement).closest(".ui-menu").length&&(this.element.trigger("focus",[!0]),this.active&&1===this.active.parents(".ui-menu").length&&clearTimeout(this.timer)))},"mouseenter .ui-menu-item":function(t){if(!this.previousFilter){var i=e(t.currentTarget);i.siblings(".ui-state-active").removeClass("ui-state-active"),this.focus(t,i)
}},mouseleave:"collapseAll","mouseleave .ui-menu":"collapseAll",focus:function(e,t){var i=this.active||this.element.find(this.options.items).eq(0);t||this.focus(e,i)},blur:function(t){this._delay(function(){e.contains(this.element[0],this.document[0].activeElement)||this.collapseAll(t)})},keydown:"_keydown"}),this.refresh(),this._on(this.document,{click:function(e){this._closeOnDocumentClick(e)&&this.collapseAll(e),this.mouseHandled=!1}})},_destroy:function(){this.element.removeAttr("aria-activedescendant").find(".ui-menu").addBack().removeClass("ui-menu ui-widget ui-widget-content ui-menu-icons ui-front").removeAttr("role").removeAttr("tabIndex").removeAttr("aria-labelledby").removeAttr("aria-expanded").removeAttr("aria-hidden").removeAttr("aria-disabled").removeUniqueId().show(),this.element.find(".ui-menu-item").removeClass("ui-menu-item").removeAttr("role").removeAttr("aria-disabled").removeUniqueId().removeClass("ui-state-hover").removeAttr("tabIndex").removeAttr("role").removeAttr("aria-haspopup").children().each(function(){var t=e(this);t.data("ui-menu-submenu-carat")&&t.remove()}),this.element.find(".ui-menu-divider").removeClass("ui-menu-divider ui-widget-content")},_keydown:function(t){var i,s,n,a,o=!0;switch(t.keyCode){case e.ui.keyCode.PAGE_UP:this.previousPage(t);break;case e.ui.keyCode.PAGE_DOWN:this.nextPage(t);break;case e.ui.keyCode.HOME:this._move("first","first",t);break;case e.ui.keyCode.END:this._move("last","last",t);break;case e.ui.keyCode.UP:this.previous(t);break;case e.ui.keyCode.DOWN:this.next(t);break;case e.ui.keyCode.LEFT:this.collapse(t);break;case e.ui.keyCode.RIGHT:this.active&&!this.active.is(".ui-state-disabled")&&this.expand(t);break;case e.ui.keyCode.ENTER:case e.ui.keyCode.SPACE:this._activate(t);break;case e.ui.keyCode.ESCAPE:this.collapse(t);break;default:o=!1,s=this.previousFilter||"",n=String.fromCharCode(t.keyCode),a=!1,clearTimeout(this.filterTimer),n===s?a=!0:n=s+n,i=this._filterMenuItems(n),i=a&&-1!==i.index(this.active.next())?this.active.nextAll(".ui-menu-item"):i,i.length||(n=String.fromCharCode(t.keyCode),i=this._filterMenuItems(n)),i.length?(this.focus(t,i),this.previousFilter=n,this.filterTimer=this._delay(function(){delete this.previousFilter},1e3)):delete this.previousFilter}o&&t.preventDefault()},_activate:function(e){this.active.is(".ui-state-disabled")||(this.active.is("[aria-haspopup='true']")?this.expand(e):this.select(e))},refresh:function(){var t,i,s=this,n=this.options.icons.submenu,a=this.element.find(this.options.menus);this.element.toggleClass("ui-menu-icons",!!this.element.find(".ui-icon").length),a.filter(":not(.ui-menu)").addClass("ui-menu ui-widget ui-widget-content ui-front").hide().attr({role:this.options.role,"aria-hidden":"true","aria-expanded":"false"}).each(function(){var t=e(this),i=t.parent(),s=e("<span>").addClass("ui-menu-icon ui-icon "+n).data("ui-menu-submenu-carat",!0);i.attr("aria-haspopup","true").prepend(s),t.attr("aria-labelledby",i.attr("id"))}),t=a.add(this.element),i=t.find(this.options.items),i.not(".ui-menu-item").each(function(){var t=e(this);s._isDivider(t)&&t.addClass("ui-widget-content ui-menu-divider")}),i.not(".ui-menu-item, .ui-menu-divider").addClass("ui-menu-item").uniqueId().attr({tabIndex:-1,role:this._itemRole()}),i.filter(".ui-state-disabled").attr("aria-disabled","true"),this.active&&!e.contains(this.element[0],this.active[0])&&this.blur()},_itemRole:function(){return{menu:"menuitem",listbox:"option"}[this.options.role]},_setOption:function(e,t){"icons"===e&&this.element.find(".ui-menu-icon").removeClass(this.options.icons.submenu).addClass(t.submenu),"disabled"===e&&this.element.toggleClass("ui-state-disabled",!!t).attr("aria-disabled",t),this._super(e,t)},focus:function(e,t){var i,s;this.blur(e,e&&"focus"===e.type),this._scrollIntoView(t),this.active=t.first(),s=this.active.addClass("ui-state-focus").removeClass("ui-state-active"),this.options.role&&this.element.attr("aria-activedescendant",s.attr("id")),this.active.parent().closest(".ui-menu-item").addClass("ui-state-active"),e&&"keydown"===e.type?this._close():this.timer=this._delay(function(){this._close()},this.delay),i=t.children(".ui-menu"),i.length&&e&&/^mouse/.test(e.type)&&this._startOpening(i),this.activeMenu=t.parent(),this._trigger("focus",e,{item:t})},_scrollIntoView:function(t){var i,s,n,a,o,r;this._hasScroll()&&(i=parseFloat(e.css(this.activeMenu[0],"borderTopWidth"))||0,s=parseFloat(e.css(this.activeMenu[0],"paddingTop"))||0,n=t.offset().top-this.activeMenu.offset().top-i-s,a=this.activeMenu.scrollTop(),o=this.activeMenu.height(),r=t.outerHeight(),0>n?this.activeMenu.scrollTop(a+n):n+r>o&&this.activeMenu.scrollTop(a+n-o+r))},blur:function(e,t){t||clearTimeout(this.timer),this.active&&(this.active.removeClass("ui-state-focus"),this.active=null,this._trigger("blur",e,{item:this.active}))},_startOpening:function(e){clearTimeout(this.timer),"true"===e.attr("aria-hidden")&&(this.timer=this._delay(function(){this._close(),this._open(e)},this.delay))},_open:function(t){var i=e.extend({of:this.active},this.options.position);clearTimeout(this.timer),this.element.find(".ui-menu").not(t.parents(".ui-menu")).hide().attr("aria-hidden","true"),t.show().removeAttr("aria-hidden").attr("aria-expanded","true").position(i)},collapseAll:function(t,i){clearTimeout(this.timer),this.timer=this._delay(function(){var s=i?this.element:e(t&&t.target).closest(this.element.find(".ui-menu"));s.length||(s=this.element),this._close(s),this.blur(t),this.activeMenu=s},this.delay)},_close:function(e){e||(e=this.active?this.active.parent():this.element),e.find(".ui-menu").hide().attr("aria-hidden","true").attr("aria-expanded","false").end().find(".ui-state-active").not(".ui-state-focus").removeClass("ui-state-active")},_closeOnDocumentClick:function(t){return!e(t.target).closest(".ui-menu").length},_isDivider:function(e){return!/[^\-\u2014\u2013\s]/.test(e.text())},collapse:function(e){var t=this.active&&this.active.parent().closest(".ui-menu-item",this.element);t&&t.length&&(this._close(),this.focus(e,t))},expand:function(e){var t=this.active&&this.active.children(".ui-menu ").find(this.options.items).first();t&&t.length&&(this._open(t.parent()),this._delay(function(){this.focus(e,t)}))},next:function(e){this._move("next","first",e)},previous:function(e){this._move("prev","last",e)},isFirstItem:function(){return this.active&&!this.active.prevAll(".ui-menu-item").length},isLastItem:function(){return this.active&&!this.active.nextAll(".ui-menu-item").length},_move:function(e,t,i){var s;this.active&&(s="first"===e||"last"===e?this.active["first"===e?"prevAll":"nextAll"](".ui-menu-item").eq(-1):this.active[e+"All"](".ui-menu-item").eq(0)),s&&s.length&&this.active||(s=this.activeMenu.find(this.options.items)[t]()),this.focus(i,s)},nextPage:function(t){var i,s,n;return this.active?(this.isLastItem()||(this._hasScroll()?(s=this.active.offset().top,n=this.element.height(),this.active.nextAll(".ui-menu-item").each(function(){return i=e(this),0>i.offset().top-s-n}),this.focus(t,i)):this.focus(t,this.activeMenu.find(this.options.items)[this.active?"last":"first"]())),void 0):(this.next(t),void 0)},previousPage:function(t){var i,s,n;return this.active?(this.isFirstItem()||(this._hasScroll()?(s=this.active.offset().top,n=this.element.height(),this.active.prevAll(".ui-menu-item").each(function(){return i=e(this),i.offset().top-s+n>0}),this.focus(t,i)):this.focus(t,this.activeMenu.find(this.options.items).first())),void 0):(this.next(t),void 0)},_hasScroll:function(){return this.element.outerHeight()<this.element.prop("scrollHeight")},select:function(t){this.active=this.active||e(t.target).closest(".ui-menu-item");var i={item:this.active};this.active.has(".ui-menu").length||this.collapseAll(t,!0),this._trigger("select",t,i)},_filterMenuItems:function(t){var i=t.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g,"\\$&"),s=RegExp("^"+i,"i");return this.activeMenu.find(this.options.items).filter(".ui-menu-item").filter(function(){return s.test(e.trim(e(this).text()))})}}),e.widget("ui.autocomplete",{version:"1.11.2",defaultElement:"<input>",options:{appendTo:null,autoFocus:!1,delay:300,minLength:1,position:{my:"left top",at:"left bottom",collision:"none"},source:null,change:null,close:null,focus:null,open:null,response:null,search:null,select:null},requestIndex:0,pending:0,_create:function(){var t,i,s,n=this.element[0].nodeName.toLowerCase(),a="textarea"===n,o="input"===n;this.isMultiLine=a?!0:o?!1:this.element.prop("isContentEditable"),this.valueMethod=this.element[a||o?"val":"text"],this.isNewMenu=!0,this.element.addClass("ui-autocomplete-input").attr("autocomplete","off"),this._on(this.element,{keydown:function(n){if(this.element.prop("readOnly"))return t=!0,s=!0,i=!0,void 0;t=!1,s=!1,i=!1;var a=e.ui.keyCode;switch(n.keyCode){case a.PAGE_UP:t=!0,this._move("previousPage",n);break;case a.PAGE_DOWN:t=!0,this._move("nextPage",n);break;case a.UP:t=!0,this._keyEvent("previous",n);break;case a.DOWN:t=!0,this._keyEvent("next",n);break;case a.ENTER:this.menu.active&&(t=!0,n.preventDefault(),this.menu.select(n));break;case a.TAB:this.menu.active&&this.menu.select(n);break;case a.ESCAPE:this.menu.element.is(":visible")&&(this.isMultiLine||this._value(this.term),this.close(n),n.preventDefault());break;default:i=!0,this._searchTimeout(n)}},keypress:function(s){if(t)return t=!1,(!this.isMultiLine||this.menu.element.is(":visible"))&&s.preventDefault(),void 0;if(!i){var n=e.ui.keyCode;switch(s.keyCode){case n.PAGE_UP:this._move("previousPage",s);break;case n.PAGE_DOWN:this._move("nextPage",s);break;case n.UP:this._keyEvent("previous",s);break;case n.DOWN:this._keyEvent("next",s)}}},input:function(e){return s?(s=!1,e.preventDefault(),void 0):(this._searchTimeout(e),void 0)},focus:function(){this.selectedItem=null,this.previous=this._value()},blur:function(e){return this.cancelBlur?(delete this.cancelBlur,void 0):(clearTimeout(this.searching),this.close(e),this._change(e),void 0)}}),this._initSource(),this.menu=e("<ul>").addClass("ui-autocomplete ui-front").appendTo(this._appendTo()).menu({role:null}).hide().menu("instance"),this._on(this.menu.element,{mousedown:function(t){t.preventDefault(),this.cancelBlur=!0,this._delay(function(){delete this.cancelBlur});var i=this.menu.element[0];e(t.target).closest(".ui-menu-item").length||this._delay(function(){var t=this;this.document.one("mousedown",function(s){s.target===t.element[0]||s.target===i||e.contains(i,s.target)||t.close()})})},menufocus:function(t,i){var s,n;return this.isNewMenu&&(this.isNewMenu=!1,t.originalEvent&&/^mouse/.test(t.originalEvent.type))?(this.menu.blur(),this.document.one("mousemove",function(){e(t.target).trigger(t.originalEvent)}),void 0):(n=i.item.data("ui-autocomplete-item"),!1!==this._trigger("focus",t,{item:n})&&t.originalEvent&&/^key/.test(t.originalEvent.type)&&this._value(n.value),s=i.item.attr("aria-label")||n.value,s&&e.trim(s).length&&(this.liveRegion.children().hide(),e("<div>").text(s).appendTo(this.liveRegion)),void 0)},menuselect:function(e,t){var i=t.item.data("ui-autocomplete-item"),s=this.previous;this.element[0]!==this.document[0].activeElement&&(this.element.focus(),this.previous=s,this._delay(function(){this.previous=s,this.selectedItem=i})),!1!==this._trigger("select",e,{item:i})&&this._value(i.value),this.term=this._value(),this.close(e),this.selectedItem=i}}),this.liveRegion=e("<span>",{role:"status","aria-live":"assertive","aria-relevant":"additions"}).addClass("ui-helper-hidden-accessible").appendTo(this.document[0].body),this._on(this.window,{beforeunload:function(){this.element.removeAttr("autocomplete")}})},_destroy:function(){clearTimeout(this.searching),this.element.removeClass("ui-autocomplete-input").removeAttr("autocomplete"),this.menu.element.remove(),this.liveRegion.remove()},_setOption:function(e,t){this._super(e,t),"source"===e&&this._initSource(),"appendTo"===e&&this.menu.element.appendTo(this._appendTo()),"disabled"===e&&t&&this.xhr&&this.xhr.abort()},_appendTo:function(){var t=this.options.appendTo;return t&&(t=t.jquery||t.nodeType?e(t):this.document.find(t).eq(0)),t&&t[0]||(t=this.element.closest(".ui-front")),t.length||(t=this.document[0].body),t},_initSource:function(){var t,i,s=this;e.isArray(this.options.source)?(t=this.options.source,this.source=function(i,s){s(e.ui.autocomplete.filter(t,i.term))}):"string"==typeof this.options.source?(i=this.options.source,this.source=function(t,n){s.xhr&&s.xhr.abort(),s.xhr=e.ajax({url:i,data:t,dataType:"json",success:function(e){n(e)},error:function(){n([])}})}):this.source=this.options.source},_searchTimeout:function(e){clearTimeout(this.searching),this.searching=this._delay(function(){var t=this.term===this._value(),i=this.menu.element.is(":visible"),s=e.altKey||e.ctrlKey||e.metaKey||e.shiftKey;(!t||t&&!i&&!s)&&(this.selectedItem=null,this.search(null,e))},this.options.delay)},search:function(e,t){return e=null!=e?e:this._value(),this.term=this._value(),e.length<this.options.minLength?this.close(t):this._trigger("search",t)!==!1?this._search(e):void 0},_search:function(e){this.pending++,this.element.addClass("ui-autocomplete-loading"),this.cancelSearch=!1,this.source({term:e},this._response())},_response:function(){var t=++this.requestIndex;return e.proxy(function(e){t===this.requestIndex&&this.__response(e),this.pending--,this.pending||this.element.removeClass("ui-autocomplete-loading")},this)},__response:function(e){e&&(e=this._normalize(e)),this._trigger("response",null,{content:e}),!this.options.disabled&&e&&e.length&&!this.cancelSearch?(this._suggest(e),this._trigger("open")):this._close()},close:function(e){this.cancelSearch=!0,this._close(e)},_close:function(e){this.menu.element.is(":visible")&&(this.menu.element.hide(),this.menu.blur(),this.isNewMenu=!0,this._trigger("close",e))},_change:function(e){this.previous!==this._value()&&this._trigger("change",e,{item:this.selectedItem})},_normalize:function(t){return t.length&&t[0].label&&t[0].value?t:e.map(t,function(t){return"string"==typeof t?{label:t,value:t}:e.extend({},t,{label:t.label||t.value,value:t.value||t.label})})},_suggest:function(t){var i=this.menu.element.empty();this._renderMenu(i,t),this.isNewMenu=!0,this.menu.refresh(),i.show(),this._resizeMenu(),i.position(e.extend({of:this.element},this.options.position)),this.options.autoFocus&&this.menu.next()},_resizeMenu:function(){var e=this.menu.element;e.outerWidth(Math.max(e.width("").outerWidth()+1,this.element.outerWidth()))},_renderMenu:function(t,i){var s=this;e.each(i,function(e,i){s._renderItemData(t,i)})},_renderItemData:function(e,t){return this._renderItem(e,t).data("ui-autocomplete-item",t)},_renderItem:function(t,i){return e("<li>").text(i.label).appendTo(t)},_move:function(e,t){return this.menu.element.is(":visible")?this.menu.isFirstItem()&&/^previous/.test(e)||this.menu.isLastItem()&&/^next/.test(e)?(this.isMultiLine||this._value(this.term),this.menu.blur(),void 0):(this.menu[e](t),void 0):(this.search(null,t),void 0)},widget:function(){return this.menu.element},_value:function(){return this.valueMethod.apply(this.element,arguments)},_keyEvent:function(e,t){(!this.isMultiLine||this.menu.element.is(":visible"))&&(this._move(e,t),t.preventDefault())}}),e.extend(e.ui.autocomplete,{escapeRegex:function(e){return e.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g,"\\$&")},filter:function(t,i){var s=RegExp(e.ui.autocomplete.escapeRegex(i),"i");return e.grep(t,function(e){return s.test(e.label||e.value||e)})}}),e.widget("ui.autocomplete",e.ui.autocomplete,{options:{messages:{noResults:"No search results.",results:function(e){return e+(e>1?" results are":" result is")+" available, use up and down arrow keys to navigate."}}},__response:function(t){var i;this._superApply(arguments),this.options.disabled||this.cancelSearch||(i=t&&t.length?this.options.messages.results(t.length):this.options.messages.noResults,this.liveRegion.children().hide(),e("<div>").text(i).appendTo(this.liveRegion))}}),e.ui.autocomplete;var c,p="ui-button ui-widget ui-state-default ui-corner-all",f="ui-button-icons-only ui-button-icon-only ui-button-text-icons ui-button-text-icon-primary ui-button-text-icon-secondary ui-button-text-only",m=function(){var t=e(this);setTimeout(function(){t.find(":ui-button").button("refresh")},1)},g=function(t){var i=t.name,s=t.form,n=e([]);return i&&(i=i.replace(/'/g,"\\'"),n=s?e(s).find("[name='"+i+"'][type=radio]"):e("[name='"+i+"'][type=radio]",t.ownerDocument).filter(function(){return!this.form})),n};e.widget("ui.button",{version:"1.11.2",defaultElement:"<button>",options:{disabled:null,text:!0,label:null,icons:{primary:null,secondary:null}},_create:function(){this.element.closest("form").unbind("reset"+this.eventNamespace).bind("reset"+this.eventNamespace,m),"boolean"!=typeof this.options.disabled?this.options.disabled=!!this.element.prop("disabled"):this.element.prop("disabled",this.options.disabled),this._determineButtonType(),this.hasTitle=!!this.buttonElement.attr("title");var t=this,i=this.options,s="checkbox"===this.type||"radio"===this.type,n=s?"":"ui-state-active";null===i.label&&(i.label="input"===this.type?this.buttonElement.val():this.buttonElement.html()),this._hoverable(this.buttonElement),this.buttonElement.addClass(p).attr("role","button").bind("mouseenter"+this.eventNamespace,function(){i.disabled||this===c&&e(this).addClass("ui-state-active")}).bind("mouseleave"+this.eventNamespace,function(){i.disabled||e(this).removeClass(n)}).bind("click"+this.eventNamespace,function(e){i.disabled&&(e.preventDefault(),e.stopImmediatePropagation())}),this._on({focus:function(){this.buttonElement.addClass("ui-state-focus")},blur:function(){this.buttonElement.removeClass("ui-state-focus")}}),s&&this.element.bind("change"+this.eventNamespace,function(){t.refresh()}),"checkbox"===this.type?this.buttonElement.bind("click"+this.eventNamespace,function(){return i.disabled?!1:void 0}):"radio"===this.type?this.buttonElement.bind("click"+this.eventNamespace,function(){if(i.disabled)return!1;e(this).addClass("ui-state-active"),t.buttonElement.attr("aria-pressed","true");var s=t.element[0];g(s).not(s).map(function(){return e(this).button("widget")[0]}).removeClass("ui-state-active").attr("aria-pressed","false")}):(this.buttonElement.bind("mousedown"+this.eventNamespace,function(){return i.disabled?!1:(e(this).addClass("ui-state-active"),c=this,t.document.one("mouseup",function(){c=null}),void 0)}).bind("mouseup"+this.eventNamespace,function(){return i.disabled?!1:(e(this).removeClass("ui-state-active"),void 0)}).bind("keydown"+this.eventNamespace,function(t){return i.disabled?!1:((t.keyCode===e.ui.keyCode.SPACE||t.keyCode===e.ui.keyCode.ENTER)&&e(this).addClass("ui-state-active"),void 0)}).bind("keyup"+this.eventNamespace+" blur"+this.eventNamespace,function(){e(this).removeClass("ui-state-active")}),this.buttonElement.is("a")&&this.buttonElement.keyup(function(t){t.keyCode===e.ui.keyCode.SPACE&&e(this).click()})),this._setOption("disabled",i.disabled),this._resetButton()},_determineButtonType:function(){var e,t,i;this.type=this.element.is("[type=checkbox]")?"checkbox":this.element.is("[type=radio]")?"radio":this.element.is("input")?"input":"button","checkbox"===this.type||"radio"===this.type?(e=this.element.parents().last(),t="label[for='"+this.element.attr("id")+"']",this.buttonElement=e.find(t),this.buttonElement.length||(e=e.length?e.siblings():this.element.siblings(),this.buttonElement=e.filter(t),this.buttonElement.length||(this.buttonElement=e.find(t))),this.element.addClass("ui-helper-hidden-accessible"),i=this.element.is(":checked"),i&&this.buttonElement.addClass("ui-state-active"),this.buttonElement.prop("aria-pressed",i)):this.buttonElement=this.element},widget:function(){return this.buttonElement},_destroy:function(){this.element.removeClass("ui-helper-hidden-accessible"),this.buttonElement.removeClass(p+" ui-state-active "+f).removeAttr("role").removeAttr("aria-pressed").html(this.buttonElement.find(".ui-button-text").html()),this.hasTitle||this.buttonElement.removeAttr("title")},_setOption:function(e,t){return this._super(e,t),"disabled"===e?(this.widget().toggleClass("ui-state-disabled",!!t),this.element.prop("disabled",!!t),t&&("checkbox"===this.type||"radio"===this.type?this.buttonElement.removeClass("ui-state-focus"):this.buttonElement.removeClass("ui-state-focus ui-state-active")),void 0):(this._resetButton(),void 0)},refresh:function(){var t=this.element.is("input, button")?this.element.is(":disabled"):this.element.hasClass("ui-button-disabled");t!==this.options.disabled&&this._setOption("disabled",t),"radio"===this.type?g(this.element[0]).each(function(){e(this).is(":checked")?e(this).button("widget").addClass("ui-state-active").attr("aria-pressed","true"):e(this).button("widget").removeClass("ui-state-active").attr("aria-pressed","false")}):"checkbox"===this.type&&(this.element.is(":checked")?this.buttonElement.addClass("ui-state-active").attr("aria-pressed","true"):this.buttonElement.removeClass("ui-state-active").attr("aria-pressed","false"))},_resetButton:function(){if("input"===this.type)return this.options.label&&this.element.val(this.options.label),void 0;var t=this.buttonElement.removeClass(f),i=e("<span></span>",this.document[0]).addClass("ui-button-text").html(this.options.label).appendTo(t.empty()).text(),s=this.options.icons,n=s.primary&&s.secondary,a=[];s.primary||s.secondary?(this.options.text&&a.push("ui-button-text-icon"+(n?"s":s.primary?"-primary":"-secondary")),s.primary&&t.prepend("<span class='ui-button-icon-primary ui-icon "+s.primary+"'></span>"),s.secondary&&t.append("<span class='ui-button-icon-secondary ui-icon "+s.secondary+"'></span>"),this.options.text||(a.push(n?"ui-button-icons-only":"ui-button-icon-only"),this.hasTitle||t.attr("title",e.trim(i)))):a.push("ui-button-text-only"),t.addClass(a.join(" "))}}),e.widget("ui.buttonset",{version:"1.11.2",options:{items:"button, input[type=button], input[type=submit], input[type=reset], input[type=checkbox], input[type=radio], a, :data(ui-button)"},_create:function(){this.element.addClass("ui-buttonset")},_init:function(){this.refresh()},_setOption:function(e,t){"disabled"===e&&this.buttons.button("option",e,t),this._super(e,t)},refresh:function(){var t="rtl"===this.element.css("direction"),i=this.element.find(this.options.items),s=i.filter(":ui-button");i.not(":ui-button").button(),s.button("refresh"),this.buttons=i.map(function(){return e(this).button("widget")[0]}).removeClass("ui-corner-all ui-corner-left ui-corner-right").filter(":first").addClass(t?"ui-corner-right":"ui-corner-left").end().filter(":last").addClass(t?"ui-corner-left":"ui-corner-right").end().end()},_destroy:function(){this.element.removeClass("ui-buttonset"),this.buttons.map(function(){return e(this).button("widget")[0]}).removeClass("ui-corner-left ui-corner-right").end().button("destroy")}}),e.ui.button,e.extend(e.ui,{datepicker:{version:"1.11.2"}});var v;e.extend(n.prototype,{markerClassName:"hasDatepicker",maxRows:4,_widgetDatepicker:function(){return this.dpDiv},setDefaults:function(e){return r(this._defaults,e||{}),this},_attachDatepicker:function(t,i){var s,n,a;s=t.nodeName.toLowerCase(),n="div"===s||"span"===s,t.id||(this.uuid+=1,t.id="dp"+this.uuid),a=this._newInst(e(t),n),a.settings=e.extend({},i||{}),"input"===s?this._connectDatepicker(t,a):n&&this._inlineDatepicker(t,a)},_newInst:function(t,i){var s=t[0].id.replace(/([^A-Za-z0-9_\-])/g,"\\\\$1");return{id:s,input:t,selectedDay:0,selectedMonth:0,selectedYear:0,drawMonth:0,drawYear:0,inline:i,dpDiv:i?a(e("<div class='"+this._inlineClass+" ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>")):this.dpDiv}},_connectDatepicker:function(t,i){var s=e(t);i.append=e([]),i.trigger=e([]),s.hasClass(this.markerClassName)||(this._attachments(s,i),s.addClass(this.markerClassName).keydown(this._doKeyDown).keypress(this._doKeyPress).keyup(this._doKeyUp),this._autoSize(i),e.data(t,"datepicker",i),i.settings.disabled&&this._disableDatepicker(t))},_attachments:function(t,i){var s,n,a,o=this._get(i,"appendText"),r=this._get(i,"isRTL");i.append&&i.append.remove(),o&&(i.append=e("<span class='"+this._appendClass+"'>"+o+"</span>"),t[r?"before":"after"](i.append)),t.unbind("focus",this._showDatepicker),i.trigger&&i.trigger.remove(),s=this._get(i,"showOn"),("focus"===s||"both"===s)&&t.focus(this._showDatepicker),("button"===s||"both"===s)&&(n=this._get(i,"buttonText"),a=this._get(i,"buttonImage"),i.trigger=e(this._get(i,"buttonImageOnly")?e("<img/>").addClass(this._triggerClass).attr({src:a,alt:n,title:n}):e("<button type='button'></button>").addClass(this._triggerClass).html(a?e("<img/>").attr({src:a,alt:n,title:n}):n)),t[r?"before":"after"](i.trigger),i.trigger.click(function(){return e.datepicker._datepickerShowing&&e.datepicker._lastInput===t[0]?e.datepicker._hideDatepicker():e.datepicker._datepickerShowing&&e.datepicker._lastInput!==t[0]?(e.datepicker._hideDatepicker(),e.datepicker._showDatepicker(t[0])):e.datepicker._showDatepicker(t[0]),!1}))},_autoSize:function(e){if(this._get(e,"autoSize")&&!e.inline){var t,i,s,n,a=new Date(2009,11,20),o=this._get(e,"dateFormat");o.match(/[DM]/)&&(t=function(e){for(i=0,s=0,n=0;e.length>n;n++)e[n].length>i&&(i=e[n].length,s=n);return s},a.setMonth(t(this._get(e,o.match(/MM/)?"monthNames":"monthNamesShort"))),a.setDate(t(this._get(e,o.match(/DD/)?"dayNames":"dayNamesShort"))+20-a.getDay())),e.input.attr("size",this._formatDate(e,a).length)}},_inlineDatepicker:function(t,i){var s=e(t);s.hasClass(this.markerClassName)||(s.addClass(this.markerClassName).append(i.dpDiv),e.data(t,"datepicker",i),this._setDate(i,this._getDefaultDate(i),!0),this._updateDatepicker(i),this._updateAlternate(i),i.settings.disabled&&this._disableDatepicker(t),i.dpDiv.css("display","block"))},_dialogDatepicker:function(t,i,s,n,a){var o,h,l,u,d,c=this._dialogInst;return c||(this.uuid+=1,o="dp"+this.uuid,this._dialogInput=e("<input type='text' id='"+o+"' style='position: absolute; top: -100px; width: 0px;'/>"),this._dialogInput.keydown(this._doKeyDown),e("body").append(this._dialogInput),c=this._dialogInst=this._newInst(this._dialogInput,!1),c.settings={},e.data(this._dialogInput[0],"datepicker",c)),r(c.settings,n||{}),i=i&&i.constructor===Date?this._formatDate(c,i):i,this._dialogInput.val(i),this._pos=a?a.length?a:[a.pageX,a.pageY]:null,this._pos||(h=document.documentElement.clientWidth,l=document.documentElement.clientHeight,u=document.documentElement.scrollLeft||document.body.scrollLeft,d=document.documentElement.scrollTop||document.body.scrollTop,this._pos=[h/2-100+u,l/2-150+d]),this._dialogInput.css("left",this._pos[0]+20+"px").css("top",this._pos[1]+"px"),c.settings.onSelect=s,this._inDialog=!0,this.dpDiv.addClass(this._dialogClass),this._showDatepicker(this._dialogInput[0]),e.blockUI&&e.blockUI(this.dpDiv),e.data(this._dialogInput[0],"datepicker",c),this},_destroyDatepicker:function(t){var i,s=e(t),n=e.data(t,"datepicker");s.hasClass(this.markerClassName)&&(i=t.nodeName.toLowerCase(),e.removeData(t,"datepicker"),"input"===i?(n.append.remove(),n.trigger.remove(),s.removeClass(this.markerClassName).unbind("focus",this._showDatepicker).unbind("keydown",this._doKeyDown).unbind("keypress",this._doKeyPress).unbind("keyup",this._doKeyUp)):("div"===i||"span"===i)&&s.removeClass(this.markerClassName).empty())},_enableDatepicker:function(t){var i,s,n=e(t),a=e.data(t,"datepicker");n.hasClass(this.markerClassName)&&(i=t.nodeName.toLowerCase(),"input"===i?(t.disabled=!1,a.trigger.filter("button").each(function(){this.disabled=!1}).end().filter("img").css({opacity:"1.0",cursor:""})):("div"===i||"span"===i)&&(s=n.children("."+this._inlineClass),s.children().removeClass("ui-state-disabled"),s.find("select.ui-datepicker-month, select.ui-datepicker-year").prop("disabled",!1)),this._disabledInputs=e.map(this._disabledInputs,function(e){return e===t?null:e}))},_disableDatepicker:function(t){var i,s,n=e(t),a=e.data(t,"datepicker");n.hasClass(this.markerClassName)&&(i=t.nodeName.toLowerCase(),"input"===i?(t.disabled=!0,a.trigger.filter("button").each(function(){this.disabled=!0}).end().filter("img").css({opacity:"0.5",cursor:"default"})):("div"===i||"span"===i)&&(s=n.children("."+this._inlineClass),s.children().addClass("ui-state-disabled"),s.find("select.ui-datepicker-month, select.ui-datepicker-year").prop("disabled",!0)),this._disabledInputs=e.map(this._disabledInputs,function(e){return e===t?null:e}),this._disabledInputs[this._disabledInputs.length]=t)},_isDisabledDatepicker:function(e){if(!e)return!1;for(var t=0;this._disabledInputs.length>t;t++)if(this._disabledInputs[t]===e)return!0;return!1},_getInst:function(t){try{return e.data(t,"datepicker")}catch(i){throw"Missing instance data for this datepicker"}},_optionDatepicker:function(t,i,s){var n,a,o,h,l=this._getInst(t);return 2===arguments.length&&"string"==typeof i?"defaults"===i?e.extend({},e.datepicker._defaults):l?"all"===i?e.extend({},l.settings):this._get(l,i):null:(n=i||{},"string"==typeof i&&(n={},n[i]=s),l&&(this._curInst===l&&this._hideDatepicker(),a=this._getDateDatepicker(t,!0),o=this._getMinMaxDate(l,"min"),h=this._getMinMaxDate(l,"max"),r(l.settings,n),null!==o&&void 0!==n.dateFormat&&void 0===n.minDate&&(l.settings.minDate=this._formatDate(l,o)),null!==h&&void 0!==n.dateFormat&&void 0===n.maxDate&&(l.settings.maxDate=this._formatDate(l,h)),"disabled"in n&&(n.disabled?this._disableDatepicker(t):this._enableDatepicker(t)),this._attachments(e(t),l),this._autoSize(l),this._setDate(l,a),this._updateAlternate(l),this._updateDatepicker(l)),void 0)},_changeDatepicker:function(e,t,i){this._optionDatepicker(e,t,i)},_refreshDatepicker:function(e){var t=this._getInst(e);t&&this._updateDatepicker(t)},_setDateDatepicker:function(e,t){var i=this._getInst(e);i&&(this._setDate(i,t),this._updateDatepicker(i),this._updateAlternate(i))},_getDateDatepicker:function(e,t){var i=this._getInst(e);return i&&!i.inline&&this._setDateFromField(i,t),i?this._getDate(i):null},_doKeyDown:function(t){var i,s,n,a=e.datepicker._getInst(t.target),o=!0,r=a.dpDiv.is(".ui-datepicker-rtl");if(a._keyEvent=!0,e.datepicker._datepickerShowing)switch(t.keyCode){case 9:e.datepicker._hideDatepicker(),o=!1;break;case 13:return n=e("td."+e.datepicker._dayOverClass+":not(."+e.datepicker._currentClass+")",a.dpDiv),n[0]&&e.datepicker._selectDay(t.target,a.selectedMonth,a.selectedYear,n[0]),i=e.datepicker._get(a,"onSelect"),i?(s=e.datepicker._formatDate(a),i.apply(a.input?a.input[0]:null,[s,a])):e.datepicker._hideDatepicker(),!1;case 27:e.datepicker._hideDatepicker();break;case 33:e.datepicker._adjustDate(t.target,t.ctrlKey?-e.datepicker._get(a,"stepBigMonths"):-e.datepicker._get(a,"stepMonths"),"M");break;case 34:e.datepicker._adjustDate(t.target,t.ctrlKey?+e.datepicker._get(a,"stepBigMonths"):+e.datepicker._get(a,"stepMonths"),"M");break;case 35:(t.ctrlKey||t.metaKey)&&e.datepicker._clearDate(t.target),o=t.ctrlKey||t.metaKey;break;case 36:(t.ctrlKey||t.metaKey)&&e.datepicker._gotoToday(t.target),o=t.ctrlKey||t.metaKey;break;case 37:(t.ctrlKey||t.metaKey)&&e.datepicker._adjustDate(t.target,r?1:-1,"D"),o=t.ctrlKey||t.metaKey,t.originalEvent.altKey&&e.datepicker._adjustDate(t.target,t.ctrlKey?-e.datepicker._get(a,"stepBigMonths"):-e.datepicker._get(a,"stepMonths"),"M");break;case 38:(t.ctrlKey||t.metaKey)&&e.datepicker._adjustDate(t.target,-7,"D"),o=t.ctrlKey||t.metaKey;break;case 39:(t.ctrlKey||t.metaKey)&&e.datepicker._adjustDate(t.target,r?-1:1,"D"),o=t.ctrlKey||t.metaKey,t.originalEvent.altKey&&e.datepicker._adjustDate(t.target,t.ctrlKey?+e.datepicker._get(a,"stepBigMonths"):+e.datepicker._get(a,"stepMonths"),"M");break;case 40:(t.ctrlKey||t.metaKey)&&e.datepicker._adjustDate(t.target,7,"D"),o=t.ctrlKey||t.metaKey;break;default:o=!1}else 36===t.keyCode&&t.ctrlKey?e.datepicker._showDatepicker(this):o=!1;o&&(t.preventDefault(),t.stopPropagation())},_doKeyPress:function(t){var i,s,n=e.datepicker._getInst(t.target);return e.datepicker._get(n,"constrainInput")?(i=e.datepicker._possibleChars(e.datepicker._get(n,"dateFormat")),s=String.fromCharCode(null==t.charCode?t.keyCode:t.charCode),t.ctrlKey||t.metaKey||" ">s||!i||i.indexOf(s)>-1):void 0
},_doKeyUp:function(t){var i,s=e.datepicker._getInst(t.target);if(s.input.val()!==s.lastVal)try{i=e.datepicker.parseDate(e.datepicker._get(s,"dateFormat"),s.input?s.input.val():null,e.datepicker._getFormatConfig(s)),i&&(e.datepicker._setDateFromField(s),e.datepicker._updateAlternate(s),e.datepicker._updateDatepicker(s))}catch(n){}return!0},_showDatepicker:function(t){if(t=t.target||t,"input"!==t.nodeName.toLowerCase()&&(t=e("input",t.parentNode)[0]),!e.datepicker._isDisabledDatepicker(t)&&e.datepicker._lastInput!==t){var i,n,a,o,h,l,u;i=e.datepicker._getInst(t),e.datepicker._curInst&&e.datepicker._curInst!==i&&(e.datepicker._curInst.dpDiv.stop(!0,!0),i&&e.datepicker._datepickerShowing&&e.datepicker._hideDatepicker(e.datepicker._curInst.input[0])),n=e.datepicker._get(i,"beforeShow"),a=n?n.apply(t,[t,i]):{},a!==!1&&(r(i.settings,a),i.lastVal=null,e.datepicker._lastInput=t,e.datepicker._setDateFromField(i),e.datepicker._inDialog&&(t.value=""),e.datepicker._pos||(e.datepicker._pos=e.datepicker._findPos(t),e.datepicker._pos[1]+=t.offsetHeight),o=!1,e(t).parents().each(function(){return o|="fixed"===e(this).css("position"),!o}),h={left:e.datepicker._pos[0],top:e.datepicker._pos[1]},e.datepicker._pos=null,i.dpDiv.empty(),i.dpDiv.css({position:"absolute",display:"block",top:"-1000px"}),e.datepicker._updateDatepicker(i),h=e.datepicker._checkOffset(i,h,o),i.dpDiv.css({position:e.datepicker._inDialog&&e.blockUI?"static":o?"fixed":"absolute",display:"none",left:h.left+"px",top:h.top+"px"}),i.inline||(l=e.datepicker._get(i,"showAnim"),u=e.datepicker._get(i,"duration"),i.dpDiv.css("z-index",s(e(t))+1),e.datepicker._datepickerShowing=!0,e.effects&&e.effects.effect[l]?i.dpDiv.show(l,e.datepicker._get(i,"showOptions"),u):i.dpDiv[l||"show"](l?u:null),e.datepicker._shouldFocusInput(i)&&i.input.focus(),e.datepicker._curInst=i))}},_updateDatepicker:function(t){this.maxRows=4,v=t,t.dpDiv.empty().append(this._generateHTML(t)),this._attachHandlers(t);var i,s=this._getNumberOfMonths(t),n=s[1],a=17,r=t.dpDiv.find("."+this._dayOverClass+" a");r.length>0&&o.apply(r.get(0)),t.dpDiv.removeClass("ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4").width(""),n>1&&t.dpDiv.addClass("ui-datepicker-multi-"+n).css("width",a*n+"em"),t.dpDiv[(1!==s[0]||1!==s[1]?"add":"remove")+"Class"]("ui-datepicker-multi"),t.dpDiv[(this._get(t,"isRTL")?"add":"remove")+"Class"]("ui-datepicker-rtl"),t===e.datepicker._curInst&&e.datepicker._datepickerShowing&&e.datepicker._shouldFocusInput(t)&&t.input.focus(),t.yearshtml&&(i=t.yearshtml,setTimeout(function(){i===t.yearshtml&&t.yearshtml&&t.dpDiv.find("select.ui-datepicker-year:first").replaceWith(t.yearshtml),i=t.yearshtml=null},0))},_shouldFocusInput:function(e){return e.input&&e.input.is(":visible")&&!e.input.is(":disabled")&&!e.input.is(":focus")},_checkOffset:function(t,i,s){var n=t.dpDiv.outerWidth(),a=t.dpDiv.outerHeight(),o=t.input?t.input.outerWidth():0,r=t.input?t.input.outerHeight():0,h=document.documentElement.clientWidth+(s?0:e(document).scrollLeft()),l=document.documentElement.clientHeight+(s?0:e(document).scrollTop());return i.left-=this._get(t,"isRTL")?n-o:0,i.left-=s&&i.left===t.input.offset().left?e(document).scrollLeft():0,i.top-=s&&i.top===t.input.offset().top+r?e(document).scrollTop():0,i.left-=Math.min(i.left,i.left+n>h&&h>n?Math.abs(i.left+n-h):0),i.top-=Math.min(i.top,i.top+a>l&&l>a?Math.abs(a+r):0),i},_findPos:function(t){for(var i,s=this._getInst(t),n=this._get(s,"isRTL");t&&("hidden"===t.type||1!==t.nodeType||e.expr.filters.hidden(t));)t=t[n?"previousSibling":"nextSibling"];return i=e(t).offset(),[i.left,i.top]},_hideDatepicker:function(t){var i,s,n,a,o=this._curInst;!o||t&&o!==e.data(t,"datepicker")||this._datepickerShowing&&(i=this._get(o,"showAnim"),s=this._get(o,"duration"),n=function(){e.datepicker._tidyDialog(o)},e.effects&&(e.effects.effect[i]||e.effects[i])?o.dpDiv.hide(i,e.datepicker._get(o,"showOptions"),s,n):o.dpDiv["slideDown"===i?"slideUp":"fadeIn"===i?"fadeOut":"hide"](i?s:null,n),i||n(),this._datepickerShowing=!1,a=this._get(o,"onClose"),a&&a.apply(o.input?o.input[0]:null,[o.input?o.input.val():"",o]),this._lastInput=null,this._inDialog&&(this._dialogInput.css({position:"absolute",left:"0",top:"-100px"}),e.blockUI&&(e.unblockUI(),e("body").append(this.dpDiv))),this._inDialog=!1)},_tidyDialog:function(e){e.dpDiv.removeClass(this._dialogClass).unbind(".ui-datepicker-calendar")},_checkExternalClick:function(t){if(e.datepicker._curInst){var i=e(t.target),s=e.datepicker._getInst(i[0]);(i[0].id!==e.datepicker._mainDivId&&0===i.parents("#"+e.datepicker._mainDivId).length&&!i.hasClass(e.datepicker.markerClassName)&&!i.closest("."+e.datepicker._triggerClass).length&&e.datepicker._datepickerShowing&&(!e.datepicker._inDialog||!e.blockUI)||i.hasClass(e.datepicker.markerClassName)&&e.datepicker._curInst!==s)&&e.datepicker._hideDatepicker()}},_adjustDate:function(t,i,s){var n=e(t),a=this._getInst(n[0]);this._isDisabledDatepicker(n[0])||(this._adjustInstDate(a,i+("M"===s?this._get(a,"showCurrentAtPos"):0),s),this._updateDatepicker(a))},_gotoToday:function(t){var i,s=e(t),n=this._getInst(s[0]);this._get(n,"gotoCurrent")&&n.currentDay?(n.selectedDay=n.currentDay,n.drawMonth=n.selectedMonth=n.currentMonth,n.drawYear=n.selectedYear=n.currentYear):(i=new Date,n.selectedDay=i.getDate(),n.drawMonth=n.selectedMonth=i.getMonth(),n.drawYear=n.selectedYear=i.getFullYear()),this._notifyChange(n),this._adjustDate(s)},_selectMonthYear:function(t,i,s){var n=e(t),a=this._getInst(n[0]);a["selected"+("M"===s?"Month":"Year")]=a["draw"+("M"===s?"Month":"Year")]=parseInt(i.options[i.selectedIndex].value,10),this._notifyChange(a),this._adjustDate(n)},_selectDay:function(t,i,s,n){var a,o=e(t);e(n).hasClass(this._unselectableClass)||this._isDisabledDatepicker(o[0])||(a=this._getInst(o[0]),a.selectedDay=a.currentDay=e("a",n).html(),a.selectedMonth=a.currentMonth=i,a.selectedYear=a.currentYear=s,this._selectDate(t,this._formatDate(a,a.currentDay,a.currentMonth,a.currentYear)))},_clearDate:function(t){var i=e(t);this._selectDate(i,"")},_selectDate:function(t,i){var s,n=e(t),a=this._getInst(n[0]);i=null!=i?i:this._formatDate(a),a.input&&a.input.val(i),this._updateAlternate(a),s=this._get(a,"onSelect"),s?s.apply(a.input?a.input[0]:null,[i,a]):a.input&&a.input.trigger("change"),a.inline?this._updateDatepicker(a):(this._hideDatepicker(),this._lastInput=a.input[0],"object"!=typeof a.input[0]&&a.input.focus(),this._lastInput=null)},_updateAlternate:function(t){var i,s,n,a=this._get(t,"altField");a&&(i=this._get(t,"altFormat")||this._get(t,"dateFormat"),s=this._getDate(t),n=this.formatDate(i,s,this._getFormatConfig(t)),e(a).each(function(){e(this).val(n)}))},noWeekends:function(e){var t=e.getDay();return[t>0&&6>t,""]},iso8601Week:function(e){var t,i=new Date(e.getTime());return i.setDate(i.getDate()+4-(i.getDay()||7)),t=i.getTime(),i.setMonth(0),i.setDate(1),Math.floor(Math.round((t-i)/864e5)/7)+1},parseDate:function(t,i,s){if(null==t||null==i)throw"Invalid arguments";if(i="object"==typeof i?""+i:i+"",""===i)return null;var n,a,o,r,h=0,l=(s?s.shortYearCutoff:null)||this._defaults.shortYearCutoff,u="string"!=typeof l?l:(new Date).getFullYear()%100+parseInt(l,10),d=(s?s.dayNamesShort:null)||this._defaults.dayNamesShort,c=(s?s.dayNames:null)||this._defaults.dayNames,p=(s?s.monthNamesShort:null)||this._defaults.monthNamesShort,f=(s?s.monthNames:null)||this._defaults.monthNames,m=-1,g=-1,v=-1,y=-1,b=!1,_=function(e){var i=t.length>n+1&&t.charAt(n+1)===e;return i&&n++,i},x=function(e){var t=_(e),s="@"===e?14:"!"===e?20:"y"===e&&t?4:"o"===e?3:2,n="y"===e?s:1,a=RegExp("^\\d{"+n+","+s+"}"),o=i.substring(h).match(a);if(!o)throw"Missing number at position "+h;return h+=o[0].length,parseInt(o[0],10)},w=function(t,s,n){var a=-1,o=e.map(_(t)?n:s,function(e,t){return[[t,e]]}).sort(function(e,t){return-(e[1].length-t[1].length)});if(e.each(o,function(e,t){var s=t[1];return i.substr(h,s.length).toLowerCase()===s.toLowerCase()?(a=t[0],h+=s.length,!1):void 0}),-1!==a)return a+1;throw"Unknown name at position "+h},k=function(){if(i.charAt(h)!==t.charAt(n))throw"Unexpected literal at position "+h;h++};for(n=0;t.length>n;n++)if(b)"'"!==t.charAt(n)||_("'")?k():b=!1;else switch(t.charAt(n)){case"d":v=x("d");break;case"D":w("D",d,c);break;case"o":y=x("o");break;case"m":g=x("m");break;case"M":g=w("M",p,f);break;case"y":m=x("y");break;case"@":r=new Date(x("@")),m=r.getFullYear(),g=r.getMonth()+1,v=r.getDate();break;case"!":r=new Date((x("!")-this._ticksTo1970)/1e4),m=r.getFullYear(),g=r.getMonth()+1,v=r.getDate();break;case"'":_("'")?k():b=!0;break;default:k()}if(i.length>h&&(o=i.substr(h),!/^\s+/.test(o)))throw"Extra/unparsed characters found in date: "+o;if(-1===m?m=(new Date).getFullYear():100>m&&(m+=(new Date).getFullYear()-(new Date).getFullYear()%100+(u>=m?0:-100)),y>-1)for(g=1,v=y;;){if(a=this._getDaysInMonth(m,g-1),a>=v)break;g++,v-=a}if(r=this._daylightSavingAdjust(new Date(m,g-1,v)),r.getFullYear()!==m||r.getMonth()+1!==g||r.getDate()!==v)throw"Invalid date";return r},ATOM:"yy-mm-dd",COOKIE:"D, dd M yy",ISO_8601:"yy-mm-dd",RFC_822:"D, d M y",RFC_850:"DD, dd-M-y",RFC_1036:"D, d M y",RFC_1123:"D, d M yy",RFC_2822:"D, d M yy",RSS:"D, d M y",TICKS:"!",TIMESTAMP:"@",W3C:"yy-mm-dd",_ticksTo1970:1e7*60*60*24*(718685+Math.floor(492.5)-Math.floor(19.7)+Math.floor(4.925)),formatDate:function(e,t,i){if(!t)return"";var s,n=(i?i.dayNamesShort:null)||this._defaults.dayNamesShort,a=(i?i.dayNames:null)||this._defaults.dayNames,o=(i?i.monthNamesShort:null)||this._defaults.monthNamesShort,r=(i?i.monthNames:null)||this._defaults.monthNames,h=function(t){var i=e.length>s+1&&e.charAt(s+1)===t;return i&&s++,i},l=function(e,t,i){var s=""+t;if(h(e))for(;i>s.length;)s="0"+s;return s},u=function(e,t,i,s){return h(e)?s[t]:i[t]},d="",c=!1;if(t)for(s=0;e.length>s;s++)if(c)"'"!==e.charAt(s)||h("'")?d+=e.charAt(s):c=!1;else switch(e.charAt(s)){case"d":d+=l("d",t.getDate(),2);break;case"D":d+=u("D",t.getDay(),n,a);break;case"o":d+=l("o",Math.round((new Date(t.getFullYear(),t.getMonth(),t.getDate()).getTime()-new Date(t.getFullYear(),0,0).getTime())/864e5),3);break;case"m":d+=l("m",t.getMonth()+1,2);break;case"M":d+=u("M",t.getMonth(),o,r);break;case"y":d+=h("y")?t.getFullYear():(10>t.getYear()%100?"0":"")+t.getYear()%100;break;case"@":d+=t.getTime();break;case"!":d+=1e4*t.getTime()+this._ticksTo1970;break;case"'":h("'")?d+="'":c=!0;break;default:d+=e.charAt(s)}return d},_possibleChars:function(e){var t,i="",s=!1,n=function(i){var s=e.length>t+1&&e.charAt(t+1)===i;return s&&t++,s};for(t=0;e.length>t;t++)if(s)"'"!==e.charAt(t)||n("'")?i+=e.charAt(t):s=!1;else switch(e.charAt(t)){case"d":case"m":case"y":case"@":i+="0123456789";break;case"D":case"M":return null;case"'":n("'")?i+="'":s=!0;break;default:i+=e.charAt(t)}return i},_get:function(e,t){return void 0!==e.settings[t]?e.settings[t]:this._defaults[t]},_setDateFromField:function(e,t){if(e.input.val()!==e.lastVal){var i=this._get(e,"dateFormat"),s=e.lastVal=e.input?e.input.val():null,n=this._getDefaultDate(e),a=n,o=this._getFormatConfig(e);try{a=this.parseDate(i,s,o)||n}catch(r){s=t?"":s}e.selectedDay=a.getDate(),e.drawMonth=e.selectedMonth=a.getMonth(),e.drawYear=e.selectedYear=a.getFullYear(),e.currentDay=s?a.getDate():0,e.currentMonth=s?a.getMonth():0,e.currentYear=s?a.getFullYear():0,this._adjustInstDate(e)}},_getDefaultDate:function(e){return this._restrictMinMax(e,this._determineDate(e,this._get(e,"defaultDate"),new Date))},_determineDate:function(t,i,s){var n=function(e){var t=new Date;return t.setDate(t.getDate()+e),t},a=function(i){try{return e.datepicker.parseDate(e.datepicker._get(t,"dateFormat"),i,e.datepicker._getFormatConfig(t))}catch(s){}for(var n=(i.toLowerCase().match(/^c/)?e.datepicker._getDate(t):null)||new Date,a=n.getFullYear(),o=n.getMonth(),r=n.getDate(),h=/([+\-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g,l=h.exec(i);l;){switch(l[2]||"d"){case"d":case"D":r+=parseInt(l[1],10);break;case"w":case"W":r+=7*parseInt(l[1],10);break;case"m":case"M":o+=parseInt(l[1],10),r=Math.min(r,e.datepicker._getDaysInMonth(a,o));break;case"y":case"Y":a+=parseInt(l[1],10),r=Math.min(r,e.datepicker._getDaysInMonth(a,o))}l=h.exec(i)}return new Date(a,o,r)},o=null==i||""===i?s:"string"==typeof i?a(i):"number"==typeof i?isNaN(i)?s:n(i):new Date(i.getTime());return o=o&&"Invalid Date"==""+o?s:o,o&&(o.setHours(0),o.setMinutes(0),o.setSeconds(0),o.setMilliseconds(0)),this._daylightSavingAdjust(o)},_daylightSavingAdjust:function(e){return e?(e.setHours(e.getHours()>12?e.getHours()+2:0),e):null},_setDate:function(e,t,i){var s=!t,n=e.selectedMonth,a=e.selectedYear,o=this._restrictMinMax(e,this._determineDate(e,t,new Date));e.selectedDay=e.currentDay=o.getDate(),e.drawMonth=e.selectedMonth=e.currentMonth=o.getMonth(),e.drawYear=e.selectedYear=e.currentYear=o.getFullYear(),n===e.selectedMonth&&a===e.selectedYear||i||this._notifyChange(e),this._adjustInstDate(e),e.input&&e.input.val(s?"":this._formatDate(e))},_getDate:function(e){var t=!e.currentYear||e.input&&""===e.input.val()?null:this._daylightSavingAdjust(new Date(e.currentYear,e.currentMonth,e.currentDay));return t},_attachHandlers:function(t){var i=this._get(t,"stepMonths"),s="#"+t.id.replace(/\\\\/g,"\\");t.dpDiv.find("[data-handler]").map(function(){var t={prev:function(){e.datepicker._adjustDate(s,-i,"M")},next:function(){e.datepicker._adjustDate(s,+i,"M")},hide:function(){e.datepicker._hideDatepicker()},today:function(){e.datepicker._gotoToday(s)},selectDay:function(){return e.datepicker._selectDay(s,+this.getAttribute("data-month"),+this.getAttribute("data-year"),this),!1},selectMonth:function(){return e.datepicker._selectMonthYear(s,this,"M"),!1},selectYear:function(){return e.datepicker._selectMonthYear(s,this,"Y"),!1}};e(this).bind(this.getAttribute("data-event"),t[this.getAttribute("data-handler")])})},_generateHTML:function(e){var t,i,s,n,a,o,r,h,l,u,d,c,p,f,m,g,v,y,b,_,x,w,k,T,D,S,M,C,N,A,P,I,z,H,F,E,O,j,W,L=new Date,R=this._daylightSavingAdjust(new Date(L.getFullYear(),L.getMonth(),L.getDate())),Y=this._get(e,"isRTL"),B=this._get(e,"showButtonPanel"),J=this._get(e,"hideIfNoPrevNext"),q=this._get(e,"navigationAsDateFormat"),K=this._getNumberOfMonths(e),V=this._get(e,"showCurrentAtPos"),U=this._get(e,"stepMonths"),Q=1!==K[0]||1!==K[1],G=this._daylightSavingAdjust(e.currentDay?new Date(e.currentYear,e.currentMonth,e.currentDay):new Date(9999,9,9)),X=this._getMinMaxDate(e,"min"),$=this._getMinMaxDate(e,"max"),Z=e.drawMonth-V,et=e.drawYear;if(0>Z&&(Z+=12,et--),$)for(t=this._daylightSavingAdjust(new Date($.getFullYear(),$.getMonth()-K[0]*K[1]+1,$.getDate())),t=X&&X>t?X:t;this._daylightSavingAdjust(new Date(et,Z,1))>t;)Z--,0>Z&&(Z=11,et--);for(e.drawMonth=Z,e.drawYear=et,i=this._get(e,"prevText"),i=q?this.formatDate(i,this._daylightSavingAdjust(new Date(et,Z-U,1)),this._getFormatConfig(e)):i,s=this._canAdjustMonth(e,-1,et,Z)?"<a class='ui-datepicker-prev ui-corner-all' data-handler='prev' data-event='click' title='"+i+"'><span class='ui-icon ui-icon-circle-triangle-"+(Y?"e":"w")+"'>"+i+"</span></a>":J?"":"<a class='ui-datepicker-prev ui-corner-all ui-state-disabled' title='"+i+"'><span class='ui-icon ui-icon-circle-triangle-"+(Y?"e":"w")+"'>"+i+"</span></a>",n=this._get(e,"nextText"),n=q?this.formatDate(n,this._daylightSavingAdjust(new Date(et,Z+U,1)),this._getFormatConfig(e)):n,a=this._canAdjustMonth(e,1,et,Z)?"<a class='ui-datepicker-next ui-corner-all' data-handler='next' data-event='click' title='"+n+"'><span class='ui-icon ui-icon-circle-triangle-"+(Y?"w":"e")+"'>"+n+"</span></a>":J?"":"<a class='ui-datepicker-next ui-corner-all ui-state-disabled' title='"+n+"'><span class='ui-icon ui-icon-circle-triangle-"+(Y?"w":"e")+"'>"+n+"</span></a>",o=this._get(e,"currentText"),r=this._get(e,"gotoCurrent")&&e.currentDay?G:R,o=q?this.formatDate(o,r,this._getFormatConfig(e)):o,h=e.inline?"":"<button type='button' class='ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all' data-handler='hide' data-event='click'>"+this._get(e,"closeText")+"</button>",l=B?"<div class='ui-datepicker-buttonpane ui-widget-content'>"+(Y?h:"")+(this._isInRange(e,r)?"<button type='button' class='ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all' data-handler='today' data-event='click'>"+o+"</button>":"")+(Y?"":h)+"</div>":"",u=parseInt(this._get(e,"firstDay"),10),u=isNaN(u)?0:u,d=this._get(e,"showWeek"),c=this._get(e,"dayNames"),p=this._get(e,"dayNamesMin"),f=this._get(e,"monthNames"),m=this._get(e,"monthNamesShort"),g=this._get(e,"beforeShowDay"),v=this._get(e,"showOtherMonths"),y=this._get(e,"selectOtherMonths"),b=this._getDefaultDate(e),_="",w=0;K[0]>w;w++){for(k="",this.maxRows=4,T=0;K[1]>T;T++){if(D=this._daylightSavingAdjust(new Date(et,Z,e.selectedDay)),S=" ui-corner-all",M="",Q){if(M+="<div class='ui-datepicker-group",K[1]>1)switch(T){case 0:M+=" ui-datepicker-group-first",S=" ui-corner-"+(Y?"right":"left");break;case K[1]-1:M+=" ui-datepicker-group-last",S=" ui-corner-"+(Y?"left":"right");break;default:M+=" ui-datepicker-group-middle",S=""}M+="'>"}for(M+="<div class='ui-datepicker-header ui-widget-header ui-helper-clearfix"+S+"'>"+(/all|left/.test(S)&&0===w?Y?a:s:"")+(/all|right/.test(S)&&0===w?Y?s:a:"")+this._generateMonthYearHeader(e,Z,et,X,$,w>0||T>0,f,m)+"</div><table class='ui-datepicker-calendar'><thead>"+"<tr>",C=d?"<th class='ui-datepicker-week-col'>"+this._get(e,"weekHeader")+"</th>":"",x=0;7>x;x++)N=(x+u)%7,C+="<th scope='col'"+((x+u+6)%7>=5?" class='ui-datepicker-week-end'":"")+">"+"<span title='"+c[N]+"'>"+p[N]+"</span></th>";for(M+=C+"</tr></thead><tbody>",A=this._getDaysInMonth(et,Z),et===e.selectedYear&&Z===e.selectedMonth&&(e.selectedDay=Math.min(e.selectedDay,A)),P=(this._getFirstDayOfMonth(et,Z)-u+7)%7,I=Math.ceil((P+A)/7),z=Q?this.maxRows>I?this.maxRows:I:I,this.maxRows=z,H=this._daylightSavingAdjust(new Date(et,Z,1-P)),F=0;z>F;F++){for(M+="<tr>",E=d?"<td class='ui-datepicker-week-col'>"+this._get(e,"calculateWeek")(H)+"</td>":"",x=0;7>x;x++)O=g?g.apply(e.input?e.input[0]:null,[H]):[!0,""],j=H.getMonth()!==Z,W=j&&!y||!O[0]||X&&X>H||$&&H>$,E+="<td class='"+((x+u+6)%7>=5?" ui-datepicker-week-end":"")+(j?" ui-datepicker-other-month":"")+(H.getTime()===D.getTime()&&Z===e.selectedMonth&&e._keyEvent||b.getTime()===H.getTime()&&b.getTime()===D.getTime()?" "+this._dayOverClass:"")+(W?" "+this._unselectableClass+" ui-state-disabled":"")+(j&&!v?"":" "+O[1]+(H.getTime()===G.getTime()?" "+this._currentClass:"")+(H.getTime()===R.getTime()?" ui-datepicker-today":""))+"'"+(j&&!v||!O[2]?"":" title='"+O[2].replace(/'/g,"&#39;")+"'")+(W?"":" data-handler='selectDay' data-event='click' data-month='"+H.getMonth()+"' data-year='"+H.getFullYear()+"'")+">"+(j&&!v?"&#xa0;":W?"<span class='ui-state-default'>"+H.getDate()+"</span>":"<a class='ui-state-default"+(H.getTime()===R.getTime()?" ui-state-highlight":"")+(H.getTime()===G.getTime()?" ui-state-active":"")+(j?" ui-priority-secondary":"")+"' href='#'>"+H.getDate()+"</a>")+"</td>",H.setDate(H.getDate()+1),H=this._daylightSavingAdjust(H);M+=E+"</tr>"}Z++,Z>11&&(Z=0,et++),M+="</tbody></table>"+(Q?"</div>"+(K[0]>0&&T===K[1]-1?"<div class='ui-datepicker-row-break'></div>":""):""),k+=M}_+=k}return _+=l,e._keyEvent=!1,_},_generateMonthYearHeader:function(e,t,i,s,n,a,o,r){var h,l,u,d,c,p,f,m,g=this._get(e,"changeMonth"),v=this._get(e,"changeYear"),y=this._get(e,"showMonthAfterYear"),b="<div class='ui-datepicker-title'>",_="";if(a||!g)_+="<span class='ui-datepicker-month'>"+o[t]+"</span>";else{for(h=s&&s.getFullYear()===i,l=n&&n.getFullYear()===i,_+="<select class='ui-datepicker-month' data-handler='selectMonth' data-event='change'>",u=0;12>u;u++)(!h||u>=s.getMonth())&&(!l||n.getMonth()>=u)&&(_+="<option value='"+u+"'"+(u===t?" selected='selected'":"")+">"+r[u]+"</option>");_+="</select>"}if(y||(b+=_+(!a&&g&&v?"":"&#xa0;")),!e.yearshtml)if(e.yearshtml="",a||!v)b+="<span class='ui-datepicker-year'>"+i+"</span>";else{for(d=this._get(e,"yearRange").split(":"),c=(new Date).getFullYear(),p=function(e){var t=e.match(/c[+\-].*/)?i+parseInt(e.substring(1),10):e.match(/[+\-].*/)?c+parseInt(e,10):parseInt(e,10);return isNaN(t)?c:t},f=p(d[0]),m=Math.max(f,p(d[1]||"")),f=s?Math.max(f,s.getFullYear()):f,m=n?Math.min(m,n.getFullYear()):m,e.yearshtml+="<select class='ui-datepicker-year' data-handler='selectYear' data-event='change'>";m>=f;f++)e.yearshtml+="<option value='"+f+"'"+(f===i?" selected='selected'":"")+">"+f+"</option>";e.yearshtml+="</select>",b+=e.yearshtml,e.yearshtml=null}return b+=this._get(e,"yearSuffix"),y&&(b+=(!a&&g&&v?"":"&#xa0;")+_),b+="</div>"},_adjustInstDate:function(e,t,i){var s=e.drawYear+("Y"===i?t:0),n=e.drawMonth+("M"===i?t:0),a=Math.min(e.selectedDay,this._getDaysInMonth(s,n))+("D"===i?t:0),o=this._restrictMinMax(e,this._daylightSavingAdjust(new Date(s,n,a)));e.selectedDay=o.getDate(),e.drawMonth=e.selectedMonth=o.getMonth(),e.drawYear=e.selectedYear=o.getFullYear(),("M"===i||"Y"===i)&&this._notifyChange(e)},_restrictMinMax:function(e,t){var i=this._getMinMaxDate(e,"min"),s=this._getMinMaxDate(e,"max"),n=i&&i>t?i:t;return s&&n>s?s:n},_notifyChange:function(e){var t=this._get(e,"onChangeMonthYear");t&&t.apply(e.input?e.input[0]:null,[e.selectedYear,e.selectedMonth+1,e])},_getNumberOfMonths:function(e){var t=this._get(e,"numberOfMonths");return null==t?[1,1]:"number"==typeof t?[1,t]:t},_getMinMaxDate:function(e,t){return this._determineDate(e,this._get(e,t+"Date"),null)},_getDaysInMonth:function(e,t){return 32-this._daylightSavingAdjust(new Date(e,t,32)).getDate()},_getFirstDayOfMonth:function(e,t){return new Date(e,t,1).getDay()},_canAdjustMonth:function(e,t,i,s){var n=this._getNumberOfMonths(e),a=this._daylightSavingAdjust(new Date(i,s+(0>t?t:n[0]*n[1]),1));return 0>t&&a.setDate(this._getDaysInMonth(a.getFullYear(),a.getMonth())),this._isInRange(e,a)},_isInRange:function(e,t){var i,s,n=this._getMinMaxDate(e,"min"),a=this._getMinMaxDate(e,"max"),o=null,r=null,h=this._get(e,"yearRange");return h&&(i=h.split(":"),s=(new Date).getFullYear(),o=parseInt(i[0],10),r=parseInt(i[1],10),i[0].match(/[+\-].*/)&&(o+=s),i[1].match(/[+\-].*/)&&(r+=s)),(!n||t.getTime()>=n.getTime())&&(!a||t.getTime()<=a.getTime())&&(!o||t.getFullYear()>=o)&&(!r||r>=t.getFullYear())},_getFormatConfig:function(e){var t=this._get(e,"shortYearCutoff");return t="string"!=typeof t?t:(new Date).getFullYear()%100+parseInt(t,10),{shortYearCutoff:t,dayNamesShort:this._get(e,"dayNamesShort"),dayNames:this._get(e,"dayNames"),monthNamesShort:this._get(e,"monthNamesShort"),monthNames:this._get(e,"monthNames")}},_formatDate:function(e,t,i,s){t||(e.currentDay=e.selectedDay,e.currentMonth=e.selectedMonth,e.currentYear=e.selectedYear);var n=t?"object"==typeof t?t:this._daylightSavingAdjust(new Date(s,i,t)):this._daylightSavingAdjust(new Date(e.currentYear,e.currentMonth,e.currentDay));return this.formatDate(this._get(e,"dateFormat"),n,this._getFormatConfig(e))}}),e.fn.datepicker=function(t){if(!this.length)return this;e.datepicker.initialized||(e(document).mousedown(e.datepicker._checkExternalClick),e.datepicker.initialized=!0),0===e("#"+e.datepicker._mainDivId).length&&e("body").append(e.datepicker.dpDiv);var i=Array.prototype.slice.call(arguments,1);return"string"!=typeof t||"isDisabled"!==t&&"getDate"!==t&&"widget"!==t?"option"===t&&2===arguments.length&&"string"==typeof arguments[1]?e.datepicker["_"+t+"Datepicker"].apply(e.datepicker,[this[0]].concat(i)):this.each(function(){"string"==typeof t?e.datepicker["_"+t+"Datepicker"].apply(e.datepicker,[this].concat(i)):e.datepicker._attachDatepicker(this,t)}):e.datepicker["_"+t+"Datepicker"].apply(e.datepicker,[this[0]].concat(i))},e.datepicker=new n,e.datepicker.initialized=!1,e.datepicker.uuid=(new Date).getTime(),e.datepicker.version="1.11.2",e.datepicker,e.widget("ui.draggable",e.ui.mouse,{version:"1.11.2",widgetEventPrefix:"drag",options:{addClasses:!0,appendTo:"parent",axis:!1,connectToSortable:!1,containment:!1,cursor:"auto",cursorAt:!1,grid:!1,handle:!1,helper:"original",iframeFix:!1,opacity:!1,refreshPositions:!1,revert:!1,revertDuration:500,scope:"default",scroll:!0,scrollSensitivity:20,scrollSpeed:20,snap:!1,snapMode:"both",snapTolerance:20,stack:!1,zIndex:!1,drag:null,start:null,stop:null},_create:function(){"original"===this.options.helper&&this._setPositionRelative(),this.options.addClasses&&this.element.addClass("ui-draggable"),this.options.disabled&&this.element.addClass("ui-draggable-disabled"),this._setHandleClassName(),this._mouseInit()},_setOption:function(e,t){this._super(e,t),"handle"===e&&(this._removeHandleClassName(),this._setHandleClassName())},_destroy:function(){return(this.helper||this.element).is(".ui-draggable-dragging")?(this.destroyOnClear=!0,void 0):(this.element.removeClass("ui-draggable ui-draggable-dragging ui-draggable-disabled"),this._removeHandleClassName(),this._mouseDestroy(),void 0)},_mouseCapture:function(t){var i=this.options;return this._blurActiveElement(t),this.helper||i.disabled||e(t.target).closest(".ui-resizable-handle").length>0?!1:(this.handle=this._getHandle(t),this.handle?(this._blockFrames(i.iframeFix===!0?"iframe":i.iframeFix),!0):!1)},_blockFrames:function(t){this.iframeBlocks=this.document.find(t).map(function(){var t=e(this);return e("<div>").css("position","absolute").appendTo(t.parent()).outerWidth(t.outerWidth()).outerHeight(t.outerHeight()).offset(t.offset())[0]})},_unblockFrames:function(){this.iframeBlocks&&(this.iframeBlocks.remove(),delete this.iframeBlocks)},_blurActiveElement:function(t){var i=this.document[0];if(this.handleElement.is(t.target))try{i.activeElement&&"body"!==i.activeElement.nodeName.toLowerCase()&&e(i.activeElement).blur()}catch(s){}},_mouseStart:function(t){var i=this.options;return this.helper=this._createHelper(t),this.helper.addClass("ui-draggable-dragging"),this._cacheHelperProportions(),e.ui.ddmanager&&(e.ui.ddmanager.current=this),this._cacheMargins(),this.cssPosition=this.helper.css("position"),this.scrollParent=this.helper.scrollParent(!0),this.offsetParent=this.helper.offsetParent(),this.hasFixedAncestor=this.helper.parents().filter(function(){return"fixed"===e(this).css("position")}).length>0,this.positionAbs=this.element.offset(),this._refreshOffsets(t),this.originalPosition=this.position=this._generatePosition(t,!1),this.originalPageX=t.pageX,this.originalPageY=t.pageY,i.cursorAt&&this._adjustOffsetFromHelper(i.cursorAt),this._setContainment(),this._trigger("start",t)===!1?(this._clear(),!1):(this._cacheHelperProportions(),e.ui.ddmanager&&!i.dropBehaviour&&e.ui.ddmanager.prepareOffsets(this,t),this._normalizeRightBottom(),this._mouseDrag(t,!0),e.ui.ddmanager&&e.ui.ddmanager.dragStart(this,t),!0)},_refreshOffsets:function(e){this.offset={top:this.positionAbs.top-this.margins.top,left:this.positionAbs.left-this.margins.left,scroll:!1,parent:this._getParentOffset(),relative:this._getRelativeOffset()},this.offset.click={left:e.pageX-this.offset.left,top:e.pageY-this.offset.top}},_mouseDrag:function(t,i){if(this.hasFixedAncestor&&(this.offset.parent=this._getParentOffset()),this.position=this._generatePosition(t,!0),this.positionAbs=this._convertPositionTo("absolute"),!i){var s=this._uiHash();if(this._trigger("drag",t,s)===!1)return this._mouseUp({}),!1;this.position=s.position}return this.helper[0].style.left=this.position.left+"px",this.helper[0].style.top=this.position.top+"px",e.ui.ddmanager&&e.ui.ddmanager.drag(this,t),!1},_mouseStop:function(t){var i=this,s=!1;return e.ui.ddmanager&&!this.options.dropBehaviour&&(s=e.ui.ddmanager.drop(this,t)),this.dropped&&(s=this.dropped,this.dropped=!1),"invalid"===this.options.revert&&!s||"valid"===this.options.revert&&s||this.options.revert===!0||e.isFunction(this.options.revert)&&this.options.revert.call(this.element,s)?e(this.helper).animate(this.originalPosition,parseInt(this.options.revertDuration,10),function(){i._trigger("stop",t)!==!1&&i._clear()}):this._trigger("stop",t)!==!1&&this._clear(),!1},_mouseUp:function(t){return this._unblockFrames(),e.ui.ddmanager&&e.ui.ddmanager.dragStop(this,t),this.handleElement.is(t.target)&&this.element.focus(),e.ui.mouse.prototype._mouseUp.call(this,t)},cancel:function(){return this.helper.is(".ui-draggable-dragging")?this._mouseUp({}):this._clear(),this},_getHandle:function(t){return this.options.handle?!!e(t.target).closest(this.element.find(this.options.handle)).length:!0},_setHandleClassName:function(){this.handleElement=this.options.handle?this.element.find(this.options.handle):this.element,this.handleElement.addClass("ui-draggable-handle")},_removeHandleClassName:function(){this.handleElement.removeClass("ui-draggable-handle")},_createHelper:function(t){var i=this.options,s=e.isFunction(i.helper),n=s?e(i.helper.apply(this.element[0],[t])):"clone"===i.helper?this.element.clone().removeAttr("id"):this.element;return n.parents("body").length||n.appendTo("parent"===i.appendTo?this.element[0].parentNode:i.appendTo),s&&n[0]===this.element[0]&&this._setPositionRelative(),n[0]===this.element[0]||/(fixed|absolute)/.test(n.css("position"))||n.css("position","absolute"),n},_setPositionRelative:function(){/^(?:r|a|f)/.test(this.element.css("position"))||(this.element[0].style.position="relative")},_adjustOffsetFromHelper:function(t){"string"==typeof t&&(t=t.split(" ")),e.isArray(t)&&(t={left:+t[0],top:+t[1]||0}),"left"in t&&(this.offset.click.left=t.left+this.margins.left),"right"in t&&(this.offset.click.left=this.helperProportions.width-t.right+this.margins.left),"top"in t&&(this.offset.click.top=t.top+this.margins.top),"bottom"in t&&(this.offset.click.top=this.helperProportions.height-t.bottom+this.margins.top)},_isRootNode:function(e){return/(html|body)/i.test(e.tagName)||e===this.document[0]},_getParentOffset:function(){var t=this.offsetParent.offset(),i=this.document[0];return"absolute"===this.cssPosition&&this.scrollParent[0]!==i&&e.contains(this.scrollParent[0],this.offsetParent[0])&&(t.left+=this.scrollParent.scrollLeft(),t.top+=this.scrollParent.scrollTop()),this._isRootNode(this.offsetParent[0])&&(t={top:0,left:0}),{top:t.top+(parseInt(this.offsetParent.css("borderTopWidth"),10)||0),left:t.left+(parseInt(this.offsetParent.css("borderLeftWidth"),10)||0)}},_getRelativeOffset:function(){if("relative"!==this.cssPosition)return{top:0,left:0};var e=this.element.position(),t=this._isRootNode(this.scrollParent[0]);return{top:e.top-(parseInt(this.helper.css("top"),10)||0)+(t?0:this.scrollParent.scrollTop()),left:e.left-(parseInt(this.helper.css("left"),10)||0)+(t?0:this.scrollParent.scrollLeft())}},_cacheMargins:function(){this.margins={left:parseInt(this.element.css("marginLeft"),10)||0,top:parseInt(this.element.css("marginTop"),10)||0,right:parseInt(this.element.css("marginRight"),10)||0,bottom:parseInt(this.element.css("marginBottom"),10)||0}},_cacheHelperProportions:function(){this.helperProportions={width:this.helper.outerWidth(),height:this.helper.outerHeight()}},_setContainment:function(){var t,i,s,n=this.options,a=this.document[0];return this.relativeContainer=null,n.containment?"window"===n.containment?(this.containment=[e(window).scrollLeft()-this.offset.relative.left-this.offset.parent.left,e(window).scrollTop()-this.offset.relative.top-this.offset.parent.top,e(window).scrollLeft()+e(window).width()-this.helperProportions.width-this.margins.left,e(window).scrollTop()+(e(window).height()||a.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top],void 0):"document"===n.containment?(this.containment=[0,0,e(a).width()-this.helperProportions.width-this.margins.left,(e(a).height()||a.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top],void 0):n.containment.constructor===Array?(this.containment=n.containment,void 0):("parent"===n.containment&&(n.containment=this.helper[0].parentNode),i=e(n.containment),s=i[0],s&&(t=/(scroll|auto)/.test(i.css("overflow")),this.containment=[(parseInt(i.css("borderLeftWidth"),10)||0)+(parseInt(i.css("paddingLeft"),10)||0),(parseInt(i.css("borderTopWidth"),10)||0)+(parseInt(i.css("paddingTop"),10)||0),(t?Math.max(s.scrollWidth,s.offsetWidth):s.offsetWidth)-(parseInt(i.css("borderRightWidth"),10)||0)-(parseInt(i.css("paddingRight"),10)||0)-this.helperProportions.width-this.margins.left-this.margins.right,(t?Math.max(s.scrollHeight,s.offsetHeight):s.offsetHeight)-(parseInt(i.css("borderBottomWidth"),10)||0)-(parseInt(i.css("paddingBottom"),10)||0)-this.helperProportions.height-this.margins.top-this.margins.bottom],this.relativeContainer=i),void 0):(this.containment=null,void 0)
},_convertPositionTo:function(e,t){t||(t=this.position);var i="absolute"===e?1:-1,s=this._isRootNode(this.scrollParent[0]);return{top:t.top+this.offset.relative.top*i+this.offset.parent.top*i-("fixed"===this.cssPosition?-this.offset.scroll.top:s?0:this.offset.scroll.top)*i,left:t.left+this.offset.relative.left*i+this.offset.parent.left*i-("fixed"===this.cssPosition?-this.offset.scroll.left:s?0:this.offset.scroll.left)*i}},_generatePosition:function(e,t){var i,s,n,a,o=this.options,r=this._isRootNode(this.scrollParent[0]),h=e.pageX,l=e.pageY;return r&&this.offset.scroll||(this.offset.scroll={top:this.scrollParent.scrollTop(),left:this.scrollParent.scrollLeft()}),t&&(this.containment&&(this.relativeContainer?(s=this.relativeContainer.offset(),i=[this.containment[0]+s.left,this.containment[1]+s.top,this.containment[2]+s.left,this.containment[3]+s.top]):i=this.containment,e.pageX-this.offset.click.left<i[0]&&(h=i[0]+this.offset.click.left),e.pageY-this.offset.click.top<i[1]&&(l=i[1]+this.offset.click.top),e.pageX-this.offset.click.left>i[2]&&(h=i[2]+this.offset.click.left),e.pageY-this.offset.click.top>i[3]&&(l=i[3]+this.offset.click.top)),o.grid&&(n=o.grid[1]?this.originalPageY+Math.round((l-this.originalPageY)/o.grid[1])*o.grid[1]:this.originalPageY,l=i?n-this.offset.click.top>=i[1]||n-this.offset.click.top>i[3]?n:n-this.offset.click.top>=i[1]?n-o.grid[1]:n+o.grid[1]:n,a=o.grid[0]?this.originalPageX+Math.round((h-this.originalPageX)/o.grid[0])*o.grid[0]:this.originalPageX,h=i?a-this.offset.click.left>=i[0]||a-this.offset.click.left>i[2]?a:a-this.offset.click.left>=i[0]?a-o.grid[0]:a+o.grid[0]:a),"y"===o.axis&&(h=this.originalPageX),"x"===o.axis&&(l=this.originalPageY)),{top:l-this.offset.click.top-this.offset.relative.top-this.offset.parent.top+("fixed"===this.cssPosition?-this.offset.scroll.top:r?0:this.offset.scroll.top),left:h-this.offset.click.left-this.offset.relative.left-this.offset.parent.left+("fixed"===this.cssPosition?-this.offset.scroll.left:r?0:this.offset.scroll.left)}},_clear:function(){this.helper.removeClass("ui-draggable-dragging"),this.helper[0]===this.element[0]||this.cancelHelperRemoval||this.helper.remove(),this.helper=null,this.cancelHelperRemoval=!1,this.destroyOnClear&&this.destroy()},_normalizeRightBottom:function(){"y"!==this.options.axis&&"auto"!==this.helper.css("right")&&(this.helper.width(this.helper.width()),this.helper.css("right","auto")),"x"!==this.options.axis&&"auto"!==this.helper.css("bottom")&&(this.helper.height(this.helper.height()),this.helper.css("bottom","auto"))},_trigger:function(t,i,s){return s=s||this._uiHash(),e.ui.plugin.call(this,t,[i,s,this],!0),/^(drag|start|stop)/.test(t)&&(this.positionAbs=this._convertPositionTo("absolute"),s.offset=this.positionAbs),e.Widget.prototype._trigger.call(this,t,i,s)},plugins:{},_uiHash:function(){return{helper:this.helper,position:this.position,originalPosition:this.originalPosition,offset:this.positionAbs}}}),e.ui.plugin.add("draggable","connectToSortable",{start:function(t,i,s){var n=e.extend({},i,{item:s.element});s.sortables=[],e(s.options.connectToSortable).each(function(){var i=e(this).sortable("instance");i&&!i.options.disabled&&(s.sortables.push(i),i.refreshPositions(),i._trigger("activate",t,n))})},stop:function(t,i,s){var n=e.extend({},i,{item:s.element});s.cancelHelperRemoval=!1,e.each(s.sortables,function(){var e=this;e.isOver?(e.isOver=0,s.cancelHelperRemoval=!0,e.cancelHelperRemoval=!1,e._storedCSS={position:e.placeholder.css("position"),top:e.placeholder.css("top"),left:e.placeholder.css("left")},e._mouseStop(t),e.options.helper=e.options._helper):(e.cancelHelperRemoval=!0,e._trigger("deactivate",t,n))})},drag:function(t,i,s){e.each(s.sortables,function(){var n=!1,a=this;a.positionAbs=s.positionAbs,a.helperProportions=s.helperProportions,a.offset.click=s.offset.click,a._intersectsWith(a.containerCache)&&(n=!0,e.each(s.sortables,function(){return this.positionAbs=s.positionAbs,this.helperProportions=s.helperProportions,this.offset.click=s.offset.click,this!==a&&this._intersectsWith(this.containerCache)&&e.contains(a.element[0],this.element[0])&&(n=!1),n})),n?(a.isOver||(a.isOver=1,a.currentItem=i.helper.appendTo(a.element).data("ui-sortable-item",!0),a.options._helper=a.options.helper,a.options.helper=function(){return i.helper[0]},t.target=a.currentItem[0],a._mouseCapture(t,!0),a._mouseStart(t,!0,!0),a.offset.click.top=s.offset.click.top,a.offset.click.left=s.offset.click.left,a.offset.parent.left-=s.offset.parent.left-a.offset.parent.left,a.offset.parent.top-=s.offset.parent.top-a.offset.parent.top,s._trigger("toSortable",t),s.dropped=a.element,e.each(s.sortables,function(){this.refreshPositions()}),s.currentItem=s.element,a.fromOutside=s),a.currentItem&&(a._mouseDrag(t),i.position=a.position)):a.isOver&&(a.isOver=0,a.cancelHelperRemoval=!0,a.options._revert=a.options.revert,a.options.revert=!1,a._trigger("out",t,a._uiHash(a)),a._mouseStop(t,!0),a.options.revert=a.options._revert,a.options.helper=a.options._helper,a.placeholder&&a.placeholder.remove(),s._refreshOffsets(t),i.position=s._generatePosition(t,!0),s._trigger("fromSortable",t),s.dropped=!1,e.each(s.sortables,function(){this.refreshPositions()}))})}}),e.ui.plugin.add("draggable","cursor",{start:function(t,i,s){var n=e("body"),a=s.options;n.css("cursor")&&(a._cursor=n.css("cursor")),n.css("cursor",a.cursor)},stop:function(t,i,s){var n=s.options;n._cursor&&e("body").css("cursor",n._cursor)}}),e.ui.plugin.add("draggable","opacity",{start:function(t,i,s){var n=e(i.helper),a=s.options;n.css("opacity")&&(a._opacity=n.css("opacity")),n.css("opacity",a.opacity)},stop:function(t,i,s){var n=s.options;n._opacity&&e(i.helper).css("opacity",n._opacity)}}),e.ui.plugin.add("draggable","scroll",{start:function(e,t,i){i.scrollParentNotHidden||(i.scrollParentNotHidden=i.helper.scrollParent(!1)),i.scrollParentNotHidden[0]!==i.document[0]&&"HTML"!==i.scrollParentNotHidden[0].tagName&&(i.overflowOffset=i.scrollParentNotHidden.offset())},drag:function(t,i,s){var n=s.options,a=!1,o=s.scrollParentNotHidden[0],r=s.document[0];o!==r&&"HTML"!==o.tagName?(n.axis&&"x"===n.axis||(s.overflowOffset.top+o.offsetHeight-t.pageY<n.scrollSensitivity?o.scrollTop=a=o.scrollTop+n.scrollSpeed:t.pageY-s.overflowOffset.top<n.scrollSensitivity&&(o.scrollTop=a=o.scrollTop-n.scrollSpeed)),n.axis&&"y"===n.axis||(s.overflowOffset.left+o.offsetWidth-t.pageX<n.scrollSensitivity?o.scrollLeft=a=o.scrollLeft+n.scrollSpeed:t.pageX-s.overflowOffset.left<n.scrollSensitivity&&(o.scrollLeft=a=o.scrollLeft-n.scrollSpeed))):(n.axis&&"x"===n.axis||(t.pageY-e(r).scrollTop()<n.scrollSensitivity?a=e(r).scrollTop(e(r).scrollTop()-n.scrollSpeed):e(window).height()-(t.pageY-e(r).scrollTop())<n.scrollSensitivity&&(a=e(r).scrollTop(e(r).scrollTop()+n.scrollSpeed))),n.axis&&"y"===n.axis||(t.pageX-e(r).scrollLeft()<n.scrollSensitivity?a=e(r).scrollLeft(e(r).scrollLeft()-n.scrollSpeed):e(window).width()-(t.pageX-e(r).scrollLeft())<n.scrollSensitivity&&(a=e(r).scrollLeft(e(r).scrollLeft()+n.scrollSpeed)))),a!==!1&&e.ui.ddmanager&&!n.dropBehaviour&&e.ui.ddmanager.prepareOffsets(s,t)}}),e.ui.plugin.add("draggable","snap",{start:function(t,i,s){var n=s.options;s.snapElements=[],e(n.snap.constructor!==String?n.snap.items||":data(ui-draggable)":n.snap).each(function(){var t=e(this),i=t.offset();this!==s.element[0]&&s.snapElements.push({item:this,width:t.outerWidth(),height:t.outerHeight(),top:i.top,left:i.left})})},drag:function(t,i,s){var n,a,o,r,h,l,u,d,c,p,f=s.options,m=f.snapTolerance,g=i.offset.left,v=g+s.helperProportions.width,y=i.offset.top,b=y+s.helperProportions.height;for(c=s.snapElements.length-1;c>=0;c--)h=s.snapElements[c].left-s.margins.left,l=h+s.snapElements[c].width,u=s.snapElements[c].top-s.margins.top,d=u+s.snapElements[c].height,h-m>v||g>l+m||u-m>b||y>d+m||!e.contains(s.snapElements[c].item.ownerDocument,s.snapElements[c].item)?(s.snapElements[c].snapping&&s.options.snap.release&&s.options.snap.release.call(s.element,t,e.extend(s._uiHash(),{snapItem:s.snapElements[c].item})),s.snapElements[c].snapping=!1):("inner"!==f.snapMode&&(n=m>=Math.abs(u-b),a=m>=Math.abs(d-y),o=m>=Math.abs(h-v),r=m>=Math.abs(l-g),n&&(i.position.top=s._convertPositionTo("relative",{top:u-s.helperProportions.height,left:0}).top),a&&(i.position.top=s._convertPositionTo("relative",{top:d,left:0}).top),o&&(i.position.left=s._convertPositionTo("relative",{top:0,left:h-s.helperProportions.width}).left),r&&(i.position.left=s._convertPositionTo("relative",{top:0,left:l}).left)),p=n||a||o||r,"outer"!==f.snapMode&&(n=m>=Math.abs(u-y),a=m>=Math.abs(d-b),o=m>=Math.abs(h-g),r=m>=Math.abs(l-v),n&&(i.position.top=s._convertPositionTo("relative",{top:u,left:0}).top),a&&(i.position.top=s._convertPositionTo("relative",{top:d-s.helperProportions.height,left:0}).top),o&&(i.position.left=s._convertPositionTo("relative",{top:0,left:h}).left),r&&(i.position.left=s._convertPositionTo("relative",{top:0,left:l-s.helperProportions.width}).left)),!s.snapElements[c].snapping&&(n||a||o||r||p)&&s.options.snap.snap&&s.options.snap.snap.call(s.element,t,e.extend(s._uiHash(),{snapItem:s.snapElements[c].item})),s.snapElements[c].snapping=n||a||o||r||p)}}),e.ui.plugin.add("draggable","stack",{start:function(t,i,s){var n,a=s.options,o=e.makeArray(e(a.stack)).sort(function(t,i){return(parseInt(e(t).css("zIndex"),10)||0)-(parseInt(e(i).css("zIndex"),10)||0)});o.length&&(n=parseInt(e(o[0]).css("zIndex"),10)||0,e(o).each(function(t){e(this).css("zIndex",n+t)}),this.css("zIndex",n+o.length))}}),e.ui.plugin.add("draggable","zIndex",{start:function(t,i,s){var n=e(i.helper),a=s.options;n.css("zIndex")&&(a._zIndex=n.css("zIndex")),n.css("zIndex",a.zIndex)},stop:function(t,i,s){var n=s.options;n._zIndex&&e(i.helper).css("zIndex",n._zIndex)}}),e.ui.draggable,e.widget("ui.resizable",e.ui.mouse,{version:"1.11.2",widgetEventPrefix:"resize",options:{alsoResize:!1,animate:!1,animateDuration:"slow",animateEasing:"swing",aspectRatio:!1,autoHide:!1,containment:!1,ghost:!1,grid:!1,handles:"e,s,se",helper:!1,maxHeight:null,maxWidth:null,minHeight:10,minWidth:10,zIndex:90,resize:null,start:null,stop:null},_num:function(e){return parseInt(e,10)||0},_isNumber:function(e){return!isNaN(parseInt(e,10))},_hasScroll:function(t,i){if("hidden"===e(t).css("overflow"))return!1;var s=i&&"left"===i?"scrollLeft":"scrollTop",n=!1;return t[s]>0?!0:(t[s]=1,n=t[s]>0,t[s]=0,n)},_create:function(){var t,i,s,n,a,o=this,r=this.options;if(this.element.addClass("ui-resizable"),e.extend(this,{_aspectRatio:!!r.aspectRatio,aspectRatio:r.aspectRatio,originalElement:this.element,_proportionallyResizeElements:[],_helper:r.helper||r.ghost||r.animate?r.helper||"ui-resizable-helper":null}),this.element[0].nodeName.match(/canvas|textarea|input|select|button|img/i)&&(this.element.wrap(e("<div class='ui-wrapper' style='overflow: hidden;'></div>").css({position:this.element.css("position"),width:this.element.outerWidth(),height:this.element.outerHeight(),top:this.element.css("top"),left:this.element.css("left")})),this.element=this.element.parent().data("ui-resizable",this.element.resizable("instance")),this.elementIsWrapper=!0,this.element.css({marginLeft:this.originalElement.css("marginLeft"),marginTop:this.originalElement.css("marginTop"),marginRight:this.originalElement.css("marginRight"),marginBottom:this.originalElement.css("marginBottom")}),this.originalElement.css({marginLeft:0,marginTop:0,marginRight:0,marginBottom:0}),this.originalResizeStyle=this.originalElement.css("resize"),this.originalElement.css("resize","none"),this._proportionallyResizeElements.push(this.originalElement.css({position:"static",zoom:1,display:"block"})),this.originalElement.css({margin:this.originalElement.css("margin")}),this._proportionallyResize()),this.handles=r.handles||(e(".ui-resizable-handle",this.element).length?{n:".ui-resizable-n",e:".ui-resizable-e",s:".ui-resizable-s",w:".ui-resizable-w",se:".ui-resizable-se",sw:".ui-resizable-sw",ne:".ui-resizable-ne",nw:".ui-resizable-nw"}:"e,s,se"),this.handles.constructor===String)for("all"===this.handles&&(this.handles="n,e,s,w,se,sw,ne,nw"),t=this.handles.split(","),this.handles={},i=0;t.length>i;i++)s=e.trim(t[i]),a="ui-resizable-"+s,n=e("<div class='ui-resizable-handle "+a+"'></div>"),n.css({zIndex:r.zIndex}),"se"===s&&n.addClass("ui-icon ui-icon-gripsmall-diagonal-se"),this.handles[s]=".ui-resizable-"+s,this.element.append(n);this._renderAxis=function(t){var i,s,n,a;t=t||this.element;for(i in this.handles)this.handles[i].constructor===String&&(this.handles[i]=this.element.children(this.handles[i]).first().show()),this.elementIsWrapper&&this.originalElement[0].nodeName.match(/textarea|input|select|button/i)&&(s=e(this.handles[i],this.element),a=/sw|ne|nw|se|n|s/.test(i)?s.outerHeight():s.outerWidth(),n=["padding",/ne|nw|n/.test(i)?"Top":/se|sw|s/.test(i)?"Bottom":/^e$/.test(i)?"Right":"Left"].join(""),t.css(n,a),this._proportionallyResize()),e(this.handles[i]).length},this._renderAxis(this.element),this._handles=e(".ui-resizable-handle",this.element).disableSelection(),this._handles.mouseover(function(){o.resizing||(this.className&&(n=this.className.match(/ui-resizable-(se|sw|ne|nw|n|e|s|w)/i)),o.axis=n&&n[1]?n[1]:"se")}),r.autoHide&&(this._handles.hide(),e(this.element).addClass("ui-resizable-autohide").mouseenter(function(){r.disabled||(e(this).removeClass("ui-resizable-autohide"),o._handles.show())}).mouseleave(function(){r.disabled||o.resizing||(e(this).addClass("ui-resizable-autohide"),o._handles.hide())})),this._mouseInit()},_destroy:function(){this._mouseDestroy();var t,i=function(t){e(t).removeClass("ui-resizable ui-resizable-disabled ui-resizable-resizing").removeData("resizable").removeData("ui-resizable").unbind(".resizable").find(".ui-resizable-handle").remove()};return this.elementIsWrapper&&(i(this.element),t=this.element,this.originalElement.css({position:t.css("position"),width:t.outerWidth(),height:t.outerHeight(),top:t.css("top"),left:t.css("left")}).insertAfter(t),t.remove()),this.originalElement.css("resize",this.originalResizeStyle),i(this.originalElement),this},_mouseCapture:function(t){var i,s,n=!1;for(i in this.handles)s=e(this.handles[i])[0],(s===t.target||e.contains(s,t.target))&&(n=!0);return!this.options.disabled&&n},_mouseStart:function(t){var i,s,n,a=this.options,o=this.element;return this.resizing=!0,this._renderProxy(),i=this._num(this.helper.css("left")),s=this._num(this.helper.css("top")),a.containment&&(i+=e(a.containment).scrollLeft()||0,s+=e(a.containment).scrollTop()||0),this.offset=this.helper.offset(),this.position={left:i,top:s},this.size=this._helper?{width:this.helper.width(),height:this.helper.height()}:{width:o.width(),height:o.height()},this.originalSize=this._helper?{width:o.outerWidth(),height:o.outerHeight()}:{width:o.width(),height:o.height()},this.sizeDiff={width:o.outerWidth()-o.width(),height:o.outerHeight()-o.height()},this.originalPosition={left:i,top:s},this.originalMousePosition={left:t.pageX,top:t.pageY},this.aspectRatio="number"==typeof a.aspectRatio?a.aspectRatio:this.originalSize.width/this.originalSize.height||1,n=e(".ui-resizable-"+this.axis).css("cursor"),e("body").css("cursor","auto"===n?this.axis+"-resize":n),o.addClass("ui-resizable-resizing"),this._propagate("start",t),!0},_mouseDrag:function(t){var i,s,n=this.originalMousePosition,a=this.axis,o=t.pageX-n.left||0,r=t.pageY-n.top||0,h=this._change[a];return this._updatePrevProperties(),h?(i=h.apply(this,[t,o,r]),this._updateVirtualBoundaries(t.shiftKey),(this._aspectRatio||t.shiftKey)&&(i=this._updateRatio(i,t)),i=this._respectSize(i,t),this._updateCache(i),this._propagate("resize",t),s=this._applyChanges(),!this._helper&&this._proportionallyResizeElements.length&&this._proportionallyResize(),e.isEmptyObject(s)||(this._updatePrevProperties(),this._trigger("resize",t,this.ui()),this._applyChanges()),!1):!1},_mouseStop:function(t){this.resizing=!1;var i,s,n,a,o,r,h,l=this.options,u=this;return this._helper&&(i=this._proportionallyResizeElements,s=i.length&&/textarea/i.test(i[0].nodeName),n=s&&this._hasScroll(i[0],"left")?0:u.sizeDiff.height,a=s?0:u.sizeDiff.width,o={width:u.helper.width()-a,height:u.helper.height()-n},r=parseInt(u.element.css("left"),10)+(u.position.left-u.originalPosition.left)||null,h=parseInt(u.element.css("top"),10)+(u.position.top-u.originalPosition.top)||null,l.animate||this.element.css(e.extend(o,{top:h,left:r})),u.helper.height(u.size.height),u.helper.width(u.size.width),this._helper&&!l.animate&&this._proportionallyResize()),e("body").css("cursor","auto"),this.element.removeClass("ui-resizable-resizing"),this._propagate("stop",t),this._helper&&this.helper.remove(),!1},_updatePrevProperties:function(){this.prevPosition={top:this.position.top,left:this.position.left},this.prevSize={width:this.size.width,height:this.size.height}},_applyChanges:function(){var e={};return this.position.top!==this.prevPosition.top&&(e.top=this.position.top+"px"),this.position.left!==this.prevPosition.left&&(e.left=this.position.left+"px"),this.size.width!==this.prevSize.width&&(e.width=this.size.width+"px"),this.size.height!==this.prevSize.height&&(e.height=this.size.height+"px"),this.helper.css(e),e},_updateVirtualBoundaries:function(e){var t,i,s,n,a,o=this.options;a={minWidth:this._isNumber(o.minWidth)?o.minWidth:0,maxWidth:this._isNumber(o.maxWidth)?o.maxWidth:1/0,minHeight:this._isNumber(o.minHeight)?o.minHeight:0,maxHeight:this._isNumber(o.maxHeight)?o.maxHeight:1/0},(this._aspectRatio||e)&&(t=a.minHeight*this.aspectRatio,s=a.minWidth/this.aspectRatio,i=a.maxHeight*this.aspectRatio,n=a.maxWidth/this.aspectRatio,t>a.minWidth&&(a.minWidth=t),s>a.minHeight&&(a.minHeight=s),a.maxWidth>i&&(a.maxWidth=i),a.maxHeight>n&&(a.maxHeight=n)),this._vBoundaries=a},_updateCache:function(e){this.offset=this.helper.offset(),this._isNumber(e.left)&&(this.position.left=e.left),this._isNumber(e.top)&&(this.position.top=e.top),this._isNumber(e.height)&&(this.size.height=e.height),this._isNumber(e.width)&&(this.size.width=e.width)},_updateRatio:function(e){var t=this.position,i=this.size,s=this.axis;return this._isNumber(e.height)?e.width=e.height*this.aspectRatio:this._isNumber(e.width)&&(e.height=e.width/this.aspectRatio),"sw"===s&&(e.left=t.left+(i.width-e.width),e.top=null),"nw"===s&&(e.top=t.top+(i.height-e.height),e.left=t.left+(i.width-e.width)),e},_respectSize:function(e){var t=this._vBoundaries,i=this.axis,s=this._isNumber(e.width)&&t.maxWidth&&t.maxWidth<e.width,n=this._isNumber(e.height)&&t.maxHeight&&t.maxHeight<e.height,a=this._isNumber(e.width)&&t.minWidth&&t.minWidth>e.width,o=this._isNumber(e.height)&&t.minHeight&&t.minHeight>e.height,r=this.originalPosition.left+this.originalSize.width,h=this.position.top+this.size.height,l=/sw|nw|w/.test(i),u=/nw|ne|n/.test(i);return a&&(e.width=t.minWidth),o&&(e.height=t.minHeight),s&&(e.width=t.maxWidth),n&&(e.height=t.maxHeight),a&&l&&(e.left=r-t.minWidth),s&&l&&(e.left=r-t.maxWidth),o&&u&&(e.top=h-t.minHeight),n&&u&&(e.top=h-t.maxHeight),e.width||e.height||e.left||!e.top?e.width||e.height||e.top||!e.left||(e.left=null):e.top=null,e},_getPaddingPlusBorderDimensions:function(e){for(var t=0,i=[],s=[e.css("borderTopWidth"),e.css("borderRightWidth"),e.css("borderBottomWidth"),e.css("borderLeftWidth")],n=[e.css("paddingTop"),e.css("paddingRight"),e.css("paddingBottom"),e.css("paddingLeft")];4>t;t++)i[t]=parseInt(s[t],10)||0,i[t]+=parseInt(n[t],10)||0;return{height:i[0]+i[2],width:i[1]+i[3]}},_proportionallyResize:function(){if(this._proportionallyResizeElements.length)for(var e,t=0,i=this.helper||this.element;this._proportionallyResizeElements.length>t;t++)e=this._proportionallyResizeElements[t],this.outerDimensions||(this.outerDimensions=this._getPaddingPlusBorderDimensions(e)),e.css({height:i.height()-this.outerDimensions.height||0,width:i.width()-this.outerDimensions.width||0})},_renderProxy:function(){var t=this.element,i=this.options;this.elementOffset=t.offset(),this._helper?(this.helper=this.helper||e("<div style='overflow:hidden;'></div>"),this.helper.addClass(this._helper).css({width:this.element.outerWidth()-1,height:this.element.outerHeight()-1,position:"absolute",left:this.elementOffset.left+"px",top:this.elementOffset.top+"px",zIndex:++i.zIndex}),this.helper.appendTo("body").disableSelection()):this.helper=this.element},_change:{e:function(e,t){return{width:this.originalSize.width+t}},w:function(e,t){var i=this.originalSize,s=this.originalPosition;return{left:s.left+t,width:i.width-t}},n:function(e,t,i){var s=this.originalSize,n=this.originalPosition;return{top:n.top+i,height:s.height-i}},s:function(e,t,i){return{height:this.originalSize.height+i}},se:function(t,i,s){return e.extend(this._change.s.apply(this,arguments),this._change.e.apply(this,[t,i,s]))},sw:function(t,i,s){return e.extend(this._change.s.apply(this,arguments),this._change.w.apply(this,[t,i,s]))},ne:function(t,i,s){return e.extend(this._change.n.apply(this,arguments),this._change.e.apply(this,[t,i,s]))},nw:function(t,i,s){return e.extend(this._change.n.apply(this,arguments),this._change.w.apply(this,[t,i,s]))}},_propagate:function(t,i){e.ui.plugin.call(this,t,[i,this.ui()]),"resize"!==t&&this._trigger(t,i,this.ui())},plugins:{},ui:function(){return{originalElement:this.originalElement,element:this.element,helper:this.helper,position:this.position,size:this.size,originalSize:this.originalSize,originalPosition:this.originalPosition}}}),e.ui.plugin.add("resizable","animate",{stop:function(t){var i=e(this).resizable("instance"),s=i.options,n=i._proportionallyResizeElements,a=n.length&&/textarea/i.test(n[0].nodeName),o=a&&i._hasScroll(n[0],"left")?0:i.sizeDiff.height,r=a?0:i.sizeDiff.width,h={width:i.size.width-r,height:i.size.height-o},l=parseInt(i.element.css("left"),10)+(i.position.left-i.originalPosition.left)||null,u=parseInt(i.element.css("top"),10)+(i.position.top-i.originalPosition.top)||null;i.element.animate(e.extend(h,u&&l?{top:u,left:l}:{}),{duration:s.animateDuration,easing:s.animateEasing,step:function(){var s={width:parseInt(i.element.css("width"),10),height:parseInt(i.element.css("height"),10),top:parseInt(i.element.css("top"),10),left:parseInt(i.element.css("left"),10)};n&&n.length&&e(n[0]).css({width:s.width,height:s.height}),i._updateCache(s),i._propagate("resize",t)}})}}),e.ui.plugin.add("resizable","containment",{start:function(){var t,i,s,n,a,o,r,h=e(this).resizable("instance"),l=h.options,u=h.element,d=l.containment,c=d instanceof e?d.get(0):/parent/.test(d)?u.parent().get(0):d;c&&(h.containerElement=e(c),/document/.test(d)||d===document?(h.containerOffset={left:0,top:0},h.containerPosition={left:0,top:0},h.parentData={element:e(document),left:0,top:0,width:e(document).width(),height:e(document).height()||document.body.parentNode.scrollHeight}):(t=e(c),i=[],e(["Top","Right","Left","Bottom"]).each(function(e,s){i[e]=h._num(t.css("padding"+s))}),h.containerOffset=t.offset(),h.containerPosition=t.position(),h.containerSize={height:t.innerHeight()-i[3],width:t.innerWidth()-i[1]},s=h.containerOffset,n=h.containerSize.height,a=h.containerSize.width,o=h._hasScroll(c,"left")?c.scrollWidth:a,r=h._hasScroll(c)?c.scrollHeight:n,h.parentData={element:c,left:s.left,top:s.top,width:o,height:r}))},resize:function(t){var i,s,n,a,o=e(this).resizable("instance"),r=o.options,h=o.containerOffset,l=o.position,u=o._aspectRatio||t.shiftKey,d={top:0,left:0},c=o.containerElement,p=!0;c[0]!==document&&/static/.test(c.css("position"))&&(d=h),l.left<(o._helper?h.left:0)&&(o.size.width=o.size.width+(o._helper?o.position.left-h.left:o.position.left-d.left),u&&(o.size.height=o.size.width/o.aspectRatio,p=!1),o.position.left=r.helper?h.left:0),l.top<(o._helper?h.top:0)&&(o.size.height=o.size.height+(o._helper?o.position.top-h.top:o.position.top),u&&(o.size.width=o.size.height*o.aspectRatio,p=!1),o.position.top=o._helper?h.top:0),n=o.containerElement.get(0)===o.element.parent().get(0),a=/relative|absolute/.test(o.containerElement.css("position")),n&&a?(o.offset.left=o.parentData.left+o.position.left,o.offset.top=o.parentData.top+o.position.top):(o.offset.left=o.element.offset().left,o.offset.top=o.element.offset().top),i=Math.abs(o.sizeDiff.width+(o._helper?o.offset.left-d.left:o.offset.left-h.left)),s=Math.abs(o.sizeDiff.height+(o._helper?o.offset.top-d.top:o.offset.top-h.top)),i+o.size.width>=o.parentData.width&&(o.size.width=o.parentData.width-i,u&&(o.size.height=o.size.width/o.aspectRatio,p=!1)),s+o.size.height>=o.parentData.height&&(o.size.height=o.parentData.height-s,u&&(o.size.width=o.size.height*o.aspectRatio,p=!1)),p||(o.position.left=o.prevPosition.left,o.position.top=o.prevPosition.top,o.size.width=o.prevSize.width,o.size.height=o.prevSize.height)},stop:function(){var t=e(this).resizable("instance"),i=t.options,s=t.containerOffset,n=t.containerPosition,a=t.containerElement,o=e(t.helper),r=o.offset(),h=o.outerWidth()-t.sizeDiff.width,l=o.outerHeight()-t.sizeDiff.height;t._helper&&!i.animate&&/relative/.test(a.css("position"))&&e(this).css({left:r.left-n.left-s.left,width:h,height:l}),t._helper&&!i.animate&&/static/.test(a.css("position"))&&e(this).css({left:r.left-n.left-s.left,width:h,height:l})}}),e.ui.plugin.add("resizable","alsoResize",{start:function(){var t=e(this).resizable("instance"),i=t.options,s=function(t){e(t).each(function(){var t=e(this);t.data("ui-resizable-alsoresize",{width:parseInt(t.width(),10),height:parseInt(t.height(),10),left:parseInt(t.css("left"),10),top:parseInt(t.css("top"),10)})})};"object"!=typeof i.alsoResize||i.alsoResize.parentNode?s(i.alsoResize):i.alsoResize.length?(i.alsoResize=i.alsoResize[0],s(i.alsoResize)):e.each(i.alsoResize,function(e){s(e)})},resize:function(t,i){var s=e(this).resizable("instance"),n=s.options,a=s.originalSize,o=s.originalPosition,r={height:s.size.height-a.height||0,width:s.size.width-a.width||0,top:s.position.top-o.top||0,left:s.position.left-o.left||0},h=function(t,s){e(t).each(function(){var t=e(this),n=e(this).data("ui-resizable-alsoresize"),a={},o=s&&s.length?s:t.parents(i.originalElement[0]).length?["width","height"]:["width","height","top","left"];e.each(o,function(e,t){var i=(n[t]||0)+(r[t]||0);i&&i>=0&&(a[t]=i||null)}),t.css(a)})};"object"!=typeof n.alsoResize||n.alsoResize.nodeType?h(n.alsoResize):e.each(n.alsoResize,function(e,t){h(e,t)})},stop:function(){e(this).removeData("resizable-alsoresize")}}),e.ui.plugin.add("resizable","ghost",{start:function(){var t=e(this).resizable("instance"),i=t.options,s=t.size;t.ghost=t.originalElement.clone(),t.ghost.css({opacity:.25,display:"block",position:"relative",height:s.height,width:s.width,margin:0,left:0,top:0}).addClass("ui-resizable-ghost").addClass("string"==typeof i.ghost?i.ghost:""),t.ghost.appendTo(t.helper)},resize:function(){var t=e(this).resizable("instance");t.ghost&&t.ghost.css({position:"relative",height:t.size.height,width:t.size.width})},stop:function(){var t=e(this).resizable("instance");t.ghost&&t.helper&&t.helper.get(0).removeChild(t.ghost.get(0))}}),e.ui.plugin.add("resizable","grid",{resize:function(){var t,i=e(this).resizable("instance"),s=i.options,n=i.size,a=i.originalSize,o=i.originalPosition,r=i.axis,h="number"==typeof s.grid?[s.grid,s.grid]:s.grid,l=h[0]||1,u=h[1]||1,d=Math.round((n.width-a.width)/l)*l,c=Math.round((n.height-a.height)/u)*u,p=a.width+d,f=a.height+c,m=s.maxWidth&&p>s.maxWidth,g=s.maxHeight&&f>s.maxHeight,v=s.minWidth&&s.minWidth>p,y=s.minHeight&&s.minHeight>f;s.grid=h,v&&(p+=l),y&&(f+=u),m&&(p-=l),g&&(f-=u),/^(se|s|e)$/.test(r)?(i.size.width=p,i.size.height=f):/^(ne)$/.test(r)?(i.size.width=p,i.size.height=f,i.position.top=o.top-c):/^(sw)$/.test(r)?(i.size.width=p,i.size.height=f,i.position.left=o.left-d):((0>=f-u||0>=p-l)&&(t=i._getPaddingPlusBorderDimensions(this)),f-u>0?(i.size.height=f,i.position.top=o.top-c):(f=u-t.height,i.size.height=f,i.position.top=o.top+a.height-f),p-l>0?(i.size.width=p,i.position.left=o.left-d):(p=u-t.height,i.size.width=p,i.position.left=o.left+a.width-p))}}),e.ui.resizable,e.widget("ui.dialog",{version:"1.11.2",options:{appendTo:"body",autoOpen:!0,buttons:[],closeOnEscape:!0,closeText:"Close",dialogClass:"",draggable:!0,hide:null,height:"auto",maxHeight:null,maxWidth:null,minHeight:150,minWidth:150,modal:!1,position:{my:"center",at:"center",of:window,collision:"fit",using:function(t){var i=e(this).css(t).offset().top;0>i&&e(this).css("top",t.top-i)}},resizable:!0,show:null,title:null,width:300,beforeClose:null,close:null,drag:null,dragStart:null,dragStop:null,focus:null,open:null,resize:null,resizeStart:null,resizeStop:null},sizeRelatedOptions:{buttons:!0,height:!0,maxHeight:!0,maxWidth:!0,minHeight:!0,minWidth:!0,width:!0},resizableRelatedOptions:{maxHeight:!0,maxWidth:!0,minHeight:!0,minWidth:!0},_create:function(){this.originalCss={display:this.element[0].style.display,width:this.element[0].style.width,minHeight:this.element[0].style.minHeight,maxHeight:this.element[0].style.maxHeight,height:this.element[0].style.height},this.originalPosition={parent:this.element.parent(),index:this.element.parent().children().index(this.element)},this.originalTitle=this.element.attr("title"),this.options.title=this.options.title||this.originalTitle,this._createWrapper(),this.element.show().removeAttr("title").addClass("ui-dialog-content ui-widget-content").appendTo(this.uiDialog),this._createTitlebar(),this._createButtonPane(),this.options.draggable&&e.fn.draggable&&this._makeDraggable(),this.options.resizable&&e.fn.resizable&&this._makeResizable(),this._isOpen=!1,this._trackFocus()},_init:function(){this.options.autoOpen&&this.open()},_appendTo:function(){var t=this.options.appendTo;return t&&(t.jquery||t.nodeType)?e(t):this.document.find(t||"body").eq(0)},_destroy:function(){var e,t=this.originalPosition;this._destroyOverlay(),this.element.removeUniqueId().removeClass("ui-dialog-content ui-widget-content").css(this.originalCss).detach(),this.uiDialog.stop(!0,!0).remove(),this.originalTitle&&this.element.attr("title",this.originalTitle),e=t.parent.children().eq(t.index),e.length&&e[0]!==this.element[0]?e.before(this.element):t.parent.append(this.element)},widget:function(){return this.uiDialog},disable:e.noop,enable:e.noop,close:function(t){var i,s=this;if(this._isOpen&&this._trigger("beforeClose",t)!==!1){if(this._isOpen=!1,this._focusedElement=null,this._destroyOverlay(),this._untrackInstance(),!this.opener.filter(":focusable").focus().length)try{i=this.document[0].activeElement,i&&"body"!==i.nodeName.toLowerCase()&&e(i).blur()}catch(n){}this._hide(this.uiDialog,this.options.hide,function(){s._trigger("close",t)})}},isOpen:function(){return this._isOpen},moveToTop:function(){this._moveToTop()},_moveToTop:function(t,i){var s=!1,n=this.uiDialog.siblings(".ui-front:visible").map(function(){return+e(this).css("z-index")}).get(),a=Math.max.apply(null,n);return a>=+this.uiDialog.css("z-index")&&(this.uiDialog.css("z-index",a+1),s=!0),s&&!i&&this._trigger("focus",t),s},open:function(){var t=this;return this._isOpen?(this._moveToTop()&&this._focusTabbable(),void 0):(this._isOpen=!0,this.opener=e(this.document[0].activeElement),this._size(),this._position(),this._createOverlay(),this._moveToTop(null,!0),this.overlay&&this.overlay.css("z-index",this.uiDialog.css("z-index")-1),this._show(this.uiDialog,this.options.show,function(){t._focusTabbable(),t._trigger("focus")}),this._makeFocusTarget(),this._trigger("open"),void 0)},_focusTabbable:function(){var e=this._focusedElement;e||(e=this.element.find("[autofocus]")),e.length||(e=this.element.find(":tabbable")),e.length||(e=this.uiDialogButtonPane.find(":tabbable")),e.length||(e=this.uiDialogTitlebarClose.filter(":tabbable")),e.length||(e=this.uiDialog),e.eq(0).focus()},_keepFocus:function(t){function i(){var t=this.document[0].activeElement,i=this.uiDialog[0]===t||e.contains(this.uiDialog[0],t);i||this._focusTabbable()}t.preventDefault(),i.call(this),this._delay(i)},_createWrapper:function(){this.uiDialog=e("<div>").addClass("ui-dialog ui-widget ui-widget-content ui-corner-all ui-front "+this.options.dialogClass).hide().attr({tabIndex:-1,role:"dialog"}).appendTo(this._appendTo()),this._on(this.uiDialog,{keydown:function(t){if(this.options.closeOnEscape&&!t.isDefaultPrevented()&&t.keyCode&&t.keyCode===e.ui.keyCode.ESCAPE)return t.preventDefault(),this.close(t),void 0;
if(t.keyCode===e.ui.keyCode.TAB&&!t.isDefaultPrevented()){var i=this.uiDialog.find(":tabbable"),s=i.filter(":first"),n=i.filter(":last");t.target!==n[0]&&t.target!==this.uiDialog[0]||t.shiftKey?t.target!==s[0]&&t.target!==this.uiDialog[0]||!t.shiftKey||(this._delay(function(){n.focus()}),t.preventDefault()):(this._delay(function(){s.focus()}),t.preventDefault())}},mousedown:function(e){this._moveToTop(e)&&this._focusTabbable()}}),this.element.find("[aria-describedby]").length||this.uiDialog.attr({"aria-describedby":this.element.uniqueId().attr("id")})},_createTitlebar:function(){var t;this.uiDialogTitlebar=e("<div>").addClass("ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix").prependTo(this.uiDialog),this._on(this.uiDialogTitlebar,{mousedown:function(t){e(t.target).closest(".ui-dialog-titlebar-close")||this.uiDialog.focus()}}),this.uiDialogTitlebarClose=e("<button type='button'></button>").button({label:this.options.closeText,icons:{primary:"ui-icon-closethick"},text:!1}).addClass("ui-dialog-titlebar-close").appendTo(this.uiDialogTitlebar),this._on(this.uiDialogTitlebarClose,{click:function(e){e.preventDefault(),this.close(e)}}),t=e("<span>").uniqueId().addClass("ui-dialog-title").prependTo(this.uiDialogTitlebar),this._title(t),this.uiDialog.attr({"aria-labelledby":t.attr("id")})},_title:function(e){this.options.title||e.html("&#160;"),e.text(this.options.title)},_createButtonPane:function(){this.uiDialogButtonPane=e("<div>").addClass("ui-dialog-buttonpane ui-widget-content ui-helper-clearfix"),this.uiButtonSet=e("<div>").addClass("ui-dialog-buttonset").appendTo(this.uiDialogButtonPane),this._createButtons()},_createButtons:function(){var t=this,i=this.options.buttons;return this.uiDialogButtonPane.remove(),this.uiButtonSet.empty(),e.isEmptyObject(i)||e.isArray(i)&&!i.length?(this.uiDialog.removeClass("ui-dialog-buttons"),void 0):(e.each(i,function(i,s){var n,a;s=e.isFunction(s)?{click:s,text:i}:s,s=e.extend({type:"button"},s),n=s.click,s.click=function(){n.apply(t.element[0],arguments)},a={icons:s.icons,text:s.showText},delete s.icons,delete s.showText,e("<button></button>",s).button(a).appendTo(t.uiButtonSet)}),this.uiDialog.addClass("ui-dialog-buttons"),this.uiDialogButtonPane.appendTo(this.uiDialog),void 0)},_makeDraggable:function(){function t(e){return{position:e.position,offset:e.offset}}var i=this,s=this.options;this.uiDialog.draggable({cancel:".ui-dialog-content, .ui-dialog-titlebar-close",handle:".ui-dialog-titlebar",containment:"document",start:function(s,n){e(this).addClass("ui-dialog-dragging"),i._blockFrames(),i._trigger("dragStart",s,t(n))},drag:function(e,s){i._trigger("drag",e,t(s))},stop:function(n,a){var o=a.offset.left-i.document.scrollLeft(),r=a.offset.top-i.document.scrollTop();s.position={my:"left top",at:"left"+(o>=0?"+":"")+o+" "+"top"+(r>=0?"+":"")+r,of:i.window},e(this).removeClass("ui-dialog-dragging"),i._unblockFrames(),i._trigger("dragStop",n,t(a))}})},_makeResizable:function(){function t(e){return{originalPosition:e.originalPosition,originalSize:e.originalSize,position:e.position,size:e.size}}var i=this,s=this.options,n=s.resizable,a=this.uiDialog.css("position"),o="string"==typeof n?n:"n,e,s,w,se,sw,ne,nw";this.uiDialog.resizable({cancel:".ui-dialog-content",containment:"document",alsoResize:this.element,maxWidth:s.maxWidth,maxHeight:s.maxHeight,minWidth:s.minWidth,minHeight:this._minHeight(),handles:o,start:function(s,n){e(this).addClass("ui-dialog-resizing"),i._blockFrames(),i._trigger("resizeStart",s,t(n))},resize:function(e,s){i._trigger("resize",e,t(s))},stop:function(n,a){var o=i.uiDialog.offset(),r=o.left-i.document.scrollLeft(),h=o.top-i.document.scrollTop();s.height=i.uiDialog.height(),s.width=i.uiDialog.width(),s.position={my:"left top",at:"left"+(r>=0?"+":"")+r+" "+"top"+(h>=0?"+":"")+h,of:i.window},e(this).removeClass("ui-dialog-resizing"),i._unblockFrames(),i._trigger("resizeStop",n,t(a))}}).css("position",a)},_trackFocus:function(){this._on(this.widget(),{focusin:function(t){this._makeFocusTarget(),this._focusedElement=e(t.target)}})},_makeFocusTarget:function(){this._untrackInstance(),this._trackingInstances().unshift(this)},_untrackInstance:function(){var t=this._trackingInstances(),i=e.inArray(this,t);-1!==i&&t.splice(i,1)},_trackingInstances:function(){var e=this.document.data("ui-dialog-instances");return e||(e=[],this.document.data("ui-dialog-instances",e)),e},_minHeight:function(){var e=this.options;return"auto"===e.height?e.minHeight:Math.min(e.minHeight,e.height)},_position:function(){var e=this.uiDialog.is(":visible");e||this.uiDialog.show(),this.uiDialog.position(this.options.position),e||this.uiDialog.hide()},_setOptions:function(t){var i=this,s=!1,n={};e.each(t,function(e,t){i._setOption(e,t),e in i.sizeRelatedOptions&&(s=!0),e in i.resizableRelatedOptions&&(n[e]=t)}),s&&(this._size(),this._position()),this.uiDialog.is(":data(ui-resizable)")&&this.uiDialog.resizable("option",n)},_setOption:function(e,t){var i,s,n=this.uiDialog;"dialogClass"===e&&n.removeClass(this.options.dialogClass).addClass(t),"disabled"!==e&&(this._super(e,t),"appendTo"===e&&this.uiDialog.appendTo(this._appendTo()),"buttons"===e&&this._createButtons(),"closeText"===e&&this.uiDialogTitlebarClose.button({label:""+t}),"draggable"===e&&(i=n.is(":data(ui-draggable)"),i&&!t&&n.draggable("destroy"),!i&&t&&this._makeDraggable()),"position"===e&&this._position(),"resizable"===e&&(s=n.is(":data(ui-resizable)"),s&&!t&&n.resizable("destroy"),s&&"string"==typeof t&&n.resizable("option","handles",t),s||t===!1||this._makeResizable()),"title"===e&&this._title(this.uiDialogTitlebar.find(".ui-dialog-title")))},_size:function(){var e,t,i,s=this.options;this.element.show().css({width:"auto",minHeight:0,maxHeight:"none",height:0}),s.minWidth>s.width&&(s.width=s.minWidth),e=this.uiDialog.css({height:"auto",width:s.width}).outerHeight(),t=Math.max(0,s.minHeight-e),i="number"==typeof s.maxHeight?Math.max(0,s.maxHeight-e):"none","auto"===s.height?this.element.css({minHeight:t,maxHeight:i,height:"auto"}):this.element.height(Math.max(0,s.height-e)),this.uiDialog.is(":data(ui-resizable)")&&this.uiDialog.resizable("option","minHeight",this._minHeight())},_blockFrames:function(){this.iframeBlocks=this.document.find("iframe").map(function(){var t=e(this);return e("<div>").css({position:"absolute",width:t.outerWidth(),height:t.outerHeight()}).appendTo(t.parent()).offset(t.offset())[0]})},_unblockFrames:function(){this.iframeBlocks&&(this.iframeBlocks.remove(),delete this.iframeBlocks)},_allowInteraction:function(t){return e(t.target).closest(".ui-dialog").length?!0:!!e(t.target).closest(".ui-datepicker").length},_createOverlay:function(){if(this.options.modal){var t=!0;this._delay(function(){t=!1}),this.document.data("ui-dialog-overlays")||this._on(this.document,{focusin:function(e){t||this._allowInteraction(e)||(e.preventDefault(),this._trackingInstances()[0]._focusTabbable())}}),this.overlay=e("<div>").addClass("ui-widget-overlay ui-front").appendTo(this._appendTo()),this._on(this.overlay,{mousedown:"_keepFocus"}),this.document.data("ui-dialog-overlays",(this.document.data("ui-dialog-overlays")||0)+1)}},_destroyOverlay:function(){if(this.options.modal&&this.overlay){var e=this.document.data("ui-dialog-overlays")-1;e?this.document.data("ui-dialog-overlays",e):this.document.unbind("focusin").removeData("ui-dialog-overlays"),this.overlay.remove(),this.overlay=null}}}),e.widget("ui.droppable",{version:"1.11.2",widgetEventPrefix:"drop",options:{accept:"*",activeClass:!1,addClasses:!0,greedy:!1,hoverClass:!1,scope:"default",tolerance:"intersect",activate:null,deactivate:null,drop:null,out:null,over:null},_create:function(){var t,i=this.options,s=i.accept;this.isover=!1,this.isout=!0,this.accept=e.isFunction(s)?s:function(e){return e.is(s)},this.proportions=function(){return arguments.length?(t=arguments[0],void 0):t?t:t={width:this.element[0].offsetWidth,height:this.element[0].offsetHeight}},this._addToManager(i.scope),i.addClasses&&this.element.addClass("ui-droppable")},_addToManager:function(t){e.ui.ddmanager.droppables[t]=e.ui.ddmanager.droppables[t]||[],e.ui.ddmanager.droppables[t].push(this)},_splice:function(e){for(var t=0;e.length>t;t++)e[t]===this&&e.splice(t,1)},_destroy:function(){var t=e.ui.ddmanager.droppables[this.options.scope];this._splice(t),this.element.removeClass("ui-droppable ui-droppable-disabled")},_setOption:function(t,i){if("accept"===t)this.accept=e.isFunction(i)?i:function(e){return e.is(i)};else if("scope"===t){var s=e.ui.ddmanager.droppables[this.options.scope];this._splice(s),this._addToManager(i)}this._super(t,i)},_activate:function(t){var i=e.ui.ddmanager.current;this.options.activeClass&&this.element.addClass(this.options.activeClass),i&&this._trigger("activate",t,this.ui(i))},_deactivate:function(t){var i=e.ui.ddmanager.current;this.options.activeClass&&this.element.removeClass(this.options.activeClass),i&&this._trigger("deactivate",t,this.ui(i))},_over:function(t){var i=e.ui.ddmanager.current;i&&(i.currentItem||i.element)[0]!==this.element[0]&&this.accept.call(this.element[0],i.currentItem||i.element)&&(this.options.hoverClass&&this.element.addClass(this.options.hoverClass),this._trigger("over",t,this.ui(i)))},_out:function(t){var i=e.ui.ddmanager.current;i&&(i.currentItem||i.element)[0]!==this.element[0]&&this.accept.call(this.element[0],i.currentItem||i.element)&&(this.options.hoverClass&&this.element.removeClass(this.options.hoverClass),this._trigger("out",t,this.ui(i)))},_drop:function(t,i){var s=i||e.ui.ddmanager.current,n=!1;return s&&(s.currentItem||s.element)[0]!==this.element[0]?(this.element.find(":data(ui-droppable)").not(".ui-draggable-dragging").each(function(){var i=e(this).droppable("instance");return i.options.greedy&&!i.options.disabled&&i.options.scope===s.options.scope&&i.accept.call(i.element[0],s.currentItem||s.element)&&e.ui.intersect(s,e.extend(i,{offset:i.element.offset()}),i.options.tolerance,t)?(n=!0,!1):void 0}),n?!1:this.accept.call(this.element[0],s.currentItem||s.element)?(this.options.activeClass&&this.element.removeClass(this.options.activeClass),this.options.hoverClass&&this.element.removeClass(this.options.hoverClass),this._trigger("drop",t,this.ui(s)),this.element):!1):!1},ui:function(e){return{draggable:e.currentItem||e.element,helper:e.helper,position:e.position,offset:e.positionAbs}}}),e.ui.intersect=function(){function e(e,t,i){return e>=t&&t+i>e}return function(t,i,s,n){if(!i.offset)return!1;var a=(t.positionAbs||t.position.absolute).left+t.margins.left,o=(t.positionAbs||t.position.absolute).top+t.margins.top,r=a+t.helperProportions.width,h=o+t.helperProportions.height,l=i.offset.left,u=i.offset.top,d=l+i.proportions().width,c=u+i.proportions().height;switch(s){case"fit":return a>=l&&d>=r&&o>=u&&c>=h;case"intersect":return a+t.helperProportions.width/2>l&&d>r-t.helperProportions.width/2&&o+t.helperProportions.height/2>u&&c>h-t.helperProportions.height/2;case"pointer":return e(n.pageY,u,i.proportions().height)&&e(n.pageX,l,i.proportions().width);case"touch":return(o>=u&&c>=o||h>=u&&c>=h||u>o&&h>c)&&(a>=l&&d>=a||r>=l&&d>=r||l>a&&r>d);default:return!1}}}(),e.ui.ddmanager={current:null,droppables:{"default":[]},prepareOffsets:function(t,i){var s,n,a=e.ui.ddmanager.droppables[t.options.scope]||[],o=i?i.type:null,r=(t.currentItem||t.element).find(":data(ui-droppable)").addBack();e:for(s=0;a.length>s;s++)if(!(a[s].options.disabled||t&&!a[s].accept.call(a[s].element[0],t.currentItem||t.element))){for(n=0;r.length>n;n++)if(r[n]===a[s].element[0]){a[s].proportions().height=0;continue e}a[s].visible="none"!==a[s].element.css("display"),a[s].visible&&("mousedown"===o&&a[s]._activate.call(a[s],i),a[s].offset=a[s].element.offset(),a[s].proportions({width:a[s].element[0].offsetWidth,height:a[s].element[0].offsetHeight}))}},drop:function(t,i){var s=!1;return e.each((e.ui.ddmanager.droppables[t.options.scope]||[]).slice(),function(){this.options&&(!this.options.disabled&&this.visible&&e.ui.intersect(t,this,this.options.tolerance,i)&&(s=this._drop.call(this,i)||s),!this.options.disabled&&this.visible&&this.accept.call(this.element[0],t.currentItem||t.element)&&(this.isout=!0,this.isover=!1,this._deactivate.call(this,i)))}),s},dragStart:function(t,i){t.element.parentsUntil("body").bind("scroll.droppable",function(){t.options.refreshPositions||e.ui.ddmanager.prepareOffsets(t,i)})},drag:function(t,i){t.options.refreshPositions&&e.ui.ddmanager.prepareOffsets(t,i),e.each(e.ui.ddmanager.droppables[t.options.scope]||[],function(){if(!this.options.disabled&&!this.greedyChild&&this.visible){var s,n,a,o=e.ui.intersect(t,this,this.options.tolerance,i),r=!o&&this.isover?"isout":o&&!this.isover?"isover":null;r&&(this.options.greedy&&(n=this.options.scope,a=this.element.parents(":data(ui-droppable)").filter(function(){return e(this).droppable("instance").options.scope===n}),a.length&&(s=e(a[0]).droppable("instance"),s.greedyChild="isover"===r)),s&&"isover"===r&&(s.isover=!1,s.isout=!0,s._out.call(s,i)),this[r]=!0,this["isout"===r?"isover":"isout"]=!1,this["isover"===r?"_over":"_out"].call(this,i),s&&"isout"===r&&(s.isout=!1,s.isover=!0,s._over.call(s,i)))}})},dragStop:function(t,i){t.element.parentsUntil("body").unbind("scroll.droppable"),t.options.refreshPositions||e.ui.ddmanager.prepareOffsets(t,i)}},e.ui.droppable;var y="ui-effects-",b=e;e.effects={effect:{}},function(e,t){function i(e,t,i){var s=d[t.type]||{};return null==e?i||!t.def?null:t.def:(e=s.floor?~~e:parseFloat(e),isNaN(e)?t.def:s.mod?(e+s.mod)%s.mod:0>e?0:e>s.max?s.max:e)}function s(i){var s=l(),n=s._rgba=[];return i=i.toLowerCase(),f(h,function(e,a){var o,r=a.re.exec(i),h=r&&a.parse(r),l=a.space||"rgba";return h?(o=s[l](h),s[u[l].cache]=o[u[l].cache],n=s._rgba=o._rgba,!1):t}),n.length?("0,0,0,0"===n.join()&&e.extend(n,a.transparent),s):a[i]}function n(e,t,i){return i=(i+1)%1,1>6*i?e+6*(t-e)*i:1>2*i?t:2>3*i?e+6*(t-e)*(2/3-i):e}var a,o="backgroundColor borderBottomColor borderLeftColor borderRightColor borderTopColor color columnRuleColor outlineColor textDecorationColor textEmphasisColor",r=/^([\-+])=\s*(\d+\.?\d*)/,h=[{re:/rgba?\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,parse:function(e){return[e[1],e[2],e[3],e[4]]}},{re:/rgba?\(\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,parse:function(e){return[2.55*e[1],2.55*e[2],2.55*e[3],e[4]]}},{re:/#([a-f0-9]{2})([a-f0-9]{2})([a-f0-9]{2})/,parse:function(e){return[parseInt(e[1],16),parseInt(e[2],16),parseInt(e[3],16)]}},{re:/#([a-f0-9])([a-f0-9])([a-f0-9])/,parse:function(e){return[parseInt(e[1]+e[1],16),parseInt(e[2]+e[2],16),parseInt(e[3]+e[3],16)]}},{re:/hsla?\(\s*(\d+(?:\.\d+)?)\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,space:"hsla",parse:function(e){return[e[1],e[2]/100,e[3]/100,e[4]]}}],l=e.Color=function(t,i,s,n){return new e.Color.fn.parse(t,i,s,n)},u={rgba:{props:{red:{idx:0,type:"byte"},green:{idx:1,type:"byte"},blue:{idx:2,type:"byte"}}},hsla:{props:{hue:{idx:0,type:"degrees"},saturation:{idx:1,type:"percent"},lightness:{idx:2,type:"percent"}}}},d={"byte":{floor:!0,max:255},percent:{max:1},degrees:{mod:360,floor:!0}},c=l.support={},p=e("<p>")[0],f=e.each;p.style.cssText="background-color:rgba(1,1,1,.5)",c.rgba=p.style.backgroundColor.indexOf("rgba")>-1,f(u,function(e,t){t.cache="_"+e,t.props.alpha={idx:3,type:"percent",def:1}}),l.fn=e.extend(l.prototype,{parse:function(n,o,r,h){if(n===t)return this._rgba=[null,null,null,null],this;(n.jquery||n.nodeType)&&(n=e(n).css(o),o=t);var d=this,c=e.type(n),p=this._rgba=[];return o!==t&&(n=[n,o,r,h],c="array"),"string"===c?this.parse(s(n)||a._default):"array"===c?(f(u.rgba.props,function(e,t){p[t.idx]=i(n[t.idx],t)}),this):"object"===c?(n instanceof l?f(u,function(e,t){n[t.cache]&&(d[t.cache]=n[t.cache].slice())}):f(u,function(t,s){var a=s.cache;f(s.props,function(e,t){if(!d[a]&&s.to){if("alpha"===e||null==n[e])return;d[a]=s.to(d._rgba)}d[a][t.idx]=i(n[e],t,!0)}),d[a]&&0>e.inArray(null,d[a].slice(0,3))&&(d[a][3]=1,s.from&&(d._rgba=s.from(d[a])))}),this):t},is:function(e){var i=l(e),s=!0,n=this;return f(u,function(e,a){var o,r=i[a.cache];return r&&(o=n[a.cache]||a.to&&a.to(n._rgba)||[],f(a.props,function(e,i){return null!=r[i.idx]?s=r[i.idx]===o[i.idx]:t})),s}),s},_space:function(){var e=[],t=this;return f(u,function(i,s){t[s.cache]&&e.push(i)}),e.pop()},transition:function(e,t){var s=l(e),n=s._space(),a=u[n],o=0===this.alpha()?l("transparent"):this,r=o[a.cache]||a.to(o._rgba),h=r.slice();return s=s[a.cache],f(a.props,function(e,n){var a=n.idx,o=r[a],l=s[a],u=d[n.type]||{};null!==l&&(null===o?h[a]=l:(u.mod&&(l-o>u.mod/2?o+=u.mod:o-l>u.mod/2&&(o-=u.mod)),h[a]=i((l-o)*t+o,n)))}),this[n](h)},blend:function(t){if(1===this._rgba[3])return this;var i=this._rgba.slice(),s=i.pop(),n=l(t)._rgba;return l(e.map(i,function(e,t){return(1-s)*n[t]+s*e}))},toRgbaString:function(){var t="rgba(",i=e.map(this._rgba,function(e,t){return null==e?t>2?1:0:e});return 1===i[3]&&(i.pop(),t="rgb("),t+i.join()+")"},toHslaString:function(){var t="hsla(",i=e.map(this.hsla(),function(e,t){return null==e&&(e=t>2?1:0),t&&3>t&&(e=Math.round(100*e)+"%"),e});return 1===i[3]&&(i.pop(),t="hsl("),t+i.join()+")"},toHexString:function(t){var i=this._rgba.slice(),s=i.pop();return t&&i.push(~~(255*s)),"#"+e.map(i,function(e){return e=(e||0).toString(16),1===e.length?"0"+e:e}).join("")},toString:function(){return 0===this._rgba[3]?"transparent":this.toRgbaString()}}),l.fn.parse.prototype=l.fn,u.hsla.to=function(e){if(null==e[0]||null==e[1]||null==e[2])return[null,null,null,e[3]];var t,i,s=e[0]/255,n=e[1]/255,a=e[2]/255,o=e[3],r=Math.max(s,n,a),h=Math.min(s,n,a),l=r-h,u=r+h,d=.5*u;return t=h===r?0:s===r?60*(n-a)/l+360:n===r?60*(a-s)/l+120:60*(s-n)/l+240,i=0===l?0:.5>=d?l/u:l/(2-u),[Math.round(t)%360,i,d,null==o?1:o]},u.hsla.from=function(e){if(null==e[0]||null==e[1]||null==e[2])return[null,null,null,e[3]];var t=e[0]/360,i=e[1],s=e[2],a=e[3],o=.5>=s?s*(1+i):s+i-s*i,r=2*s-o;return[Math.round(255*n(r,o,t+1/3)),Math.round(255*n(r,o,t)),Math.round(255*n(r,o,t-1/3)),a]},f(u,function(s,n){var a=n.props,o=n.cache,h=n.to,u=n.from;l.fn[s]=function(s){if(h&&!this[o]&&(this[o]=h(this._rgba)),s===t)return this[o].slice();var n,r=e.type(s),d="array"===r||"object"===r?s:arguments,c=this[o].slice();return f(a,function(e,t){var s=d["object"===r?e:t.idx];null==s&&(s=c[t.idx]),c[t.idx]=i(s,t)}),u?(n=l(u(c)),n[o]=c,n):l(c)},f(a,function(t,i){l.fn[t]||(l.fn[t]=function(n){var a,o=e.type(n),h="alpha"===t?this._hsla?"hsla":"rgba":s,l=this[h](),u=l[i.idx];return"undefined"===o?u:("function"===o&&(n=n.call(this,u),o=e.type(n)),null==n&&i.empty?this:("string"===o&&(a=r.exec(n),a&&(n=u+parseFloat(a[2])*("+"===a[1]?1:-1))),l[i.idx]=n,this[h](l)))})})}),l.hook=function(t){var i=t.split(" ");f(i,function(t,i){e.cssHooks[i]={set:function(t,n){var a,o,r="";if("transparent"!==n&&("string"!==e.type(n)||(a=s(n)))){if(n=l(a||n),!c.rgba&&1!==n._rgba[3]){for(o="backgroundColor"===i?t.parentNode:t;(""===r||"transparent"===r)&&o&&o.style;)try{r=e.css(o,"backgroundColor"),o=o.parentNode}catch(h){}n=n.blend(r&&"transparent"!==r?r:"_default")}n=n.toRgbaString()}try{t.style[i]=n}catch(h){}}},e.fx.step[i]=function(t){t.colorInit||(t.start=l(t.elem,i),t.end=l(t.end),t.colorInit=!0),e.cssHooks[i].set(t.elem,t.start.transition(t.end,t.pos))}})},l.hook(o),e.cssHooks.borderColor={expand:function(e){var t={};return f(["Top","Right","Bottom","Left"],function(i,s){t["border"+s+"Color"]=e}),t}},a=e.Color.names={aqua:"#00ffff",black:"#000000",blue:"#0000ff",fuchsia:"#ff00ff",gray:"#808080",green:"#008000",lime:"#00ff00",maroon:"#800000",navy:"#000080",olive:"#808000",purple:"#800080",red:"#ff0000",silver:"#c0c0c0",teal:"#008080",white:"#ffffff",yellow:"#ffff00",transparent:[null,null,null,0],_default:"#ffffff"}}(b),function(){function t(t){var i,s,n=t.ownerDocument.defaultView?t.ownerDocument.defaultView.getComputedStyle(t,null):t.currentStyle,a={};if(n&&n.length&&n[0]&&n[n[0]])for(s=n.length;s--;)i=n[s],"string"==typeof n[i]&&(a[e.camelCase(i)]=n[i]);else for(i in n)"string"==typeof n[i]&&(a[i]=n[i]);return a}function i(t,i){var s,a,o={};for(s in i)a=i[s],t[s]!==a&&(n[s]||(e.fx.step[s]||!isNaN(parseFloat(a)))&&(o[s]=a));return o}var s=["add","remove","toggle"],n={border:1,borderBottom:1,borderColor:1,borderLeft:1,borderRight:1,borderTop:1,borderWidth:1,margin:1,padding:1};e.each(["borderLeftStyle","borderRightStyle","borderBottomStyle","borderTopStyle"],function(t,i){e.fx.step[i]=function(e){("none"!==e.end&&!e.setAttr||1===e.pos&&!e.setAttr)&&(b.style(e.elem,i,e.end),e.setAttr=!0)}}),e.fn.addBack||(e.fn.addBack=function(e){return this.add(null==e?this.prevObject:this.prevObject.filter(e))}),e.effects.animateClass=function(n,a,o,r){var h=e.speed(a,o,r);return this.queue(function(){var a,o=e(this),r=o.attr("class")||"",l=h.children?o.find("*").addBack():o;l=l.map(function(){var i=e(this);return{el:i,start:t(this)}}),a=function(){e.each(s,function(e,t){n[t]&&o[t+"Class"](n[t])})},a(),l=l.map(function(){return this.end=t(this.el[0]),this.diff=i(this.start,this.end),this}),o.attr("class",r),l=l.map(function(){var t=this,i=e.Deferred(),s=e.extend({},h,{queue:!1,complete:function(){i.resolve(t)}});return this.el.animate(this.diff,s),i.promise()}),e.when.apply(e,l.get()).done(function(){a(),e.each(arguments,function(){var t=this.el;e.each(this.diff,function(e){t.css(e,"")})}),h.complete.call(o[0])})})},e.fn.extend({addClass:function(t){return function(i,s,n,a){return s?e.effects.animateClass.call(this,{add:i},s,n,a):t.apply(this,arguments)}}(e.fn.addClass),removeClass:function(t){return function(i,s,n,a){return arguments.length>1?e.effects.animateClass.call(this,{remove:i},s,n,a):t.apply(this,arguments)}}(e.fn.removeClass),toggleClass:function(t){return function(i,s,n,a,o){return"boolean"==typeof s||void 0===s?n?e.effects.animateClass.call(this,s?{add:i}:{remove:i},n,a,o):t.apply(this,arguments):e.effects.animateClass.call(this,{toggle:i},s,n,a)}}(e.fn.toggleClass),switchClass:function(t,i,s,n,a){return e.effects.animateClass.call(this,{add:i,remove:t},s,n,a)}})}(),function(){function t(t,i,s,n){return e.isPlainObject(t)&&(i=t,t=t.effect),t={effect:t},null==i&&(i={}),e.isFunction(i)&&(n=i,s=null,i={}),("number"==typeof i||e.fx.speeds[i])&&(n=s,s=i,i={}),e.isFunction(s)&&(n=s,s=null),i&&e.extend(t,i),s=s||i.duration,t.duration=e.fx.off?0:"number"==typeof s?s:s in e.fx.speeds?e.fx.speeds[s]:e.fx.speeds._default,t.complete=n||i.complete,t}function i(t){return!t||"number"==typeof t||e.fx.speeds[t]?!0:"string"!=typeof t||e.effects.effect[t]?e.isFunction(t)?!0:"object"!=typeof t||t.effect?!1:!0:!0}e.extend(e.effects,{version:"1.11.2",save:function(e,t){for(var i=0;t.length>i;i++)null!==t[i]&&e.data(y+t[i],e[0].style[t[i]])},restore:function(e,t){var i,s;for(s=0;t.length>s;s++)null!==t[s]&&(i=e.data(y+t[s]),void 0===i&&(i=""),e.css(t[s],i))},setMode:function(e,t){return"toggle"===t&&(t=e.is(":hidden")?"show":"hide"),t},getBaseline:function(e,t){var i,s;switch(e[0]){case"top":i=0;break;case"middle":i=.5;break;case"bottom":i=1;break;default:i=e[0]/t.height}switch(e[1]){case"left":s=0;break;case"center":s=.5;break;case"right":s=1;break;default:s=e[1]/t.width}return{x:s,y:i}},createWrapper:function(t){if(t.parent().is(".ui-effects-wrapper"))return t.parent();var i={width:t.outerWidth(!0),height:t.outerHeight(!0),"float":t.css("float")},s=e("<div></div>").addClass("ui-effects-wrapper").css({fontSize:"100%",background:"transparent",border:"none",margin:0,padding:0}),n={width:t.width(),height:t.height()},a=document.activeElement;try{a.id}catch(o){a=document.body}return t.wrap(s),(t[0]===a||e.contains(t[0],a))&&e(a).focus(),s=t.parent(),"static"===t.css("position")?(s.css({position:"relative"}),t.css({position:"relative"})):(e.extend(i,{position:t.css("position"),zIndex:t.css("z-index")}),e.each(["top","left","bottom","right"],function(e,s){i[s]=t.css(s),isNaN(parseInt(i[s],10))&&(i[s]="auto")}),t.css({position:"relative",top:0,left:0,right:"auto",bottom:"auto"})),t.css(n),s.css(i).show()},removeWrapper:function(t){var i=document.activeElement;return t.parent().is(".ui-effects-wrapper")&&(t.parent().replaceWith(t),(t[0]===i||e.contains(t[0],i))&&e(i).focus()),t},setTransition:function(t,i,s,n){return n=n||{},e.each(i,function(e,i){var a=t.cssUnit(i);a[0]>0&&(n[i]=a[0]*s+a[1])}),n}}),e.fn.extend({effect:function(){function i(t){function i(){e.isFunction(a)&&a.call(n[0]),e.isFunction(t)&&t()}var n=e(this),a=s.complete,r=s.mode;(n.is(":hidden")?"hide"===r:"show"===r)?(n[r](),i()):o.call(n[0],s,i)}var s=t.apply(this,arguments),n=s.mode,a=s.queue,o=e.effects.effect[s.effect];return e.fx.off||!o?n?this[n](s.duration,s.complete):this.each(function(){s.complete&&s.complete.call(this)}):a===!1?this.each(i):this.queue(a||"fx",i)},show:function(e){return function(s){if(i(s))return e.apply(this,arguments);var n=t.apply(this,arguments);return n.mode="show",this.effect.call(this,n)}}(e.fn.show),hide:function(e){return function(s){if(i(s))return e.apply(this,arguments);var n=t.apply(this,arguments);return n.mode="hide",this.effect.call(this,n)}}(e.fn.hide),toggle:function(e){return function(s){if(i(s)||"boolean"==typeof s)return e.apply(this,arguments);var n=t.apply(this,arguments);return n.mode="toggle",this.effect.call(this,n)}}(e.fn.toggle),cssUnit:function(t){var i=this.css(t),s=[];return e.each(["em","px","%","pt"],function(e,t){i.indexOf(t)>0&&(s=[parseFloat(i),t])}),s}})}(),function(){var t={};e.each(["Quad","Cubic","Quart","Quint","Expo"],function(e,i){t[i]=function(t){return Math.pow(t,e+2)}}),e.extend(t,{Sine:function(e){return 1-Math.cos(e*Math.PI/2)},Circ:function(e){return 1-Math.sqrt(1-e*e)},Elastic:function(e){return 0===e||1===e?e:-Math.pow(2,8*(e-1))*Math.sin((80*(e-1)-7.5)*Math.PI/15)},Back:function(e){return e*e*(3*e-2)},Bounce:function(e){for(var t,i=4;((t=Math.pow(2,--i))-1)/11>e;);return 1/Math.pow(4,3-i)-7.5625*Math.pow((3*t-2)/22-e,2)}}),e.each(t,function(t,i){e.easing["easeIn"+t]=i,e.easing["easeOut"+t]=function(e){return 1-i(1-e)},e.easing["easeInOut"+t]=function(e){return.5>e?i(2*e)/2:1-i(-2*e+2)/2}})}(),e.effects,e.effects.effect.blind=function(t,i){var s,n,a,o=e(this),r=/up|down|vertical/,h=/up|left|vertical|horizontal/,l=["position","top","bottom","left","right","height","width"],u=e.effects.setMode(o,t.mode||"hide"),d=t.direction||"up",c=r.test(d),p=c?"height":"width",f=c?"top":"left",m=h.test(d),g={},v="show"===u;o.parent().is(".ui-effects-wrapper")?e.effects.save(o.parent(),l):e.effects.save(o,l),o.show(),s=e.effects.createWrapper(o).css({overflow:"hidden"}),n=s[p](),a=parseFloat(s.css(f))||0,g[p]=v?n:0,m||(o.css(c?"bottom":"right",0).css(c?"top":"left","auto").css({position:"absolute"}),g[f]=v?a:n+a),v&&(s.css(p,0),m||s.css(f,a+n)),s.animate(g,{duration:t.duration,easing:t.easing,queue:!1,complete:function(){"hide"===u&&o.hide(),e.effects.restore(o,l),e.effects.removeWrapper(o),i()}})},e.effects.effect.bounce=function(t,i){var s,n,a,o=e(this),r=["position","top","bottom","left","right","height","width"],h=e.effects.setMode(o,t.mode||"effect"),l="hide"===h,u="show"===h,d=t.direction||"up",c=t.distance,p=t.times||5,f=2*p+(u||l?1:0),m=t.duration/f,g=t.easing,v="up"===d||"down"===d?"top":"left",y="up"===d||"left"===d,b=o.queue(),_=b.length;for((u||l)&&r.push("opacity"),e.effects.save(o,r),o.show(),e.effects.createWrapper(o),c||(c=o["top"===v?"outerHeight":"outerWidth"]()/3),u&&(a={opacity:1},a[v]=0,o.css("opacity",0).css(v,y?2*-c:2*c).animate(a,m,g)),l&&(c/=Math.pow(2,p-1)),a={},a[v]=0,s=0;p>s;s++)n={},n[v]=(y?"-=":"+=")+c,o.animate(n,m,g).animate(a,m,g),c=l?2*c:c/2;l&&(n={opacity:0},n[v]=(y?"-=":"+=")+c,o.animate(n,m,g)),o.queue(function(){l&&o.hide(),e.effects.restore(o,r),e.effects.removeWrapper(o),i()}),_>1&&b.splice.apply(b,[1,0].concat(b.splice(_,f+1))),o.dequeue()},e.effects.effect.clip=function(t,i){var s,n,a,o=e(this),r=["position","top","bottom","left","right","height","width"],h=e.effects.setMode(o,t.mode||"hide"),l="show"===h,u=t.direction||"vertical",d="vertical"===u,c=d?"height":"width",p=d?"top":"left",f={};e.effects.save(o,r),o.show(),s=e.effects.createWrapper(o).css({overflow:"hidden"}),n="IMG"===o[0].tagName?s:o,a=n[c](),l&&(n.css(c,0),n.css(p,a/2)),f[c]=l?a:0,f[p]=l?0:a/2,n.animate(f,{queue:!1,duration:t.duration,easing:t.easing,complete:function(){l||o.hide(),e.effects.restore(o,r),e.effects.removeWrapper(o),i()}})},e.effects.effect.drop=function(t,i){var s,n=e(this),a=["position","top","bottom","left","right","opacity","height","width"],o=e.effects.setMode(n,t.mode||"hide"),r="show"===o,h=t.direction||"left",l="up"===h||"down"===h?"top":"left",u="up"===h||"left"===h?"pos":"neg",d={opacity:r?1:0};e.effects.save(n,a),n.show(),e.effects.createWrapper(n),s=t.distance||n["top"===l?"outerHeight":"outerWidth"](!0)/2,r&&n.css("opacity",0).css(l,"pos"===u?-s:s),d[l]=(r?"pos"===u?"+=":"-=":"pos"===u?"-=":"+=")+s,n.animate(d,{queue:!1,duration:t.duration,easing:t.easing,complete:function(){"hide"===o&&n.hide(),e.effects.restore(n,a),e.effects.removeWrapper(n),i()}})},e.effects.effect.explode=function(t,i){function s(){b.push(this),b.length===d*c&&n()}function n(){p.css({visibility:"visible"}),e(b).remove(),m||p.hide(),i()}var a,o,r,h,l,u,d=t.pieces?Math.round(Math.sqrt(t.pieces)):3,c=d,p=e(this),f=e.effects.setMode(p,t.mode||"hide"),m="show"===f,g=p.show().css("visibility","hidden").offset(),v=Math.ceil(p.outerWidth()/c),y=Math.ceil(p.outerHeight()/d),b=[];for(a=0;d>a;a++)for(h=g.top+a*y,u=a-(d-1)/2,o=0;c>o;o++)r=g.left+o*v,l=o-(c-1)/2,p.clone().appendTo("body").wrap("<div></div>").css({position:"absolute",visibility:"visible",left:-o*v,top:-a*y}).parent().addClass("ui-effects-explode").css({position:"absolute",overflow:"hidden",width:v,height:y,left:r+(m?l*v:0),top:h+(m?u*y:0),opacity:m?0:1}).animate({left:r+(m?0:l*v),top:h+(m?0:u*y),opacity:m?1:0},t.duration||500,t.easing,s)},e.effects.effect.fade=function(t,i){var s=e(this),n=e.effects.setMode(s,t.mode||"toggle");s.animate({opacity:n},{queue:!1,duration:t.duration,easing:t.easing,complete:i})},e.effects.effect.fold=function(t,i){var s,n,a=e(this),o=["position","top","bottom","left","right","height","width"],r=e.effects.setMode(a,t.mode||"hide"),h="show"===r,l="hide"===r,u=t.size||15,d=/([0-9]+)%/.exec(u),c=!!t.horizFirst,p=h!==c,f=p?["width","height"]:["height","width"],m=t.duration/2,g={},v={};e.effects.save(a,o),a.show(),s=e.effects.createWrapper(a).css({overflow:"hidden"}),n=p?[s.width(),s.height()]:[s.height(),s.width()],d&&(u=parseInt(d[1],10)/100*n[l?0:1]),h&&s.css(c?{height:0,width:u}:{height:u,width:0}),g[f[0]]=h?n[0]:u,v[f[1]]=h?n[1]:0,s.animate(g,m,t.easing).animate(v,m,t.easing,function(){l&&a.hide(),e.effects.restore(a,o),e.effects.removeWrapper(a),i()})},e.effects.effect.highlight=function(t,i){var s=e(this),n=["backgroundImage","backgroundColor","opacity"],a=e.effects.setMode(s,t.mode||"show"),o={backgroundColor:s.css("backgroundColor")};"hide"===a&&(o.opacity=0),e.effects.save(s,n),s.show().css({backgroundImage:"none",backgroundColor:t.color||"#ffff99"}).animate(o,{queue:!1,duration:t.duration,easing:t.easing,complete:function(){"hide"===a&&s.hide(),e.effects.restore(s,n),i()}})},e.effects.effect.size=function(t,i){var s,n,a,o=e(this),r=["position","top","bottom","left","right","width","height","overflow","opacity"],h=["position","top","bottom","left","right","overflow","opacity"],l=["width","height","overflow"],u=["fontSize"],d=["borderTopWidth","borderBottomWidth","paddingTop","paddingBottom"],c=["borderLeftWidth","borderRightWidth","paddingLeft","paddingRight"],p=e.effects.setMode(o,t.mode||"effect"),f=t.restore||"effect"!==p,m=t.scale||"both",g=t.origin||["middle","center"],v=o.css("position"),y=f?r:h,b={height:0,width:0,outerHeight:0,outerWidth:0};"show"===p&&o.show(),s={height:o.height(),width:o.width(),outerHeight:o.outerHeight(),outerWidth:o.outerWidth()},"toggle"===t.mode&&"show"===p?(o.from=t.to||b,o.to=t.from||s):(o.from=t.from||("show"===p?b:s),o.to=t.to||("hide"===p?b:s)),a={from:{y:o.from.height/s.height,x:o.from.width/s.width},to:{y:o.to.height/s.height,x:o.to.width/s.width}},("box"===m||"both"===m)&&(a.from.y!==a.to.y&&(y=y.concat(d),o.from=e.effects.setTransition(o,d,a.from.y,o.from),o.to=e.effects.setTransition(o,d,a.to.y,o.to)),a.from.x!==a.to.x&&(y=y.concat(c),o.from=e.effects.setTransition(o,c,a.from.x,o.from),o.to=e.effects.setTransition(o,c,a.to.x,o.to))),("content"===m||"both"===m)&&a.from.y!==a.to.y&&(y=y.concat(u).concat(l),o.from=e.effects.setTransition(o,u,a.from.y,o.from),o.to=e.effects.setTransition(o,u,a.to.y,o.to)),e.effects.save(o,y),o.show(),e.effects.createWrapper(o),o.css("overflow","hidden").css(o.from),g&&(n=e.effects.getBaseline(g,s),o.from.top=(s.outerHeight-o.outerHeight())*n.y,o.from.left=(s.outerWidth-o.outerWidth())*n.x,o.to.top=(s.outerHeight-o.to.outerHeight)*n.y,o.to.left=(s.outerWidth-o.to.outerWidth)*n.x),o.css(o.from),("content"===m||"both"===m)&&(d=d.concat(["marginTop","marginBottom"]).concat(u),c=c.concat(["marginLeft","marginRight"]),l=r.concat(d).concat(c),o.find("*[width]").each(function(){var i=e(this),s={height:i.height(),width:i.width(),outerHeight:i.outerHeight(),outerWidth:i.outerWidth()};
f&&e.effects.save(i,l),i.from={height:s.height*a.from.y,width:s.width*a.from.x,outerHeight:s.outerHeight*a.from.y,outerWidth:s.outerWidth*a.from.x},i.to={height:s.height*a.to.y,width:s.width*a.to.x,outerHeight:s.height*a.to.y,outerWidth:s.width*a.to.x},a.from.y!==a.to.y&&(i.from=e.effects.setTransition(i,d,a.from.y,i.from),i.to=e.effects.setTransition(i,d,a.to.y,i.to)),a.from.x!==a.to.x&&(i.from=e.effects.setTransition(i,c,a.from.x,i.from),i.to=e.effects.setTransition(i,c,a.to.x,i.to)),i.css(i.from),i.animate(i.to,t.duration,t.easing,function(){f&&e.effects.restore(i,l)})})),o.animate(o.to,{queue:!1,duration:t.duration,easing:t.easing,complete:function(){0===o.to.opacity&&o.css("opacity",o.from.opacity),"hide"===p&&o.hide(),e.effects.restore(o,y),f||("static"===v?o.css({position:"relative",top:o.to.top,left:o.to.left}):e.each(["top","left"],function(e,t){o.css(t,function(t,i){var s=parseInt(i,10),n=e?o.to.left:o.to.top;return"auto"===i?n+"px":s+n+"px"})})),e.effects.removeWrapper(o),i()}})},e.effects.effect.scale=function(t,i){var s=e(this),n=e.extend(!0,{},t),a=e.effects.setMode(s,t.mode||"effect"),o=parseInt(t.percent,10)||(0===parseInt(t.percent,10)?0:"hide"===a?0:100),r=t.direction||"both",h=t.origin,l={height:s.height(),width:s.width(),outerHeight:s.outerHeight(),outerWidth:s.outerWidth()},u={y:"horizontal"!==r?o/100:1,x:"vertical"!==r?o/100:1};n.effect="size",n.queue=!1,n.complete=i,"effect"!==a&&(n.origin=h||["middle","center"],n.restore=!0),n.from=t.from||("show"===a?{height:0,width:0,outerHeight:0,outerWidth:0}:l),n.to={height:l.height*u.y,width:l.width*u.x,outerHeight:l.outerHeight*u.y,outerWidth:l.outerWidth*u.x},n.fade&&("show"===a&&(n.from.opacity=0,n.to.opacity=1),"hide"===a&&(n.from.opacity=1,n.to.opacity=0)),s.effect(n)},e.effects.effect.puff=function(t,i){var s=e(this),n=e.effects.setMode(s,t.mode||"hide"),a="hide"===n,o=parseInt(t.percent,10)||150,r=o/100,h={height:s.height(),width:s.width(),outerHeight:s.outerHeight(),outerWidth:s.outerWidth()};e.extend(t,{effect:"scale",queue:!1,fade:!0,mode:n,complete:i,percent:a?o:100,from:a?h:{height:h.height*r,width:h.width*r,outerHeight:h.outerHeight*r,outerWidth:h.outerWidth*r}}),s.effect(t)},e.effects.effect.pulsate=function(t,i){var s,n=e(this),a=e.effects.setMode(n,t.mode||"show"),o="show"===a,r="hide"===a,h=o||"hide"===a,l=2*(t.times||5)+(h?1:0),u=t.duration/l,d=0,c=n.queue(),p=c.length;for((o||!n.is(":visible"))&&(n.css("opacity",0).show(),d=1),s=1;l>s;s++)n.animate({opacity:d},u,t.easing),d=1-d;n.animate({opacity:d},u,t.easing),n.queue(function(){r&&n.hide(),i()}),p>1&&c.splice.apply(c,[1,0].concat(c.splice(p,l+1))),n.dequeue()},e.effects.effect.shake=function(t,i){var s,n=e(this),a=["position","top","bottom","left","right","height","width"],o=e.effects.setMode(n,t.mode||"effect"),r=t.direction||"left",h=t.distance||20,l=t.times||3,u=2*l+1,d=Math.round(t.duration/u),c="up"===r||"down"===r?"top":"left",p="up"===r||"left"===r,f={},m={},g={},v=n.queue(),y=v.length;for(e.effects.save(n,a),n.show(),e.effects.createWrapper(n),f[c]=(p?"-=":"+=")+h,m[c]=(p?"+=":"-=")+2*h,g[c]=(p?"-=":"+=")+2*h,n.animate(f,d,t.easing),s=1;l>s;s++)n.animate(m,d,t.easing).animate(g,d,t.easing);n.animate(m,d,t.easing).animate(f,d/2,t.easing).queue(function(){"hide"===o&&n.hide(),e.effects.restore(n,a),e.effects.removeWrapper(n),i()}),y>1&&v.splice.apply(v,[1,0].concat(v.splice(y,u+1))),n.dequeue()},e.effects.effect.slide=function(t,i){var s,n=e(this),a=["position","top","bottom","left","right","width","height"],o=e.effects.setMode(n,t.mode||"show"),r="show"===o,h=t.direction||"left",l="up"===h||"down"===h?"top":"left",u="up"===h||"left"===h,d={};e.effects.save(n,a),n.show(),s=t.distance||n["top"===l?"outerHeight":"outerWidth"](!0),e.effects.createWrapper(n).css({overflow:"hidden"}),r&&n.css(l,u?isNaN(s)?"-"+s:-s:s),d[l]=(r?u?"+=":"-=":u?"-=":"+=")+s,n.animate(d,{queue:!1,duration:t.duration,easing:t.easing,complete:function(){"hide"===o&&n.hide(),e.effects.restore(n,a),e.effects.removeWrapper(n),i()}})},e.effects.effect.transfer=function(t,i){var s=e(this),n=e(t.to),a="fixed"===n.css("position"),o=e("body"),r=a?o.scrollTop():0,h=a?o.scrollLeft():0,l=n.offset(),u={top:l.top-r,left:l.left-h,height:n.innerHeight(),width:n.innerWidth()},d=s.offset(),c=e("<div class='ui-effects-transfer'></div>").appendTo(document.body).addClass(t.className).css({top:d.top-r,left:d.left-h,height:s.innerHeight(),width:s.innerWidth(),position:a?"fixed":"absolute"}).animate(u,t.duration,t.easing,function(){c.remove(),i()})},e.widget("ui.progressbar",{version:"1.11.2",options:{max:100,value:0,change:null,complete:null},min:0,_create:function(){this.oldValue=this.options.value=this._constrainedValue(),this.element.addClass("ui-progressbar ui-widget ui-widget-content ui-corner-all").attr({role:"progressbar","aria-valuemin":this.min}),this.valueDiv=e("<div class='ui-progressbar-value ui-widget-header ui-corner-left'></div>").appendTo(this.element),this._refreshValue()},_destroy:function(){this.element.removeClass("ui-progressbar ui-widget ui-widget-content ui-corner-all").removeAttr("role").removeAttr("aria-valuemin").removeAttr("aria-valuemax").removeAttr("aria-valuenow"),this.valueDiv.remove()},value:function(e){return void 0===e?this.options.value:(this.options.value=this._constrainedValue(e),this._refreshValue(),void 0)},_constrainedValue:function(e){return void 0===e&&(e=this.options.value),this.indeterminate=e===!1,"number"!=typeof e&&(e=0),this.indeterminate?!1:Math.min(this.options.max,Math.max(this.min,e))},_setOptions:function(e){var t=e.value;delete e.value,this._super(e),this.options.value=this._constrainedValue(t),this._refreshValue()},_setOption:function(e,t){"max"===e&&(t=Math.max(this.min,t)),"disabled"===e&&this.element.toggleClass("ui-state-disabled",!!t).attr("aria-disabled",t),this._super(e,t)},_percentage:function(){return this.indeterminate?100:100*(this.options.value-this.min)/(this.options.max-this.min)},_refreshValue:function(){var t=this.options.value,i=this._percentage();this.valueDiv.toggle(this.indeterminate||t>this.min).toggleClass("ui-corner-right",t===this.options.max).width(i.toFixed(0)+"%"),this.element.toggleClass("ui-progressbar-indeterminate",this.indeterminate),this.indeterminate?(this.element.removeAttr("aria-valuenow"),this.overlayDiv||(this.overlayDiv=e("<div class='ui-progressbar-overlay'></div>").appendTo(this.valueDiv))):(this.element.attr({"aria-valuemax":this.options.max,"aria-valuenow":t}),this.overlayDiv&&(this.overlayDiv.remove(),this.overlayDiv=null)),this.oldValue!==t&&(this.oldValue=t,this._trigger("change")),t===this.options.max&&this._trigger("complete")}}),e.widget("ui.selectable",e.ui.mouse,{version:"1.11.2",options:{appendTo:"body",autoRefresh:!0,distance:0,filter:"*",tolerance:"touch",selected:null,selecting:null,start:null,stop:null,unselected:null,unselecting:null},_create:function(){var t,i=this;this.element.addClass("ui-selectable"),this.dragged=!1,this.refresh=function(){t=e(i.options.filter,i.element[0]),t.addClass("ui-selectee"),t.each(function(){var t=e(this),i=t.offset();e.data(this,"selectable-item",{element:this,$element:t,left:i.left,top:i.top,right:i.left+t.outerWidth(),bottom:i.top+t.outerHeight(),startselected:!1,selected:t.hasClass("ui-selected"),selecting:t.hasClass("ui-selecting"),unselecting:t.hasClass("ui-unselecting")})})},this.refresh(),this.selectees=t.addClass("ui-selectee"),this._mouseInit(),this.helper=e("<div class='ui-selectable-helper'></div>")},_destroy:function(){this.selectees.removeClass("ui-selectee").removeData("selectable-item"),this.element.removeClass("ui-selectable ui-selectable-disabled"),this._mouseDestroy()},_mouseStart:function(t){var i=this,s=this.options;this.opos=[t.pageX,t.pageY],this.options.disabled||(this.selectees=e(s.filter,this.element[0]),this._trigger("start",t),e(s.appendTo).append(this.helper),this.helper.css({left:t.pageX,top:t.pageY,width:0,height:0}),s.autoRefresh&&this.refresh(),this.selectees.filter(".ui-selected").each(function(){var s=e.data(this,"selectable-item");s.startselected=!0,t.metaKey||t.ctrlKey||(s.$element.removeClass("ui-selected"),s.selected=!1,s.$element.addClass("ui-unselecting"),s.unselecting=!0,i._trigger("unselecting",t,{unselecting:s.element}))}),e(t.target).parents().addBack().each(function(){var s,n=e.data(this,"selectable-item");return n?(s=!t.metaKey&&!t.ctrlKey||!n.$element.hasClass("ui-selected"),n.$element.removeClass(s?"ui-unselecting":"ui-selected").addClass(s?"ui-selecting":"ui-unselecting"),n.unselecting=!s,n.selecting=s,n.selected=s,s?i._trigger("selecting",t,{selecting:n.element}):i._trigger("unselecting",t,{unselecting:n.element}),!1):void 0}))},_mouseDrag:function(t){if(this.dragged=!0,!this.options.disabled){var i,s=this,n=this.options,a=this.opos[0],o=this.opos[1],r=t.pageX,h=t.pageY;return a>r&&(i=r,r=a,a=i),o>h&&(i=h,h=o,o=i),this.helper.css({left:a,top:o,width:r-a,height:h-o}),this.selectees.each(function(){var i=e.data(this,"selectable-item"),l=!1;i&&i.element!==s.element[0]&&("touch"===n.tolerance?l=!(i.left>r||a>i.right||i.top>h||o>i.bottom):"fit"===n.tolerance&&(l=i.left>a&&r>i.right&&i.top>o&&h>i.bottom),l?(i.selected&&(i.$element.removeClass("ui-selected"),i.selected=!1),i.unselecting&&(i.$element.removeClass("ui-unselecting"),i.unselecting=!1),i.selecting||(i.$element.addClass("ui-selecting"),i.selecting=!0,s._trigger("selecting",t,{selecting:i.element}))):(i.selecting&&((t.metaKey||t.ctrlKey)&&i.startselected?(i.$element.removeClass("ui-selecting"),i.selecting=!1,i.$element.addClass("ui-selected"),i.selected=!0):(i.$element.removeClass("ui-selecting"),i.selecting=!1,i.startselected&&(i.$element.addClass("ui-unselecting"),i.unselecting=!0),s._trigger("unselecting",t,{unselecting:i.element}))),i.selected&&(t.metaKey||t.ctrlKey||i.startselected||(i.$element.removeClass("ui-selected"),i.selected=!1,i.$element.addClass("ui-unselecting"),i.unselecting=!0,s._trigger("unselecting",t,{unselecting:i.element})))))}),!1}},_mouseStop:function(t){var i=this;return this.dragged=!1,e(".ui-unselecting",this.element[0]).each(function(){var s=e.data(this,"selectable-item");s.$element.removeClass("ui-unselecting"),s.unselecting=!1,s.startselected=!1,i._trigger("unselected",t,{unselected:s.element})}),e(".ui-selecting",this.element[0]).each(function(){var s=e.data(this,"selectable-item");s.$element.removeClass("ui-selecting").addClass("ui-selected"),s.selecting=!1,s.selected=!0,s.startselected=!0,i._trigger("selected",t,{selected:s.element})}),this._trigger("stop",t),this.helper.remove(),!1}}),e.widget("ui.selectmenu",{version:"1.11.2",defaultElement:"<select>",options:{appendTo:null,disabled:null,icons:{button:"ui-icon-triangle-1-s"},position:{my:"left top",at:"left bottom",collision:"none"},width:null,change:null,close:null,focus:null,open:null,select:null},_create:function(){var e=this.element.uniqueId().attr("id");this.ids={element:e,button:e+"-button",menu:e+"-menu"},this._drawButton(),this._drawMenu(),this.options.disabled&&this.disable()},_drawButton:function(){var t=this,i=this.element.attr("tabindex");this.label=e("label[for='"+this.ids.element+"']").attr("for",this.ids.button),this._on(this.label,{click:function(e){this.button.focus(),e.preventDefault()}}),this.element.hide(),this.button=e("<span>",{"class":"ui-selectmenu-button ui-widget ui-state-default ui-corner-all",tabindex:i||this.options.disabled?-1:0,id:this.ids.button,role:"combobox","aria-expanded":"false","aria-autocomplete":"list","aria-owns":this.ids.menu,"aria-haspopup":"true"}).insertAfter(this.element),e("<span>",{"class":"ui-icon "+this.options.icons.button}).prependTo(this.button),this.buttonText=e("<span>",{"class":"ui-selectmenu-text"}).appendTo(this.button),this._setText(this.buttonText,this.element.find("option:selected").text()),this._resizeButton(),this._on(this.button,this._buttonEvents),this.button.one("focusin",function(){t.menuItems||t._refreshMenu()}),this._hoverable(this.button),this._focusable(this.button)},_drawMenu:function(){var t=this;this.menu=e("<ul>",{"aria-hidden":"true","aria-labelledby":this.ids.button,id:this.ids.menu}),this.menuWrap=e("<div>",{"class":"ui-selectmenu-menu ui-front"}).append(this.menu).appendTo(this._appendTo()),this.menuInstance=this.menu.menu({role:"listbox",select:function(e,i){e.preventDefault(),t._setSelection(),t._select(i.item.data("ui-selectmenu-item"),e)},focus:function(e,i){var s=i.item.data("ui-selectmenu-item");null!=t.focusIndex&&s.index!==t.focusIndex&&(t._trigger("focus",e,{item:s}),t.isOpen||t._select(s,e)),t.focusIndex=s.index,t.button.attr("aria-activedescendant",t.menuItems.eq(s.index).attr("id"))}}).menu("instance"),this.menu.addClass("ui-corner-bottom").removeClass("ui-corner-all"),this.menuInstance._off(this.menu,"mouseleave"),this.menuInstance._closeOnDocumentClick=function(){return!1},this.menuInstance._isDivider=function(){return!1}},refresh:function(){this._refreshMenu(),this._setText(this.buttonText,this._getSelectedItem().text()),this.options.width||this._resizeButton()},_refreshMenu:function(){this.menu.empty();var e,t=this.element.find("option");t.length&&(this._parseOptions(t),this._renderMenu(this.menu,this.items),this.menuInstance.refresh(),this.menuItems=this.menu.find("li").not(".ui-selectmenu-optgroup"),e=this._getSelectedItem(),this.menuInstance.focus(null,e),this._setAria(e.data("ui-selectmenu-item")),this._setOption("disabled",this.element.prop("disabled")))},open:function(e){this.options.disabled||(this.menuItems?(this.menu.find(".ui-state-focus").removeClass("ui-state-focus"),this.menuInstance.focus(null,this._getSelectedItem())):this._refreshMenu(),this.isOpen=!0,this._toggleAttr(),this._resizeMenu(),this._position(),this._on(this.document,this._documentClick),this._trigger("open",e))},_position:function(){this.menuWrap.position(e.extend({of:this.button},this.options.position))},close:function(e){this.isOpen&&(this.isOpen=!1,this._toggleAttr(),this.range=null,this._off(this.document),this._trigger("close",e))},widget:function(){return this.button},menuWidget:function(){return this.menu},_renderMenu:function(t,i){var s=this,n="";e.each(i,function(i,a){a.optgroup!==n&&(e("<li>",{"class":"ui-selectmenu-optgroup ui-menu-divider"+(a.element.parent("optgroup").prop("disabled")?" ui-state-disabled":""),text:a.optgroup}).appendTo(t),n=a.optgroup),s._renderItemData(t,a)})},_renderItemData:function(e,t){return this._renderItem(e,t).data("ui-selectmenu-item",t)},_renderItem:function(t,i){var s=e("<li>");return i.disabled&&s.addClass("ui-state-disabled"),this._setText(s,i.label),s.appendTo(t)},_setText:function(e,t){t?e.text(t):e.html("&#160;")},_move:function(e,t){var i,s,n=".ui-menu-item";this.isOpen?i=this.menuItems.eq(this.focusIndex):(i=this.menuItems.eq(this.element[0].selectedIndex),n+=":not(.ui-state-disabled)"),s="first"===e||"last"===e?i["first"===e?"prevAll":"nextAll"](n).eq(-1):i[e+"All"](n).eq(0),s.length&&this.menuInstance.focus(t,s)},_getSelectedItem:function(){return this.menuItems.eq(this.element[0].selectedIndex)},_toggle:function(e){this[this.isOpen?"close":"open"](e)},_setSelection:function(){var e;this.range&&(window.getSelection?(e=window.getSelection(),e.removeAllRanges(),e.addRange(this.range)):this.range.select(),this.button.focus())},_documentClick:{mousedown:function(t){this.isOpen&&(e(t.target).closest(".ui-selectmenu-menu, #"+this.ids.button).length||this.close(t))}},_buttonEvents:{mousedown:function(){var e;window.getSelection?(e=window.getSelection(),e.rangeCount&&(this.range=e.getRangeAt(0))):this.range=document.selection.createRange()},click:function(e){this._setSelection(),this._toggle(e)},keydown:function(t){var i=!0;switch(t.keyCode){case e.ui.keyCode.TAB:case e.ui.keyCode.ESCAPE:this.close(t),i=!1;break;case e.ui.keyCode.ENTER:this.isOpen&&this._selectFocusedItem(t);break;case e.ui.keyCode.UP:t.altKey?this._toggle(t):this._move("prev",t);break;case e.ui.keyCode.DOWN:t.altKey?this._toggle(t):this._move("next",t);break;case e.ui.keyCode.SPACE:this.isOpen?this._selectFocusedItem(t):this._toggle(t);break;case e.ui.keyCode.LEFT:this._move("prev",t);break;case e.ui.keyCode.RIGHT:this._move("next",t);break;case e.ui.keyCode.HOME:case e.ui.keyCode.PAGE_UP:this._move("first",t);break;case e.ui.keyCode.END:case e.ui.keyCode.PAGE_DOWN:this._move("last",t);break;default:this.menu.trigger(t),i=!1}i&&t.preventDefault()}},_selectFocusedItem:function(e){var t=this.menuItems.eq(this.focusIndex);t.hasClass("ui-state-disabled")||this._select(t.data("ui-selectmenu-item"),e)},_select:function(e,t){var i=this.element[0].selectedIndex;this.element[0].selectedIndex=e.index,this._setText(this.buttonText,e.label),this._setAria(e),this._trigger("select",t,{item:e}),e.index!==i&&this._trigger("change",t,{item:e}),this.close(t)},_setAria:function(e){var t=this.menuItems.eq(e.index).attr("id");this.button.attr({"aria-labelledby":t,"aria-activedescendant":t}),this.menu.attr("aria-activedescendant",t)},_setOption:function(e,t){"icons"===e&&this.button.find("span.ui-icon").removeClass(this.options.icons.button).addClass(t.button),this._super(e,t),"appendTo"===e&&this.menuWrap.appendTo(this._appendTo()),"disabled"===e&&(this.menuInstance.option("disabled",t),this.button.toggleClass("ui-state-disabled",t).attr("aria-disabled",t),this.element.prop("disabled",t),t?(this.button.attr("tabindex",-1),this.close()):this.button.attr("tabindex",0)),"width"===e&&this._resizeButton()},_appendTo:function(){var t=this.options.appendTo;return t&&(t=t.jquery||t.nodeType?e(t):this.document.find(t).eq(0)),t&&t[0]||(t=this.element.closest(".ui-front")),t.length||(t=this.document[0].body),t},_toggleAttr:function(){this.button.toggleClass("ui-corner-top",this.isOpen).toggleClass("ui-corner-all",!this.isOpen).attr("aria-expanded",this.isOpen),this.menuWrap.toggleClass("ui-selectmenu-open",this.isOpen),this.menu.attr("aria-hidden",!this.isOpen)},_resizeButton:function(){var e=this.options.width;e||(e=this.element.show().outerWidth(),this.element.hide()),this.button.outerWidth(e)},_resizeMenu:function(){this.menu.outerWidth(Math.max(this.button.outerWidth(),this.menu.width("").outerWidth()+1))},_getCreateOptions:function(){return{disabled:this.element.prop("disabled")}},_parseOptions:function(t){var i=[];t.each(function(t,s){var n=e(s),a=n.parent("optgroup");i.push({element:n,index:t,value:n.attr("value"),label:n.text(),optgroup:a.attr("label")||"",disabled:a.prop("disabled")||n.prop("disabled")})}),this.items=i},_destroy:function(){this.menuWrap.remove(),this.button.remove(),this.element.show(),this.element.removeUniqueId(),this.label.attr("for",this.ids.element)}}),e.widget("ui.slider",e.ui.mouse,{version:"1.11.2",widgetEventPrefix:"slide",options:{animate:!1,distance:0,max:100,min:0,orientation:"horizontal",range:!1,step:1,value:0,values:null,change:null,slide:null,start:null,stop:null},numPages:5,_create:function(){this._keySliding=!1,this._mouseSliding=!1,this._animateOff=!0,this._handleIndex=null,this._detectOrientation(),this._mouseInit(),this._calculateNewMax(),this.element.addClass("ui-slider ui-slider-"+this.orientation+" ui-widget"+" ui-widget-content"+" ui-corner-all"),this._refresh(),this._setOption("disabled",this.options.disabled),this._animateOff=!1},_refresh:function(){this._createRange(),this._createHandles(),this._setupEvents(),this._refreshValue()},_createHandles:function(){var t,i,s=this.options,n=this.element.find(".ui-slider-handle").addClass("ui-state-default ui-corner-all"),a="<span class='ui-slider-handle ui-state-default ui-corner-all' tabindex='0'></span>",o=[];for(i=s.values&&s.values.length||1,n.length>i&&(n.slice(i).remove(),n=n.slice(0,i)),t=n.length;i>t;t++)o.push(a);this.handles=n.add(e(o.join("")).appendTo(this.element)),this.handle=this.handles.eq(0),this.handles.each(function(t){e(this).data("ui-slider-handle-index",t)})},_createRange:function(){var t=this.options,i="";t.range?(t.range===!0&&(t.values?t.values.length&&2!==t.values.length?t.values=[t.values[0],t.values[0]]:e.isArray(t.values)&&(t.values=t.values.slice(0)):t.values=[this._valueMin(),this._valueMin()]),this.range&&this.range.length?this.range.removeClass("ui-slider-range-min ui-slider-range-max").css({left:"",bottom:""}):(this.range=e("<div></div>").appendTo(this.element),i="ui-slider-range ui-widget-header ui-corner-all"),this.range.addClass(i+("min"===t.range||"max"===t.range?" ui-slider-range-"+t.range:""))):(this.range&&this.range.remove(),this.range=null)},_setupEvents:function(){this._off(this.handles),this._on(this.handles,this._handleEvents),this._hoverable(this.handles),this._focusable(this.handles)},_destroy:function(){this.handles.remove(),this.range&&this.range.remove(),this.element.removeClass("ui-slider ui-slider-horizontal ui-slider-vertical ui-widget ui-widget-content ui-corner-all"),this._mouseDestroy()},_mouseCapture:function(t){var i,s,n,a,o,r,h,l,u=this,d=this.options;return d.disabled?!1:(this.elementSize={width:this.element.outerWidth(),height:this.element.outerHeight()},this.elementOffset=this.element.offset(),i={x:t.pageX,y:t.pageY},s=this._normValueFromMouse(i),n=this._valueMax()-this._valueMin()+1,this.handles.each(function(t){var i=Math.abs(s-u.values(t));(n>i||n===i&&(t===u._lastChangedValue||u.values(t)===d.min))&&(n=i,a=e(this),o=t)}),r=this._start(t,o),r===!1?!1:(this._mouseSliding=!0,this._handleIndex=o,a.addClass("ui-state-active").focus(),h=a.offset(),l=!e(t.target).parents().addBack().is(".ui-slider-handle"),this._clickOffset=l?{left:0,top:0}:{left:t.pageX-h.left-a.width()/2,top:t.pageY-h.top-a.height()/2-(parseInt(a.css("borderTopWidth"),10)||0)-(parseInt(a.css("borderBottomWidth"),10)||0)+(parseInt(a.css("marginTop"),10)||0)},this.handles.hasClass("ui-state-hover")||this._slide(t,o,s),this._animateOff=!0,!0))},_mouseStart:function(){return!0},_mouseDrag:function(e){var t={x:e.pageX,y:e.pageY},i=this._normValueFromMouse(t);return this._slide(e,this._handleIndex,i),!1},_mouseStop:function(e){return this.handles.removeClass("ui-state-active"),this._mouseSliding=!1,this._stop(e,this._handleIndex),this._change(e,this._handleIndex),this._handleIndex=null,this._clickOffset=null,this._animateOff=!1,!1},_detectOrientation:function(){this.orientation="vertical"===this.options.orientation?"vertical":"horizontal"},_normValueFromMouse:function(e){var t,i,s,n,a;return"horizontal"===this.orientation?(t=this.elementSize.width,i=e.x-this.elementOffset.left-(this._clickOffset?this._clickOffset.left:0)):(t=this.elementSize.height,i=e.y-this.elementOffset.top-(this._clickOffset?this._clickOffset.top:0)),s=i/t,s>1&&(s=1),0>s&&(s=0),"vertical"===this.orientation&&(s=1-s),n=this._valueMax()-this._valueMin(),a=this._valueMin()+s*n,this._trimAlignValue(a)},_start:function(e,t){var i={handle:this.handles[t],value:this.value()};return this.options.values&&this.options.values.length&&(i.value=this.values(t),i.values=this.values()),this._trigger("start",e,i)},_slide:function(e,t,i){var s,n,a;this.options.values&&this.options.values.length?(s=this.values(t?0:1),2===this.options.values.length&&this.options.range===!0&&(0===t&&i>s||1===t&&s>i)&&(i=s),i!==this.values(t)&&(n=this.values(),n[t]=i,a=this._trigger("slide",e,{handle:this.handles[t],value:i,values:n}),s=this.values(t?0:1),a!==!1&&this.values(t,i))):i!==this.value()&&(a=this._trigger("slide",e,{handle:this.handles[t],value:i}),a!==!1&&this.value(i))},_stop:function(e,t){var i={handle:this.handles[t],value:this.value()};this.options.values&&this.options.values.length&&(i.value=this.values(t),i.values=this.values()),this._trigger("stop",e,i)},_change:function(e,t){if(!this._keySliding&&!this._mouseSliding){var i={handle:this.handles[t],value:this.value()};this.options.values&&this.options.values.length&&(i.value=this.values(t),i.values=this.values()),this._lastChangedValue=t,this._trigger("change",e,i)}},value:function(e){return arguments.length?(this.options.value=this._trimAlignValue(e),this._refreshValue(),this._change(null,0),void 0):this._value()},values:function(t,i){var s,n,a;if(arguments.length>1)return this.options.values[t]=this._trimAlignValue(i),this._refreshValue(),this._change(null,t),void 0;if(!arguments.length)return this._values();if(!e.isArray(arguments[0]))return this.options.values&&this.options.values.length?this._values(t):this.value();for(s=this.options.values,n=arguments[0],a=0;s.length>a;a+=1)s[a]=this._trimAlignValue(n[a]),this._change(null,a);this._refreshValue()},_setOption:function(t,i){var s,n=0;switch("range"===t&&this.options.range===!0&&("min"===i?(this.options.value=this._values(0),this.options.values=null):"max"===i&&(this.options.value=this._values(this.options.values.length-1),this.options.values=null)),e.isArray(this.options.values)&&(n=this.options.values.length),"disabled"===t&&this.element.toggleClass("ui-state-disabled",!!i),this._super(t,i),t){case"orientation":this._detectOrientation(),this.element.removeClass("ui-slider-horizontal ui-slider-vertical").addClass("ui-slider-"+this.orientation),this._refreshValue(),this.handles.css("horizontal"===i?"bottom":"left","");break;case"value":this._animateOff=!0,this._refreshValue(),this._change(null,0),this._animateOff=!1;break;case"values":for(this._animateOff=!0,this._refreshValue(),s=0;n>s;s+=1)this._change(null,s);this._animateOff=!1;break;case"step":case"min":case"max":this._animateOff=!0,this._calculateNewMax(),this._refreshValue(),this._animateOff=!1;break;case"range":this._animateOff=!0,this._refresh(),this._animateOff=!1}},_value:function(){var e=this.options.value;return e=this._trimAlignValue(e)},_values:function(e){var t,i,s;if(arguments.length)return t=this.options.values[e],t=this._trimAlignValue(t);if(this.options.values&&this.options.values.length){for(i=this.options.values.slice(),s=0;i.length>s;s+=1)i[s]=this._trimAlignValue(i[s]);return i}return[]},_trimAlignValue:function(e){if(this._valueMin()>=e)return this._valueMin();if(e>=this._valueMax())return this._valueMax();var t=this.options.step>0?this.options.step:1,i=(e-this._valueMin())%t,s=e-i;return 2*Math.abs(i)>=t&&(s+=i>0?t:-t),parseFloat(s.toFixed(5))},_calculateNewMax:function(){var e=(this.options.max-this._valueMin())%this.options.step;this.max=this.options.max-e},_valueMin:function(){return this.options.min},_valueMax:function(){return this.max},_refreshValue:function(){var t,i,s,n,a,o=this.options.range,r=this.options,h=this,l=this._animateOff?!1:r.animate,u={};this.options.values&&this.options.values.length?this.handles.each(function(s){i=100*((h.values(s)-h._valueMin())/(h._valueMax()-h._valueMin())),u["horizontal"===h.orientation?"left":"bottom"]=i+"%",e(this).stop(1,1)[l?"animate":"css"](u,r.animate),h.options.range===!0&&("horizontal"===h.orientation?(0===s&&h.range.stop(1,1)[l?"animate":"css"]({left:i+"%"},r.animate),1===s&&h.range[l?"animate":"css"]({width:i-t+"%"},{queue:!1,duration:r.animate})):(0===s&&h.range.stop(1,1)[l?"animate":"css"]({bottom:i+"%"},r.animate),1===s&&h.range[l?"animate":"css"]({height:i-t+"%"},{queue:!1,duration:r.animate}))),t=i}):(s=this.value(),n=this._valueMin(),a=this._valueMax(),i=a!==n?100*((s-n)/(a-n)):0,u["horizontal"===this.orientation?"left":"bottom"]=i+"%",this.handle.stop(1,1)[l?"animate":"css"](u,r.animate),"min"===o&&"horizontal"===this.orientation&&this.range.stop(1,1)[l?"animate":"css"]({width:i+"%"},r.animate),"max"===o&&"horizontal"===this.orientation&&this.range[l?"animate":"css"]({width:100-i+"%"},{queue:!1,duration:r.animate}),"min"===o&&"vertical"===this.orientation&&this.range.stop(1,1)[l?"animate":"css"]({height:i+"%"},r.animate),"max"===o&&"vertical"===this.orientation&&this.range[l?"animate":"css"]({height:100-i+"%"},{queue:!1,duration:r.animate}))},_handleEvents:{keydown:function(t){var i,s,n,a,o=e(t.target).data("ui-slider-handle-index");switch(t.keyCode){case e.ui.keyCode.HOME:case e.ui.keyCode.END:case e.ui.keyCode.PAGE_UP:case e.ui.keyCode.PAGE_DOWN:case e.ui.keyCode.UP:case e.ui.keyCode.RIGHT:case e.ui.keyCode.DOWN:case e.ui.keyCode.LEFT:if(t.preventDefault(),!this._keySliding&&(this._keySliding=!0,e(t.target).addClass("ui-state-active"),i=this._start(t,o),i===!1))return}switch(a=this.options.step,s=n=this.options.values&&this.options.values.length?this.values(o):this.value(),t.keyCode){case e.ui.keyCode.HOME:n=this._valueMin();break;case e.ui.keyCode.END:n=this._valueMax();break;case e.ui.keyCode.PAGE_UP:n=this._trimAlignValue(s+(this._valueMax()-this._valueMin())/this.numPages);break;case e.ui.keyCode.PAGE_DOWN:n=this._trimAlignValue(s-(this._valueMax()-this._valueMin())/this.numPages);break;case e.ui.keyCode.UP:case e.ui.keyCode.RIGHT:if(s===this._valueMax())return;n=this._trimAlignValue(s+a);break;case e.ui.keyCode.DOWN:case e.ui.keyCode.LEFT:if(s===this._valueMin())return;n=this._trimAlignValue(s-a)}this._slide(t,o,n)},keyup:function(t){var i=e(t.target).data("ui-slider-handle-index");this._keySliding&&(this._keySliding=!1,this._stop(t,i),this._change(t,i),e(t.target).removeClass("ui-state-active"))}}}),e.widget("ui.sortable",e.ui.mouse,{version:"1.11.2",widgetEventPrefix:"sort",ready:!1,options:{appendTo:"parent",axis:!1,connectWith:!1,containment:!1,cursor:"auto",cursorAt:!1,dropOnEmpty:!0,forcePlaceholderSize:!1,forceHelperSize:!1,grid:!1,handle:!1,helper:"original",items:"> *",opacity:!1,placeholder:!1,revert:!1,scroll:!0,scrollSensitivity:20,scrollSpeed:20,scope:"default",tolerance:"intersect",zIndex:1e3,activate:null,beforeStop:null,change:null,deactivate:null,out:null,over:null,receive:null,remove:null,sort:null,start:null,stop:null,update:null},_isOverAxis:function(e,t,i){return e>=t&&t+i>e},_isFloating:function(e){return/left|right/.test(e.css("float"))||/inline|table-cell/.test(e.css("display"))},_create:function(){var e=this.options;this.containerCache={},this.element.addClass("ui-sortable"),this.refresh(),this.floating=this.items.length?"x"===e.axis||this._isFloating(this.items[0].item):!1,this.offset=this.element.offset(),this._mouseInit(),this._setHandleClassName(),this.ready=!0},_setOption:function(e,t){this._super(e,t),"handle"===e&&this._setHandleClassName()},_setHandleClassName:function(){this.element.find(".ui-sortable-handle").removeClass("ui-sortable-handle"),e.each(this.items,function(){(this.instance.options.handle?this.item.find(this.instance.options.handle):this.item).addClass("ui-sortable-handle")})},_destroy:function(){this.element.removeClass("ui-sortable ui-sortable-disabled").find(".ui-sortable-handle").removeClass("ui-sortable-handle"),this._mouseDestroy();for(var e=this.items.length-1;e>=0;e--)this.items[e].item.removeData(this.widgetName+"-item");return this},_mouseCapture:function(t,i){var s=null,n=!1,a=this;return this.reverting?!1:this.options.disabled||"static"===this.options.type?!1:(this._refreshItems(t),e(t.target).parents().each(function(){return e.data(this,a.widgetName+"-item")===a?(s=e(this),!1):void 0}),e.data(t.target,a.widgetName+"-item")===a&&(s=e(t.target)),s?!this.options.handle||i||(e(this.options.handle,s).find("*").addBack().each(function(){this===t.target&&(n=!0)}),n)?(this.currentItem=s,this._removeCurrentsFromItems(),!0):!1:!1)},_mouseStart:function(t,i,s){var n,a,o=this.options;if(this.currentContainer=this,this.refreshPositions(),this.helper=this._createHelper(t),this._cacheHelperProportions(),this._cacheMargins(),this.scrollParent=this.helper.scrollParent(),this.offset=this.currentItem.offset(),this.offset={top:this.offset.top-this.margins.top,left:this.offset.left-this.margins.left},e.extend(this.offset,{click:{left:t.pageX-this.offset.left,top:t.pageY-this.offset.top},parent:this._getParentOffset(),relative:this._getRelativeOffset()}),this.helper.css("position","absolute"),this.cssPosition=this.helper.css("position"),this.originalPosition=this._generatePosition(t),this.originalPageX=t.pageX,this.originalPageY=t.pageY,o.cursorAt&&this._adjustOffsetFromHelper(o.cursorAt),this.domPosition={prev:this.currentItem.prev()[0],parent:this.currentItem.parent()[0]},this.helper[0]!==this.currentItem[0]&&this.currentItem.hide(),this._createPlaceholder(),o.containment&&this._setContainment(),o.cursor&&"auto"!==o.cursor&&(a=this.document.find("body"),this.storedCursor=a.css("cursor"),a.css("cursor",o.cursor),this.storedStylesheet=e("<style>*{ cursor: "+o.cursor+" !important; }</style>").appendTo(a)),o.opacity&&(this.helper.css("opacity")&&(this._storedOpacity=this.helper.css("opacity")),this.helper.css("opacity",o.opacity)),o.zIndex&&(this.helper.css("zIndex")&&(this._storedZIndex=this.helper.css("zIndex")),this.helper.css("zIndex",o.zIndex)),this.scrollParent[0]!==document&&"HTML"!==this.scrollParent[0].tagName&&(this.overflowOffset=this.scrollParent.offset()),this._trigger("start",t,this._uiHash()),this._preserveHelperProportions||this._cacheHelperProportions(),!s)for(n=this.containers.length-1;n>=0;n--)this.containers[n]._trigger("activate",t,this._uiHash(this));
return e.ui.ddmanager&&(e.ui.ddmanager.current=this),e.ui.ddmanager&&!o.dropBehaviour&&e.ui.ddmanager.prepareOffsets(this,t),this.dragging=!0,this.helper.addClass("ui-sortable-helper"),this._mouseDrag(t),!0},_mouseDrag:function(t){var i,s,n,a,o=this.options,r=!1;for(this.position=this._generatePosition(t),this.positionAbs=this._convertPositionTo("absolute"),this.lastPositionAbs||(this.lastPositionAbs=this.positionAbs),this.options.scroll&&(this.scrollParent[0]!==document&&"HTML"!==this.scrollParent[0].tagName?(this.overflowOffset.top+this.scrollParent[0].offsetHeight-t.pageY<o.scrollSensitivity?this.scrollParent[0].scrollTop=r=this.scrollParent[0].scrollTop+o.scrollSpeed:t.pageY-this.overflowOffset.top<o.scrollSensitivity&&(this.scrollParent[0].scrollTop=r=this.scrollParent[0].scrollTop-o.scrollSpeed),this.overflowOffset.left+this.scrollParent[0].offsetWidth-t.pageX<o.scrollSensitivity?this.scrollParent[0].scrollLeft=r=this.scrollParent[0].scrollLeft+o.scrollSpeed:t.pageX-this.overflowOffset.left<o.scrollSensitivity&&(this.scrollParent[0].scrollLeft=r=this.scrollParent[0].scrollLeft-o.scrollSpeed)):(t.pageY-e(document).scrollTop()<o.scrollSensitivity?r=e(document).scrollTop(e(document).scrollTop()-o.scrollSpeed):e(window).height()-(t.pageY-e(document).scrollTop())<o.scrollSensitivity&&(r=e(document).scrollTop(e(document).scrollTop()+o.scrollSpeed)),t.pageX-e(document).scrollLeft()<o.scrollSensitivity?r=e(document).scrollLeft(e(document).scrollLeft()-o.scrollSpeed):e(window).width()-(t.pageX-e(document).scrollLeft())<o.scrollSensitivity&&(r=e(document).scrollLeft(e(document).scrollLeft()+o.scrollSpeed))),r!==!1&&e.ui.ddmanager&&!o.dropBehaviour&&e.ui.ddmanager.prepareOffsets(this,t)),this.positionAbs=this._convertPositionTo("absolute"),this.options.axis&&"y"===this.options.axis||(this.helper[0].style.left=this.position.left+"px"),this.options.axis&&"x"===this.options.axis||(this.helper[0].style.top=this.position.top+"px"),i=this.items.length-1;i>=0;i--)if(s=this.items[i],n=s.item[0],a=this._intersectsWithPointer(s),a&&s.instance===this.currentContainer&&n!==this.currentItem[0]&&this.placeholder[1===a?"next":"prev"]()[0]!==n&&!e.contains(this.placeholder[0],n)&&("semi-dynamic"===this.options.type?!e.contains(this.element[0],n):!0)){if(this.direction=1===a?"down":"up","pointer"!==this.options.tolerance&&!this._intersectsWithSides(s))break;this._rearrange(t,s),this._trigger("change",t,this._uiHash());break}return this._contactContainers(t),e.ui.ddmanager&&e.ui.ddmanager.drag(this,t),this._trigger("sort",t,this._uiHash()),this.lastPositionAbs=this.positionAbs,!1},_mouseStop:function(t,i){if(t){if(e.ui.ddmanager&&!this.options.dropBehaviour&&e.ui.ddmanager.drop(this,t),this.options.revert){var s=this,n=this.placeholder.offset(),a=this.options.axis,o={};a&&"x"!==a||(o.left=n.left-this.offset.parent.left-this.margins.left+(this.offsetParent[0]===document.body?0:this.offsetParent[0].scrollLeft)),a&&"y"!==a||(o.top=n.top-this.offset.parent.top-this.margins.top+(this.offsetParent[0]===document.body?0:this.offsetParent[0].scrollTop)),this.reverting=!0,e(this.helper).animate(o,parseInt(this.options.revert,10)||500,function(){s._clear(t)})}else this._clear(t,i);return!1}},cancel:function(){if(this.dragging){this._mouseUp({target:null}),"original"===this.options.helper?this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper"):this.currentItem.show();for(var t=this.containers.length-1;t>=0;t--)this.containers[t]._trigger("deactivate",null,this._uiHash(this)),this.containers[t].containerCache.over&&(this.containers[t]._trigger("out",null,this._uiHash(this)),this.containers[t].containerCache.over=0)}return this.placeholder&&(this.placeholder[0].parentNode&&this.placeholder[0].parentNode.removeChild(this.placeholder[0]),"original"!==this.options.helper&&this.helper&&this.helper[0].parentNode&&this.helper.remove(),e.extend(this,{helper:null,dragging:!1,reverting:!1,_noFinalSort:null}),this.domPosition.prev?e(this.domPosition.prev).after(this.currentItem):e(this.domPosition.parent).prepend(this.currentItem)),this},serialize:function(t){var i=this._getItemsAsjQuery(t&&t.connected),s=[];return t=t||{},e(i).each(function(){var i=(e(t.item||this).attr(t.attribute||"id")||"").match(t.expression||/(.+)[\-=_](.+)/);i&&s.push((t.key||i[1]+"[]")+"="+(t.key&&t.expression?i[1]:i[2]))}),!s.length&&t.key&&s.push(t.key+"="),s.join("&")},toArray:function(t){var i=this._getItemsAsjQuery(t&&t.connected),s=[];return t=t||{},i.each(function(){s.push(e(t.item||this).attr(t.attribute||"id")||"")}),s},_intersectsWith:function(e){var t=this.positionAbs.left,i=t+this.helperProportions.width,s=this.positionAbs.top,n=s+this.helperProportions.height,a=e.left,o=a+e.width,r=e.top,h=r+e.height,l=this.offset.click.top,u=this.offset.click.left,d="x"===this.options.axis||s+l>r&&h>s+l,c="y"===this.options.axis||t+u>a&&o>t+u,p=d&&c;return"pointer"===this.options.tolerance||this.options.forcePointerForContainers||"pointer"!==this.options.tolerance&&this.helperProportions[this.floating?"width":"height"]>e[this.floating?"width":"height"]?p:t+this.helperProportions.width/2>a&&o>i-this.helperProportions.width/2&&s+this.helperProportions.height/2>r&&h>n-this.helperProportions.height/2},_intersectsWithPointer:function(e){var t="x"===this.options.axis||this._isOverAxis(this.positionAbs.top+this.offset.click.top,e.top,e.height),i="y"===this.options.axis||this._isOverAxis(this.positionAbs.left+this.offset.click.left,e.left,e.width),s=t&&i,n=this._getDragVerticalDirection(),a=this._getDragHorizontalDirection();return s?this.floating?a&&"right"===a||"down"===n?2:1:n&&("down"===n?2:1):!1},_intersectsWithSides:function(e){var t=this._isOverAxis(this.positionAbs.top+this.offset.click.top,e.top+e.height/2,e.height),i=this._isOverAxis(this.positionAbs.left+this.offset.click.left,e.left+e.width/2,e.width),s=this._getDragVerticalDirection(),n=this._getDragHorizontalDirection();return this.floating&&n?"right"===n&&i||"left"===n&&!i:s&&("down"===s&&t||"up"===s&&!t)},_getDragVerticalDirection:function(){var e=this.positionAbs.top-this.lastPositionAbs.top;return 0!==e&&(e>0?"down":"up")},_getDragHorizontalDirection:function(){var e=this.positionAbs.left-this.lastPositionAbs.left;return 0!==e&&(e>0?"right":"left")},refresh:function(e){return this._refreshItems(e),this._setHandleClassName(),this.refreshPositions(),this},_connectWith:function(){var e=this.options;return e.connectWith.constructor===String?[e.connectWith]:e.connectWith},_getItemsAsjQuery:function(t){function i(){r.push(this)}var s,n,a,o,r=[],h=[],l=this._connectWith();if(l&&t)for(s=l.length-1;s>=0;s--)for(a=e(l[s]),n=a.length-1;n>=0;n--)o=e.data(a[n],this.widgetFullName),o&&o!==this&&!o.options.disabled&&h.push([e.isFunction(o.options.items)?o.options.items.call(o.element):e(o.options.items,o.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"),o]);for(h.push([e.isFunction(this.options.items)?this.options.items.call(this.element,null,{options:this.options,item:this.currentItem}):e(this.options.items,this.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"),this]),s=h.length-1;s>=0;s--)h[s][0].each(i);return e(r)},_removeCurrentsFromItems:function(){var t=this.currentItem.find(":data("+this.widgetName+"-item)");this.items=e.grep(this.items,function(e){for(var i=0;t.length>i;i++)if(t[i]===e.item[0])return!1;return!0})},_refreshItems:function(t){this.items=[],this.containers=[this];var i,s,n,a,o,r,h,l,u=this.items,d=[[e.isFunction(this.options.items)?this.options.items.call(this.element[0],t,{item:this.currentItem}):e(this.options.items,this.element),this]],c=this._connectWith();if(c&&this.ready)for(i=c.length-1;i>=0;i--)for(n=e(c[i]),s=n.length-1;s>=0;s--)a=e.data(n[s],this.widgetFullName),a&&a!==this&&!a.options.disabled&&(d.push([e.isFunction(a.options.items)?a.options.items.call(a.element[0],t,{item:this.currentItem}):e(a.options.items,a.element),a]),this.containers.push(a));for(i=d.length-1;i>=0;i--)for(o=d[i][1],r=d[i][0],s=0,l=r.length;l>s;s++)h=e(r[s]),h.data(this.widgetName+"-item",o),u.push({item:h,instance:o,width:0,height:0,left:0,top:0})},refreshPositions:function(t){this.offsetParent&&this.helper&&(this.offset.parent=this._getParentOffset());var i,s,n,a;for(i=this.items.length-1;i>=0;i--)s=this.items[i],s.instance!==this.currentContainer&&this.currentContainer&&s.item[0]!==this.currentItem[0]||(n=this.options.toleranceElement?e(this.options.toleranceElement,s.item):s.item,t||(s.width=n.outerWidth(),s.height=n.outerHeight()),a=n.offset(),s.left=a.left,s.top=a.top);if(this.options.custom&&this.options.custom.refreshContainers)this.options.custom.refreshContainers.call(this);else for(i=this.containers.length-1;i>=0;i--)a=this.containers[i].element.offset(),this.containers[i].containerCache.left=a.left,this.containers[i].containerCache.top=a.top,this.containers[i].containerCache.width=this.containers[i].element.outerWidth(),this.containers[i].containerCache.height=this.containers[i].element.outerHeight();return this},_createPlaceholder:function(t){t=t||this;var i,s=t.options;s.placeholder&&s.placeholder.constructor!==String||(i=s.placeholder,s.placeholder={element:function(){var s=t.currentItem[0].nodeName.toLowerCase(),n=e("<"+s+">",t.document[0]).addClass(i||t.currentItem[0].className+" ui-sortable-placeholder").removeClass("ui-sortable-helper");return"tr"===s?t.currentItem.children().each(function(){e("<td>&#160;</td>",t.document[0]).attr("colspan",e(this).attr("colspan")||1).appendTo(n)}):"img"===s&&n.attr("src",t.currentItem.attr("src")),i||n.css("visibility","hidden"),n},update:function(e,n){(!i||s.forcePlaceholderSize)&&(n.height()||n.height(t.currentItem.innerHeight()-parseInt(t.currentItem.css("paddingTop")||0,10)-parseInt(t.currentItem.css("paddingBottom")||0,10)),n.width()||n.width(t.currentItem.innerWidth()-parseInt(t.currentItem.css("paddingLeft")||0,10)-parseInt(t.currentItem.css("paddingRight")||0,10)))}}),t.placeholder=e(s.placeholder.element.call(t.element,t.currentItem)),t.currentItem.after(t.placeholder),s.placeholder.update(t,t.placeholder)},_contactContainers:function(t){var i,s,n,a,o,r,h,l,u,d,c=null,p=null;for(i=this.containers.length-1;i>=0;i--)if(!e.contains(this.currentItem[0],this.containers[i].element[0]))if(this._intersectsWith(this.containers[i].containerCache)){if(c&&e.contains(this.containers[i].element[0],c.element[0]))continue;c=this.containers[i],p=i}else this.containers[i].containerCache.over&&(this.containers[i]._trigger("out",t,this._uiHash(this)),this.containers[i].containerCache.over=0);if(c)if(1===this.containers.length)this.containers[p].containerCache.over||(this.containers[p]._trigger("over",t,this._uiHash(this)),this.containers[p].containerCache.over=1);else{for(n=1e4,a=null,u=c.floating||this._isFloating(this.currentItem),o=u?"left":"top",r=u?"width":"height",d=u?"clientX":"clientY",s=this.items.length-1;s>=0;s--)e.contains(this.containers[p].element[0],this.items[s].item[0])&&this.items[s].item[0]!==this.currentItem[0]&&(h=this.items[s].item.offset()[o],l=!1,t[d]-h>this.items[s][r]/2&&(l=!0),n>Math.abs(t[d]-h)&&(n=Math.abs(t[d]-h),a=this.items[s],this.direction=l?"up":"down"));if(!a&&!this.options.dropOnEmpty)return;if(this.currentContainer===this.containers[p])return this.currentContainer.containerCache.over||(this.containers[p]._trigger("over",t,this._uiHash()),this.currentContainer.containerCache.over=1),void 0;a?this._rearrange(t,a,null,!0):this._rearrange(t,null,this.containers[p].element,!0),this._trigger("change",t,this._uiHash()),this.containers[p]._trigger("change",t,this._uiHash(this)),this.currentContainer=this.containers[p],this.options.placeholder.update(this.currentContainer,this.placeholder),this.containers[p]._trigger("over",t,this._uiHash(this)),this.containers[p].containerCache.over=1}},_createHelper:function(t){var i=this.options,s=e.isFunction(i.helper)?e(i.helper.apply(this.element[0],[t,this.currentItem])):"clone"===i.helper?this.currentItem.clone():this.currentItem;return s.parents("body").length||e("parent"!==i.appendTo?i.appendTo:this.currentItem[0].parentNode)[0].appendChild(s[0]),s[0]===this.currentItem[0]&&(this._storedCSS={width:this.currentItem[0].style.width,height:this.currentItem[0].style.height,position:this.currentItem.css("position"),top:this.currentItem.css("top"),left:this.currentItem.css("left")}),(!s[0].style.width||i.forceHelperSize)&&s.width(this.currentItem.width()),(!s[0].style.height||i.forceHelperSize)&&s.height(this.currentItem.height()),s},_adjustOffsetFromHelper:function(t){"string"==typeof t&&(t=t.split(" ")),e.isArray(t)&&(t={left:+t[0],top:+t[1]||0}),"left"in t&&(this.offset.click.left=t.left+this.margins.left),"right"in t&&(this.offset.click.left=this.helperProportions.width-t.right+this.margins.left),"top"in t&&(this.offset.click.top=t.top+this.margins.top),"bottom"in t&&(this.offset.click.top=this.helperProportions.height-t.bottom+this.margins.top)},_getParentOffset:function(){this.offsetParent=this.helper.offsetParent();var t=this.offsetParent.offset();return"absolute"===this.cssPosition&&this.scrollParent[0]!==document&&e.contains(this.scrollParent[0],this.offsetParent[0])&&(t.left+=this.scrollParent.scrollLeft(),t.top+=this.scrollParent.scrollTop()),(this.offsetParent[0]===document.body||this.offsetParent[0].tagName&&"html"===this.offsetParent[0].tagName.toLowerCase()&&e.ui.ie)&&(t={top:0,left:0}),{top:t.top+(parseInt(this.offsetParent.css("borderTopWidth"),10)||0),left:t.left+(parseInt(this.offsetParent.css("borderLeftWidth"),10)||0)}},_getRelativeOffset:function(){if("relative"===this.cssPosition){var e=this.currentItem.position();return{top:e.top-(parseInt(this.helper.css("top"),10)||0)+this.scrollParent.scrollTop(),left:e.left-(parseInt(this.helper.css("left"),10)||0)+this.scrollParent.scrollLeft()}}return{top:0,left:0}},_cacheMargins:function(){this.margins={left:parseInt(this.currentItem.css("marginLeft"),10)||0,top:parseInt(this.currentItem.css("marginTop"),10)||0}},_cacheHelperProportions:function(){this.helperProportions={width:this.helper.outerWidth(),height:this.helper.outerHeight()}},_setContainment:function(){var t,i,s,n=this.options;"parent"===n.containment&&(n.containment=this.helper[0].parentNode),("document"===n.containment||"window"===n.containment)&&(this.containment=[0-this.offset.relative.left-this.offset.parent.left,0-this.offset.relative.top-this.offset.parent.top,e("document"===n.containment?document:window).width()-this.helperProportions.width-this.margins.left,(e("document"===n.containment?document:window).height()||document.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top]),/^(document|window|parent)$/.test(n.containment)||(t=e(n.containment)[0],i=e(n.containment).offset(),s="hidden"!==e(t).css("overflow"),this.containment=[i.left+(parseInt(e(t).css("borderLeftWidth"),10)||0)+(parseInt(e(t).css("paddingLeft"),10)||0)-this.margins.left,i.top+(parseInt(e(t).css("borderTopWidth"),10)||0)+(parseInt(e(t).css("paddingTop"),10)||0)-this.margins.top,i.left+(s?Math.max(t.scrollWidth,t.offsetWidth):t.offsetWidth)-(parseInt(e(t).css("borderLeftWidth"),10)||0)-(parseInt(e(t).css("paddingRight"),10)||0)-this.helperProportions.width-this.margins.left,i.top+(s?Math.max(t.scrollHeight,t.offsetHeight):t.offsetHeight)-(parseInt(e(t).css("borderTopWidth"),10)||0)-(parseInt(e(t).css("paddingBottom"),10)||0)-this.helperProportions.height-this.margins.top])},_convertPositionTo:function(t,i){i||(i=this.position);var s="absolute"===t?1:-1,n="absolute"!==this.cssPosition||this.scrollParent[0]!==document&&e.contains(this.scrollParent[0],this.offsetParent[0])?this.scrollParent:this.offsetParent,a=/(html|body)/i.test(n[0].tagName);return{top:i.top+this.offset.relative.top*s+this.offset.parent.top*s-("fixed"===this.cssPosition?-this.scrollParent.scrollTop():a?0:n.scrollTop())*s,left:i.left+this.offset.relative.left*s+this.offset.parent.left*s-("fixed"===this.cssPosition?-this.scrollParent.scrollLeft():a?0:n.scrollLeft())*s}},_generatePosition:function(t){var i,s,n=this.options,a=t.pageX,o=t.pageY,r="absolute"!==this.cssPosition||this.scrollParent[0]!==document&&e.contains(this.scrollParent[0],this.offsetParent[0])?this.scrollParent:this.offsetParent,h=/(html|body)/i.test(r[0].tagName);return"relative"!==this.cssPosition||this.scrollParent[0]!==document&&this.scrollParent[0]!==this.offsetParent[0]||(this.offset.relative=this._getRelativeOffset()),this.originalPosition&&(this.containment&&(t.pageX-this.offset.click.left<this.containment[0]&&(a=this.containment[0]+this.offset.click.left),t.pageY-this.offset.click.top<this.containment[1]&&(o=this.containment[1]+this.offset.click.top),t.pageX-this.offset.click.left>this.containment[2]&&(a=this.containment[2]+this.offset.click.left),t.pageY-this.offset.click.top>this.containment[3]&&(o=this.containment[3]+this.offset.click.top)),n.grid&&(i=this.originalPageY+Math.round((o-this.originalPageY)/n.grid[1])*n.grid[1],o=this.containment?i-this.offset.click.top>=this.containment[1]&&i-this.offset.click.top<=this.containment[3]?i:i-this.offset.click.top>=this.containment[1]?i-n.grid[1]:i+n.grid[1]:i,s=this.originalPageX+Math.round((a-this.originalPageX)/n.grid[0])*n.grid[0],a=this.containment?s-this.offset.click.left>=this.containment[0]&&s-this.offset.click.left<=this.containment[2]?s:s-this.offset.click.left>=this.containment[0]?s-n.grid[0]:s+n.grid[0]:s)),{top:o-this.offset.click.top-this.offset.relative.top-this.offset.parent.top+("fixed"===this.cssPosition?-this.scrollParent.scrollTop():h?0:r.scrollTop()),left:a-this.offset.click.left-this.offset.relative.left-this.offset.parent.left+("fixed"===this.cssPosition?-this.scrollParent.scrollLeft():h?0:r.scrollLeft())}},_rearrange:function(e,t,i,s){i?i[0].appendChild(this.placeholder[0]):t.item[0].parentNode.insertBefore(this.placeholder[0],"down"===this.direction?t.item[0]:t.item[0].nextSibling),this.counter=this.counter?++this.counter:1;var n=this.counter;this._delay(function(){n===this.counter&&this.refreshPositions(!s)})},_clear:function(e,t){function i(e,t,i){return function(s){i._trigger(e,s,t._uiHash(t))}}this.reverting=!1;var s,n=[];if(!this._noFinalSort&&this.currentItem.parent().length&&this.placeholder.before(this.currentItem),this._noFinalSort=null,this.helper[0]===this.currentItem[0]){for(s in this._storedCSS)("auto"===this._storedCSS[s]||"static"===this._storedCSS[s])&&(this._storedCSS[s]="");this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper")}else this.currentItem.show();for(this.fromOutside&&!t&&n.push(function(e){this._trigger("receive",e,this._uiHash(this.fromOutside))}),!this.fromOutside&&this.domPosition.prev===this.currentItem.prev().not(".ui-sortable-helper")[0]&&this.domPosition.parent===this.currentItem.parent()[0]||t||n.push(function(e){this._trigger("update",e,this._uiHash())}),this!==this.currentContainer&&(t||(n.push(function(e){this._trigger("remove",e,this._uiHash())}),n.push(function(e){return function(t){e._trigger("receive",t,this._uiHash(this))}}.call(this,this.currentContainer)),n.push(function(e){return function(t){e._trigger("update",t,this._uiHash(this))}}.call(this,this.currentContainer)))),s=this.containers.length-1;s>=0;s--)t||n.push(i("deactivate",this,this.containers[s])),this.containers[s].containerCache.over&&(n.push(i("out",this,this.containers[s])),this.containers[s].containerCache.over=0);if(this.storedCursor&&(this.document.find("body").css("cursor",this.storedCursor),this.storedStylesheet.remove()),this._storedOpacity&&this.helper.css("opacity",this._storedOpacity),this._storedZIndex&&this.helper.css("zIndex","auto"===this._storedZIndex?"":this._storedZIndex),this.dragging=!1,t||this._trigger("beforeStop",e,this._uiHash()),this.placeholder[0].parentNode.removeChild(this.placeholder[0]),this.cancelHelperRemoval||(this.helper[0]!==this.currentItem[0]&&this.helper.remove(),this.helper=null),!t){for(s=0;n.length>s;s++)n[s].call(this,e);this._trigger("stop",e,this._uiHash())}return this.fromOutside=!1,!this.cancelHelperRemoval},_trigger:function(){e.Widget.prototype._trigger.apply(this,arguments)===!1&&this.cancel()},_uiHash:function(t){var i=t||this;return{helper:i.helper,placeholder:i.placeholder||e([]),position:i.position,originalPosition:i.originalPosition,offset:i.positionAbs,item:i.currentItem,sender:t?t.element:null}}}),e.widget("ui.spinner",{version:"1.11.2",defaultElement:"<input>",widgetEventPrefix:"spin",options:{culture:null,icons:{down:"ui-icon-triangle-1-s",up:"ui-icon-triangle-1-n"},incremental:!0,max:null,min:null,numberFormat:null,page:10,step:1,change:null,spin:null,start:null,stop:null},_create:function(){this._setOption("max",this.options.max),this._setOption("min",this.options.min),this._setOption("step",this.options.step),""!==this.value()&&this._value(this.element.val(),!0),this._draw(),this._on(this._events),this._refresh(),this._on(this.window,{beforeunload:function(){this.element.removeAttr("autocomplete")}})},_getCreateOptions:function(){var t={},i=this.element;return e.each(["min","max","step"],function(e,s){var n=i.attr(s);void 0!==n&&n.length&&(t[s]=n)}),t},_events:{keydown:function(e){this._start(e)&&this._keydown(e)&&e.preventDefault()},keyup:"_stop",focus:function(){this.previous=this.element.val()},blur:function(e){return this.cancelBlur?(delete this.cancelBlur,void 0):(this._stop(),this._refresh(),this.previous!==this.element.val()&&this._trigger("change",e),void 0)},mousewheel:function(e,t){if(t){if(!this.spinning&&!this._start(e))return!1;this._spin((t>0?1:-1)*this.options.step,e),clearTimeout(this.mousewheelTimer),this.mousewheelTimer=this._delay(function(){this.spinning&&this._stop(e)},100),e.preventDefault()}},"mousedown .ui-spinner-button":function(t){function i(){var e=this.element[0]===this.document[0].activeElement;e||(this.element.focus(),this.previous=s,this._delay(function(){this.previous=s}))}var s;s=this.element[0]===this.document[0].activeElement?this.previous:this.element.val(),t.preventDefault(),i.call(this),this.cancelBlur=!0,this._delay(function(){delete this.cancelBlur,i.call(this)}),this._start(t)!==!1&&this._repeat(null,e(t.currentTarget).hasClass("ui-spinner-up")?1:-1,t)},"mouseup .ui-spinner-button":"_stop","mouseenter .ui-spinner-button":function(t){return e(t.currentTarget).hasClass("ui-state-active")?this._start(t)===!1?!1:(this._repeat(null,e(t.currentTarget).hasClass("ui-spinner-up")?1:-1,t),void 0):void 0},"mouseleave .ui-spinner-button":"_stop"},_draw:function(){var e=this.uiSpinner=this.element.addClass("ui-spinner-input").attr("autocomplete","off").wrap(this._uiSpinnerHtml()).parent().append(this._buttonHtml());this.element.attr("role","spinbutton"),this.buttons=e.find(".ui-spinner-button").attr("tabIndex",-1).button().removeClass("ui-corner-all"),this.buttons.height()>Math.ceil(.5*e.height())&&e.height()>0&&e.height(e.height()),this.options.disabled&&this.disable()},_keydown:function(t){var i=this.options,s=e.ui.keyCode;switch(t.keyCode){case s.UP:return this._repeat(null,1,t),!0;case s.DOWN:return this._repeat(null,-1,t),!0;case s.PAGE_UP:return this._repeat(null,i.page,t),!0;case s.PAGE_DOWN:return this._repeat(null,-i.page,t),!0}return!1},_uiSpinnerHtml:function(){return"<span class='ui-spinner ui-widget ui-widget-content ui-corner-all'></span>"},_buttonHtml:function(){return"<a class='ui-spinner-button ui-spinner-up ui-corner-tr'><span class='ui-icon "+this.options.icons.up+"'>&#9650;</span>"+"</a>"+"<a class='ui-spinner-button ui-spinner-down ui-corner-br'>"+"<span class='ui-icon "+this.options.icons.down+"'>&#9660;</span>"+"</a>"},_start:function(e){return this.spinning||this._trigger("start",e)!==!1?(this.counter||(this.counter=1),this.spinning=!0,!0):!1},_repeat:function(e,t,i){e=e||500,clearTimeout(this.timer),this.timer=this._delay(function(){this._repeat(40,t,i)},e),this._spin(t*this.options.step,i)},_spin:function(e,t){var i=this.value()||0;this.counter||(this.counter=1),i=this._adjustValue(i+e*this._increment(this.counter)),this.spinning&&this._trigger("spin",t,{value:i})===!1||(this._value(i),this.counter++)},_increment:function(t){var i=this.options.incremental;return i?e.isFunction(i)?i(t):Math.floor(t*t*t/5e4-t*t/500+17*t/200+1):1},_precision:function(){var e=this._precisionOf(this.options.step);return null!==this.options.min&&(e=Math.max(e,this._precisionOf(this.options.min))),e},_precisionOf:function(e){var t=""+e,i=t.indexOf(".");return-1===i?0:t.length-i-1},_adjustValue:function(e){var t,i,s=this.options;return t=null!==s.min?s.min:0,i=e-t,i=Math.round(i/s.step)*s.step,e=t+i,e=parseFloat(e.toFixed(this._precision())),null!==s.max&&e>s.max?s.max:null!==s.min&&s.min>e?s.min:e},_stop:function(e){this.spinning&&(clearTimeout(this.timer),clearTimeout(this.mousewheelTimer),this.counter=0,this.spinning=!1,this._trigger("stop",e))},_setOption:function(e,t){if("culture"===e||"numberFormat"===e){var i=this._parse(this.element.val());return this.options[e]=t,this.element.val(this._format(i)),void 0}("max"===e||"min"===e||"step"===e)&&"string"==typeof t&&(t=this._parse(t)),"icons"===e&&(this.buttons.first().find(".ui-icon").removeClass(this.options.icons.up).addClass(t.up),this.buttons.last().find(".ui-icon").removeClass(this.options.icons.down).addClass(t.down)),this._super(e,t),"disabled"===e&&(this.widget().toggleClass("ui-state-disabled",!!t),this.element.prop("disabled",!!t),this.buttons.button(t?"disable":"enable"))},_setOptions:h(function(e){this._super(e)}),_parse:function(e){return"string"==typeof e&&""!==e&&(e=window.Globalize&&this.options.numberFormat?Globalize.parseFloat(e,10,this.options.culture):+e),""===e||isNaN(e)?null:e},_format:function(e){return""===e?"":window.Globalize&&this.options.numberFormat?Globalize.format(e,this.options.numberFormat,this.options.culture):e},_refresh:function(){this.element.attr({"aria-valuemin":this.options.min,"aria-valuemax":this.options.max,"aria-valuenow":this._parse(this.element.val())})},isValid:function(){var e=this.value();return null===e?!1:e===this._adjustValue(e)},_value:function(e,t){var i;""!==e&&(i=this._parse(e),null!==i&&(t||(i=this._adjustValue(i)),e=this._format(i))),this.element.val(e),this._refresh()},_destroy:function(){this.element.removeClass("ui-spinner-input").prop("disabled",!1).removeAttr("autocomplete").removeAttr("role").removeAttr("aria-valuemin").removeAttr("aria-valuemax").removeAttr("aria-valuenow"),this.uiSpinner.replaceWith(this.element)},stepUp:h(function(e){this._stepUp(e)}),_stepUp:function(e){this._start()&&(this._spin((e||1)*this.options.step),this._stop())},stepDown:h(function(e){this._stepDown(e)}),_stepDown:function(e){this._start()&&(this._spin((e||1)*-this.options.step),this._stop())},pageUp:h(function(e){this._stepUp((e||1)*this.options.page)}),pageDown:h(function(e){this._stepDown((e||1)*this.options.page)}),value:function(e){return arguments.length?(h(this._value).call(this,e),void 0):this._parse(this.element.val())},widget:function(){return this.uiSpinner}}),e.widget("ui.tabs",{version:"1.11.2",delay:300,options:{active:null,collapsible:!1,event:"click",heightStyle:"content",hide:null,show:null,activate:null,beforeActivate:null,beforeLoad:null,load:null},_isLocal:function(){var e=/#.*$/;return function(t){var i,s;t=t.cloneNode(!1),i=t.href.replace(e,""),s=location.href.replace(e,"");try{i=decodeURIComponent(i)}catch(n){}try{s=decodeURIComponent(s)}catch(n){}return t.hash.length>1&&i===s}}(),_create:function(){var t=this,i=this.options;this.running=!1,this.element.addClass("ui-tabs ui-widget ui-widget-content ui-corner-all").toggleClass("ui-tabs-collapsible",i.collapsible),this._processTabs(),i.active=this._initialActive(),e.isArray(i.disabled)&&(i.disabled=e.unique(i.disabled.concat(e.map(this.tabs.filter(".ui-state-disabled"),function(e){return t.tabs.index(e)}))).sort()),this.active=this.options.active!==!1&&this.anchors.length?this._findActive(i.active):e(),this._refresh(),this.active.length&&this.load(i.active)},_initialActive:function(){var t=this.options.active,i=this.options.collapsible,s=location.hash.substring(1);return null===t&&(s&&this.tabs.each(function(i,n){return e(n).attr("aria-controls")===s?(t=i,!1):void 0}),null===t&&(t=this.tabs.index(this.tabs.filter(".ui-tabs-active"))),(null===t||-1===t)&&(t=this.tabs.length?0:!1)),t!==!1&&(t=this.tabs.index(this.tabs.eq(t)),-1===t&&(t=i?!1:0)),!i&&t===!1&&this.anchors.length&&(t=0),t},_getCreateEventData:function(){return{tab:this.active,panel:this.active.length?this._getPanelForTab(this.active):e()}},_tabKeydown:function(t){var i=e(this.document[0].activeElement).closest("li"),s=this.tabs.index(i),n=!0;if(!this._handlePageNav(t)){switch(t.keyCode){case e.ui.keyCode.RIGHT:case e.ui.keyCode.DOWN:s++;break;case e.ui.keyCode.UP:case e.ui.keyCode.LEFT:n=!1,s--;break;case e.ui.keyCode.END:s=this.anchors.length-1;break;case e.ui.keyCode.HOME:s=0;break;case e.ui.keyCode.SPACE:return t.preventDefault(),clearTimeout(this.activating),this._activate(s),void 0;case e.ui.keyCode.ENTER:return t.preventDefault(),clearTimeout(this.activating),this._activate(s===this.options.active?!1:s),void 0;default:return}t.preventDefault(),clearTimeout(this.activating),s=this._focusNextTab(s,n),t.ctrlKey||(i.attr("aria-selected","false"),this.tabs.eq(s).attr("aria-selected","true"),this.activating=this._delay(function(){this.option("active",s)},this.delay))}},_panelKeydown:function(t){this._handlePageNav(t)||t.ctrlKey&&t.keyCode===e.ui.keyCode.UP&&(t.preventDefault(),this.active.focus())},_handlePageNav:function(t){return t.altKey&&t.keyCode===e.ui.keyCode.PAGE_UP?(this._activate(this._focusNextTab(this.options.active-1,!1)),!0):t.altKey&&t.keyCode===e.ui.keyCode.PAGE_DOWN?(this._activate(this._focusNextTab(this.options.active+1,!0)),!0):void 0},_findNextTab:function(t,i){function s(){return t>n&&(t=0),0>t&&(t=n),t}for(var n=this.tabs.length-1;-1!==e.inArray(s(),this.options.disabled);)t=i?t+1:t-1;return t},_focusNextTab:function(e,t){return e=this._findNextTab(e,t),this.tabs.eq(e).focus(),e},_setOption:function(e,t){return"active"===e?(this._activate(t),void 0):"disabled"===e?(this._setupDisabled(t),void 0):(this._super(e,t),"collapsible"===e&&(this.element.toggleClass("ui-tabs-collapsible",t),t||this.options.active!==!1||this._activate(0)),"event"===e&&this._setupEvents(t),"heightStyle"===e&&this._setupHeightStyle(t),void 0)},_sanitizeSelector:function(e){return e?e.replace(/[!"$%&'()*+,.\/:;<=>?@\[\]\^`{|}~]/g,"\\$&"):""},refresh:function(){var t=this.options,i=this.tablist.children(":has(a[href])");t.disabled=e.map(i.filter(".ui-state-disabled"),function(e){return i.index(e)}),this._processTabs(),t.active!==!1&&this.anchors.length?this.active.length&&!e.contains(this.tablist[0],this.active[0])?this.tabs.length===t.disabled.length?(t.active=!1,this.active=e()):this._activate(this._findNextTab(Math.max(0,t.active-1),!1)):t.active=this.tabs.index(this.active):(t.active=!1,this.active=e()),this._refresh()},_refresh:function(){this._setupDisabled(this.options.disabled),this._setupEvents(this.options.event),this._setupHeightStyle(this.options.heightStyle),this.tabs.not(this.active).attr({"aria-selected":"false","aria-expanded":"false",tabIndex:-1}),this.panels.not(this._getPanelForTab(this.active)).hide().attr({"aria-hidden":"true"}),this.active.length?(this.active.addClass("ui-tabs-active ui-state-active").attr({"aria-selected":"true","aria-expanded":"true",tabIndex:0}),this._getPanelForTab(this.active).show().attr({"aria-hidden":"false"})):this.tabs.eq(0).attr("tabIndex",0)},_processTabs:function(){var t=this,i=this.tabs,s=this.anchors,n=this.panels;this.tablist=this._getList().addClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all").attr("role","tablist").delegate("> li","mousedown"+this.eventNamespace,function(t){e(this).is(".ui-state-disabled")&&t.preventDefault()}).delegate(".ui-tabs-anchor","focus"+this.eventNamespace,function(){e(this).closest("li").is(".ui-state-disabled")&&this.blur()}),this.tabs=this.tablist.find("> li:has(a[href])").addClass("ui-state-default ui-corner-top").attr({role:"tab",tabIndex:-1}),this.anchors=this.tabs.map(function(){return e("a",this)[0]
}).addClass("ui-tabs-anchor").attr({role:"presentation",tabIndex:-1}),this.panels=e(),this.anchors.each(function(i,s){var n,a,o,r=e(s).uniqueId().attr("id"),h=e(s).closest("li"),l=h.attr("aria-controls");t._isLocal(s)?(n=s.hash,o=n.substring(1),a=t.element.find(t._sanitizeSelector(n))):(o=h.attr("aria-controls")||e({}).uniqueId()[0].id,n="#"+o,a=t.element.find(n),a.length||(a=t._createPanel(o),a.insertAfter(t.panels[i-1]||t.tablist)),a.attr("aria-live","polite")),a.length&&(t.panels=t.panels.add(a)),l&&h.data("ui-tabs-aria-controls",l),h.attr({"aria-controls":o,"aria-labelledby":r}),a.attr("aria-labelledby",r)}),this.panels.addClass("ui-tabs-panel ui-widget-content ui-corner-bottom").attr("role","tabpanel"),i&&(this._off(i.not(this.tabs)),this._off(s.not(this.anchors)),this._off(n.not(this.panels)))},_getList:function(){return this.tablist||this.element.find("ol,ul").eq(0)},_createPanel:function(t){return e("<div>").attr("id",t).addClass("ui-tabs-panel ui-widget-content ui-corner-bottom").data("ui-tabs-destroy",!0)},_setupDisabled:function(t){e.isArray(t)&&(t.length?t.length===this.anchors.length&&(t=!0):t=!1);for(var i,s=0;i=this.tabs[s];s++)t===!0||-1!==e.inArray(s,t)?e(i).addClass("ui-state-disabled").attr("aria-disabled","true"):e(i).removeClass("ui-state-disabled").removeAttr("aria-disabled");this.options.disabled=t},_setupEvents:function(t){var i={};t&&e.each(t.split(" "),function(e,t){i[t]="_eventHandler"}),this._off(this.anchors.add(this.tabs).add(this.panels)),this._on(!0,this.anchors,{click:function(e){e.preventDefault()}}),this._on(this.anchors,i),this._on(this.tabs,{keydown:"_tabKeydown"}),this._on(this.panels,{keydown:"_panelKeydown"}),this._focusable(this.tabs),this._hoverable(this.tabs)},_setupHeightStyle:function(t){var i,s=this.element.parent();"fill"===t?(i=s.height(),i-=this.element.outerHeight()-this.element.height(),this.element.siblings(":visible").each(function(){var t=e(this),s=t.css("position");"absolute"!==s&&"fixed"!==s&&(i-=t.outerHeight(!0))}),this.element.children().not(this.panels).each(function(){i-=e(this).outerHeight(!0)}),this.panels.each(function(){e(this).height(Math.max(0,i-e(this).innerHeight()+e(this).height()))}).css("overflow","auto")):"auto"===t&&(i=0,this.panels.each(function(){i=Math.max(i,e(this).height("").height())}).height(i))},_eventHandler:function(t){var i=this.options,s=this.active,n=e(t.currentTarget),a=n.closest("li"),o=a[0]===s[0],r=o&&i.collapsible,h=r?e():this._getPanelForTab(a),l=s.length?this._getPanelForTab(s):e(),u={oldTab:s,oldPanel:l,newTab:r?e():a,newPanel:h};t.preventDefault(),a.hasClass("ui-state-disabled")||a.hasClass("ui-tabs-loading")||this.running||o&&!i.collapsible||this._trigger("beforeActivate",t,u)===!1||(i.active=r?!1:this.tabs.index(a),this.active=o?e():a,this.xhr&&this.xhr.abort(),l.length||h.length||e.error("jQuery UI Tabs: Mismatching fragment identifier."),h.length&&this.load(this.tabs.index(a),t),this._toggle(t,u))},_toggle:function(t,i){function s(){a.running=!1,a._trigger("activate",t,i)}function n(){i.newTab.closest("li").addClass("ui-tabs-active ui-state-active"),o.length&&a.options.show?a._show(o,a.options.show,s):(o.show(),s())}var a=this,o=i.newPanel,r=i.oldPanel;this.running=!0,r.length&&this.options.hide?this._hide(r,this.options.hide,function(){i.oldTab.closest("li").removeClass("ui-tabs-active ui-state-active"),n()}):(i.oldTab.closest("li").removeClass("ui-tabs-active ui-state-active"),r.hide(),n()),r.attr("aria-hidden","true"),i.oldTab.attr({"aria-selected":"false","aria-expanded":"false"}),o.length&&r.length?i.oldTab.attr("tabIndex",-1):o.length&&this.tabs.filter(function(){return 0===e(this).attr("tabIndex")}).attr("tabIndex",-1),o.attr("aria-hidden","false"),i.newTab.attr({"aria-selected":"true","aria-expanded":"true",tabIndex:0})},_activate:function(t){var i,s=this._findActive(t);s[0]!==this.active[0]&&(s.length||(s=this.active),i=s.find(".ui-tabs-anchor")[0],this._eventHandler({target:i,currentTarget:i,preventDefault:e.noop}))},_findActive:function(t){return t===!1?e():this.tabs.eq(t)},_getIndex:function(e){return"string"==typeof e&&(e=this.anchors.index(this.anchors.filter("[href$='"+e+"']"))),e},_destroy:function(){this.xhr&&this.xhr.abort(),this.element.removeClass("ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible"),this.tablist.removeClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all").removeAttr("role"),this.anchors.removeClass("ui-tabs-anchor").removeAttr("role").removeAttr("tabIndex").removeUniqueId(),this.tablist.unbind(this.eventNamespace),this.tabs.add(this.panels).each(function(){e.data(this,"ui-tabs-destroy")?e(this).remove():e(this).removeClass("ui-state-default ui-state-active ui-state-disabled ui-corner-top ui-corner-bottom ui-widget-content ui-tabs-active ui-tabs-panel").removeAttr("tabIndex").removeAttr("aria-live").removeAttr("aria-busy").removeAttr("aria-selected").removeAttr("aria-labelledby").removeAttr("aria-hidden").removeAttr("aria-expanded").removeAttr("role")}),this.tabs.each(function(){var t=e(this),i=t.data("ui-tabs-aria-controls");i?t.attr("aria-controls",i).removeData("ui-tabs-aria-controls"):t.removeAttr("aria-controls")}),this.panels.show(),"content"!==this.options.heightStyle&&this.panels.css("height","")},enable:function(t){var i=this.options.disabled;i!==!1&&(void 0===t?i=!1:(t=this._getIndex(t),i=e.isArray(i)?e.map(i,function(e){return e!==t?e:null}):e.map(this.tabs,function(e,i){return i!==t?i:null})),this._setupDisabled(i))},disable:function(t){var i=this.options.disabled;if(i!==!0){if(void 0===t)i=!0;else{if(t=this._getIndex(t),-1!==e.inArray(t,i))return;i=e.isArray(i)?e.merge([t],i).sort():[t]}this._setupDisabled(i)}},load:function(t,i){t=this._getIndex(t);var s=this,n=this.tabs.eq(t),a=n.find(".ui-tabs-anchor"),o=this._getPanelForTab(n),r={tab:n,panel:o};this._isLocal(a[0])||(this.xhr=e.ajax(this._ajaxSettings(a,i,r)),this.xhr&&"canceled"!==this.xhr.statusText&&(n.addClass("ui-tabs-loading"),o.attr("aria-busy","true"),this.xhr.success(function(e){setTimeout(function(){o.html(e),s._trigger("load",i,r)},1)}).complete(function(e,t){setTimeout(function(){"abort"===t&&s.panels.stop(!1,!0),n.removeClass("ui-tabs-loading"),o.removeAttr("aria-busy"),e===s.xhr&&delete s.xhr},1)})))},_ajaxSettings:function(t,i,s){var n=this;return{url:t.attr("href"),beforeSend:function(t,a){return n._trigger("beforeLoad",i,e.extend({jqXHR:t,ajaxSettings:a},s))}}},_getPanelForTab:function(t){var i=e(t).attr("aria-controls");return this.element.find(this._sanitizeSelector("#"+i))}}),e.widget("ui.tooltip",{version:"1.11.2",options:{content:function(){var t=e(this).attr("title")||"";return e("<a>").text(t).html()},hide:!0,items:"[title]:not([disabled])",position:{my:"left top+15",at:"left bottom",collision:"flipfit flip"},show:!0,tooltipClass:null,track:!1,close:null,open:null},_addDescribedBy:function(t,i){var s=(t.attr("aria-describedby")||"").split(/\s+/);s.push(i),t.data("ui-tooltip-id",i).attr("aria-describedby",e.trim(s.join(" ")))},_removeDescribedBy:function(t){var i=t.data("ui-tooltip-id"),s=(t.attr("aria-describedby")||"").split(/\s+/),n=e.inArray(i,s);-1!==n&&s.splice(n,1),t.removeData("ui-tooltip-id"),s=e.trim(s.join(" ")),s?t.attr("aria-describedby",s):t.removeAttr("aria-describedby")},_create:function(){this._on({mouseover:"open",focusin:"open"}),this.tooltips={},this.parents={},this.options.disabled&&this._disable(),this.liveRegion=e("<div>").attr({role:"log","aria-live":"assertive","aria-relevant":"additions"}).addClass("ui-helper-hidden-accessible").appendTo(this.document[0].body)},_setOption:function(t,i){var s=this;return"disabled"===t?(this[i?"_disable":"_enable"](),this.options[t]=i,void 0):(this._super(t,i),"content"===t&&e.each(this.tooltips,function(e,t){s._updateContent(t.element)}),void 0)},_disable:function(){var t=this;e.each(this.tooltips,function(i,s){var n=e.Event("blur");n.target=n.currentTarget=s.element[0],t.close(n,!0)}),this.element.find(this.options.items).addBack().each(function(){var t=e(this);t.is("[title]")&&t.data("ui-tooltip-title",t.attr("title")).removeAttr("title")})},_enable:function(){this.element.find(this.options.items).addBack().each(function(){var t=e(this);t.data("ui-tooltip-title")&&t.attr("title",t.data("ui-tooltip-title"))})},open:function(t){var i=this,s=e(t?t.target:this.element).closest(this.options.items);s.length&&!s.data("ui-tooltip-id")&&(s.attr("title")&&s.data("ui-tooltip-title",s.attr("title")),s.data("ui-tooltip-open",!0),t&&"mouseover"===t.type&&s.parents().each(function(){var t,s=e(this);s.data("ui-tooltip-open")&&(t=e.Event("blur"),t.target=t.currentTarget=this,i.close(t,!0)),s.attr("title")&&(s.uniqueId(),i.parents[this.id]={element:this,title:s.attr("title")},s.attr("title",""))}),this._updateContent(s,t))},_updateContent:function(e,t){var i,s=this.options.content,n=this,a=t?t.type:null;return"string"==typeof s?this._open(t,e,s):(i=s.call(e[0],function(i){e.data("ui-tooltip-open")&&n._delay(function(){t&&(t.type=a),this._open(t,e,i)})}),i&&this._open(t,e,i),void 0)},_open:function(t,i,s){function n(e){u.of=e,o.is(":hidden")||o.position(u)}var a,o,r,h,l,u=e.extend({},this.options.position);if(s){if(a=this._find(i))return a.tooltip.find(".ui-tooltip-content").html(s),void 0;i.is("[title]")&&(t&&"mouseover"===t.type?i.attr("title",""):i.removeAttr("title")),a=this._tooltip(i),o=a.tooltip,this._addDescribedBy(i,o.attr("id")),o.find(".ui-tooltip-content").html(s),this.liveRegion.children().hide(),s.clone?(l=s.clone(),l.removeAttr("id").find("[id]").removeAttr("id")):l=s,e("<div>").html(l).appendTo(this.liveRegion),this.options.track&&t&&/^mouse/.test(t.type)?(this._on(this.document,{mousemove:n}),n(t)):o.position(e.extend({of:i},this.options.position)),o.hide(),this._show(o,this.options.show),this.options.show&&this.options.show.delay&&(h=this.delayedShow=setInterval(function(){o.is(":visible")&&(n(u.of),clearInterval(h))},e.fx.interval)),this._trigger("open",t,{tooltip:o}),r={keyup:function(t){if(t.keyCode===e.ui.keyCode.ESCAPE){var s=e.Event(t);s.currentTarget=i[0],this.close(s,!0)}}},i[0]!==this.element[0]&&(r.remove=function(){this._removeTooltip(o)}),t&&"mouseover"!==t.type||(r.mouseleave="close"),t&&"focusin"!==t.type||(r.focusout="close"),this._on(!0,i,r)}},close:function(t){var i,s=this,n=e(t?t.currentTarget:this.element),a=this._find(n);a&&(i=a.tooltip,a.closing||(clearInterval(this.delayedShow),n.data("ui-tooltip-title")&&!n.attr("title")&&n.attr("title",n.data("ui-tooltip-title")),this._removeDescribedBy(n),a.hiding=!0,i.stop(!0),this._hide(i,this.options.hide,function(){s._removeTooltip(e(this))}),n.removeData("ui-tooltip-open"),this._off(n,"mouseleave focusout keyup"),n[0]!==this.element[0]&&this._off(n,"remove"),this._off(this.document,"mousemove"),t&&"mouseleave"===t.type&&e.each(this.parents,function(t,i){e(i.element).attr("title",i.title),delete s.parents[t]}),a.closing=!0,this._trigger("close",t,{tooltip:i}),a.hiding||(a.closing=!1)))},_tooltip:function(t){var i=e("<div>").attr("role","tooltip").addClass("ui-tooltip ui-widget ui-corner-all ui-widget-content "+(this.options.tooltipClass||"")),s=i.uniqueId().attr("id");return e("<div>").addClass("ui-tooltip-content").appendTo(i),i.appendTo(this.document[0].body),this.tooltips[s]={element:t,tooltip:i}},_find:function(e){var t=e.data("ui-tooltip-id");return t?this.tooltips[t]:null},_removeTooltip:function(e){e.remove(),delete this.tooltips[e.attr("id")]},_destroy:function(){var t=this;e.each(this.tooltips,function(i,s){var n=e.Event("blur"),a=s.element;n.target=n.currentTarget=a[0],t.close(n,!0),e("#"+i).remove(),a.data("ui-tooltip-title")&&(a.attr("title")||a.attr("title",a.data("ui-tooltip-title")),a.removeData("ui-tooltip-title"))}),this.liveRegion.remove()}})});
/*
 * jQuery blockUI plugin
 * Version 2.59.0-2013.04.05
 * @requires jQuery v1.7 or later
 *
 * Examples at: http://malsup.com/jquery/block/
 * Copyright (c) 2007-2013 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * Thanks to Amir-Hossein Sobhi for some excellent contributions!
 */
(function(){function n(b){function n(d,a){var e,h,f=d==window,g=a&&void 0!==a.message?a.message:void 0;a=b.extend({},b.blockUI.defaults,a||{});if(!a.ignoreIfBlocked||!b(d).data("blockUI.isBlocked")){a.overlayCSS=b.extend({},b.blockUI.defaults.overlayCSS,a.overlayCSS||{});e=b.extend({},b.blockUI.defaults.css,a.css||{});a.onOverlayClick&&(a.overlayCSS.cursor="pointer");h=b.extend({},b.blockUI.defaults.themedCSS,a.themedCSS||{});g=void 0===g?a.message:g;f&&l&&r(window,{fadeOut:0});if(g&&"string"!=typeof g&&
(g.parentNode||g.jquery)){var j=g.jquery?g[0]:g,c={};b(d).data("blockUI.history",c);c.el=j;c.parent=j.parentNode;c.display=j.style.display;c.position=j.style.position;c.parent&&c.parent.removeChild(j)}b(d).data("blockUI.onUnblock",a.onUnblock);var c=a.baseZ,k;k=s||a.forceIframe?b('<iframe class="blockUI" style="z-index:'+c++ +';display:none;border:none;margin:0;padding:0;position:absolute;width:100%;height:100%;top:0;left:0" src="'+a.iframeSrc+'"></iframe>'):b('<div class="blockUI" style="display:none"></div>');
j=a.theme?b('<div class="blockUI blockOverlay ui-widget-overlay" style="z-index:'+c++ +';display:none"></div>'):b('<div class="blockUI blockOverlay" style="z-index:'+c++ +';display:none;border:none;margin:0;padding:0;width:100%;height:100%;top:0;left:0"></div>');a.theme&&f?(c='<div class="blockUI '+a.blockMsgClass+' blockPage ui-dialog ui-widget ui-corner-all" style="z-index:'+(c+10)+';display:none;position:fixed">',a.title&&(c+='<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+
(a.title||"&nbsp;")+"</div>"),c+='<div class="ui-widget-content ui-dialog-content"></div></div>'):a.theme?(c='<div class="blockUI '+a.blockMsgClass+' blockElement ui-dialog ui-widget ui-corner-all" style="z-index:'+(c+10)+';display:none;position:absolute">',a.title&&(c+='<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+(a.title||"&nbsp;")+"</div>"),c+='<div class="ui-widget-content ui-dialog-content"></div>',c+="</div>"):c=f?'<div class="blockUI '+a.blockMsgClass+' blockPage" style="z-index:'+
(c+10)+';display:none;position:fixed"></div>':'<div class="blockUI '+a.blockMsgClass+' blockElement" style="z-index:'+(c+10)+';display:none;position:absolute"></div>';c=b(c);g&&(a.theme?(c.css(h),c.addClass("ui-widget-content")):c.css(e));a.theme||j.css(a.overlayCSS);j.css("position",f?"fixed":"absolute");(s||a.forceIframe)&&k.css("opacity",0);e=[k,j,c];var q=f?b("body"):b(d);b.each(e,function(){this.appendTo(q)});a.theme&&(a.draggable&&b.fn.draggable)&&c.draggable({handle:".ui-dialog-titlebar",cancel:"li"});
h=z&&(!b.support.boxModel||0<b("object,embed",f?null:d).length);if(u||h){f&&(a.allowBodyStretch&&b.support.boxModel)&&b("html,body").css("height","100%");if((u||!b.support.boxModel)&&!f){h=parseInt(b.css(d,"borderTopWidth"),10)||0;var p=parseInt(b.css(d,"borderLeftWidth"),10)||0,v=h?"(0 - "+h+")":0,w=p?"(0 - "+p+")":0}b.each(e,function(b,d){var c=d[0].style;c.position="absolute";if(2>b)f?c.setExpression("height","Math.max(document.body.scrollHeight, document.body.offsetHeight) - (jQuery.support.boxModel?0:"+
a.quirksmodeOffsetHack+') + "px"'):c.setExpression("height",'this.parentNode.offsetHeight + "px"'),f?c.setExpression("width",'jQuery.support.boxModel && document.documentElement.clientWidth || document.body.clientWidth + "px"'):c.setExpression("width",'this.parentNode.offsetWidth + "px"'),w&&c.setExpression("left",w),v&&c.setExpression("top",v);else if(a.centerY)f&&c.setExpression("top",'(document.documentElement.clientHeight || document.body.clientHeight) / 2 - (this.offsetHeight / 2) + (blah = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "px"'),
c.marginTop=0;else if(!a.centerY&&f){var e="((document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "+(a.css&&a.css.top?parseInt(a.css.top,10):0)+') + "px"';c.setExpression("top",e)}})}g&&(a.theme?c.find(".ui-widget-content").append(g):c.append(g),(g.jquery||g.nodeType)&&b(g).show());(s||a.forceIframe)&&a.showOverlay&&k.show();if(a.fadeIn)e=a.onBlock?a.onBlock:t,k=a.showOverlay&&!g?e:t,e=g?e:t,a.showOverlay&&j._fadeIn(a.fadeIn,k),g&&c._fadeIn(a.fadeIn,
e);else if(a.showOverlay&&j.show(),g&&c.show(),a.onBlock)a.onBlock();x(1,d,a);f?(l=c[0],m=b(":input:enabled:visible",l),a.focusInput&&setTimeout(y,20)):(e=c[0],g=a.centerX,j=a.centerY,h=e.parentNode,c=e.style,k=(h.offsetWidth-e.offsetWidth)/2-(parseInt(b.css(h,"borderLeftWidth"),10)||0),e=(h.offsetHeight-e.offsetHeight)/2-(parseInt(b.css(h,"borderTopWidth"),10)||0),g&&(c.left=0<k?k+"px":"0"),j&&(c.top=0<e?e+"px":"0"));a.timeout&&(g=setTimeout(function(){f?b.unblockUI(a):b(d).unblock(a)},a.timeout),
b(d).data("blockUI.timeout",g))}}function r(d,a){var e,h=d==window,f=b(d),g=f.data("blockUI.history"),j=f.data("blockUI.timeout");j&&(clearTimeout(j),f.removeData("blockUI.timeout"));a=b.extend({},b.blockUI.defaults,a||{});x(0,d,a);null===a.onUnblock&&(a.onUnblock=f.data("blockUI.onUnblock"),f.removeData("blockUI.onUnblock"));var c;c=h?b("body").children().filter(".blockUI").add("body > .blockUI"):f.find(">.blockUI");a.cursorReset&&(1<c.length&&(c[1].style.cursor=a.cursorReset),2<c.length&&(c[2].style.cursor=
a.cursorReset));h&&(l=m=null);a.fadeOut?(e=c.length,c.fadeOut(a.fadeOut,function(){0===--e&&q(c,g,a,d)})):q(c,g,a,d)}function q(d,a,e,h){var f=b(h);d.each(function(){this.parentNode&&this.parentNode.removeChild(this)});a&&a.el&&(a.el.style.display=a.display,a.el.style.position=a.position,a.parent&&a.parent.appendChild(a.el),f.removeData("blockUI.history"));f.data("blockUI.static")&&f.css("position","static");if("function"==typeof e.onUnblock)e.onUnblock(h,e);d=b(document.body);a=d.width();e=d[0].style.width;
d.width(a-1).width(a);d[0].style.width=e}function x(d,a,e){var h=a==window;a=b(a);if(d||!(h&&!l||!h&&!a.data("blockUI.isBlocked")))a.data("blockUI.isBlocked",d),h&&(e.bindEvents&&(!d||e.showOverlay))&&(d?b(document).bind("mousedown mouseup keydown keypress keyup touchstart touchend touchmove",e,p):b(document).unbind("mousedown mouseup keydown keypress keyup touchstart touchend touchmove",p))}function p(d){if(d.keyCode&&9==d.keyCode&&l&&d.data.constrainTabKey){var a=m,e=d.shiftKey&&d.target===a[0];
if(!d.shiftKey&&d.target===a[a.length-1]||e)return setTimeout(function(){y(e)},10),!1}a=d.data;d=b(d.target);if(d.hasClass("blockOverlay")&&a.onOverlayClick)a.onOverlayClick();return 0<d.parents("div."+a.blockMsgClass).length?!0:0===d.parents().children().filter("div.blockUI").length}function y(b){m&&(b=m[!0===b?m.length-1:0])&&b.focus()}b.fn._fadeIn=b.fn.fadeIn;var t=b.noop||function(){},s=/MSIE/.test(navigator.userAgent),u=/MSIE 6.0/.test(navigator.userAgent)&&!/MSIE 8.0/.test(navigator.userAgent),
z=b.isFunction(document.createElement("div").style.setExpression);b.blockUI=function(b){n(window,b)};b.unblockUI=function(b){r(window,b)};b.growlUI=function(d,a,e,h){var f=b('<div class="growlUI"></div>');d&&f.append("<h1>"+d+"</h1>");a&&f.append("<h2>"+a+"</h2>");void 0===e&&(e=3E3);b.blockUI({message:f,fadeIn:700,fadeOut:1E3,centerY:!1,timeout:e,showOverlay:!1,onUnblock:h,css:b.blockUI.defaults.growlCSS})};b.fn.block=function(d){if(this[0]===window)return b.blockUI(d),this;var a=b.extend({},b.blockUI.defaults,
d||{});this.each(function(){var d=b(this);(!a.ignoreIfBlocked||!d.data("blockUI.isBlocked"))&&d.unblock({fadeOut:0})});return this.each(function(){"static"==b.css(this,"position")&&(this.style.position="relative",b(this).data("blockUI.static",!0));this.style.zoom=1;n(this,d)})};b.fn.unblock=function(d){return this[0]===window?(b.unblockUI(d),this):this.each(function(){r(this,d)})};b.blockUI.version=2.59;b.blockUI.defaults={message:"<h1>Please wait...</h1>",title:null,draggable:!0,theme:!1,css:{padding:0,
margin:0,width:"30%",top:"40%",left:"35%",textAlign:"center",color:"#000",border:"3px solid #aaa",backgroundColor:"#fff",cursor:"wait"},themedCSS:{width:"30%",top:"40%",left:"35%"},overlayCSS:{backgroundColor:"#000",opacity:0.6,cursor:"wait"},cursorReset:"default",growlCSS:{width:"350px",top:"10px",left:"",right:"10px",border:"none",padding:"5px",opacity:0.6,cursor:"default",color:"#fff",backgroundColor:"#000","-webkit-border-radius":"10px","-moz-border-radius":"10px","border-radius":"10px"},iframeSrc:/^https/i.test(window.location.href||
"")?"javascript:false":"about:blank",forceIframe:!1,baseZ:1E3,centerX:!0,centerY:!0,allowBodyStretch:!0,bindEvents:!0,constrainTabKey:!0,fadeIn:200,fadeOut:400,timeout:0,showOverlay:!0,focusInput:!0,onBlock:null,onUnblock:null,onOverlayClick:null,quirksmodeOffsetHack:4,blockMsgClass:"blockMsg",ignoreIfBlocked:!1};var l=null,m=[]}"function"===typeof define&&define.amd&&define.amd.jQuery?define(["jquery"],n):n(jQuery)})();


/*
 * Poshy Tip jQuery plugin v1.1 - jSCAF updated for jQuery 1.9 compatibility (remove of msie support, .live=>.on , .die=>.off)
 */
(function(e){var a=[],d=/^url\(["']?([^"'\)]*)["']?\);?$/i,c=/\.png$/i,b=false;function f(){e.each(a,function(){this.refresh(true)})}e(window).resize(f);e.Poshytip=function(h,g){this.$elm=e(h);this.opts=e.extend({},e.fn.poshytip.defaults,g);this.$tip=e(['<div class="',this.opts.className,'">','<div class="tip-inner tip-bg-image"></div>','<div class="tip-arrow tip-arrow-top tip-arrow-right tip-arrow-bottom tip-arrow-left"></div>',"</div>"].join("")).appendTo(document.body);this.$arrow=this.$tip.find("div.tip-arrow");this.$inner=this.$tip.find("div.tip-inner");this.disabled=false;this.content=null;this.init()};e.Poshytip.prototype={init:function(){a.push(this);var g=this.$elm.attr("title");this.$elm.data("title.poshytip",g!==undefined?g:null).data("poshytip",this);if(this.opts.showOn!="none"){this.$elm.bind({"mouseenter.poshytip":e.proxy(this.mouseenter,this),"mouseleave.poshytip":e.proxy(this.mouseleave,this)});switch(this.opts.showOn){case"hover":if(this.opts.alignTo=="cursor"){this.$elm.bind("mousemove.poshytip",e.proxy(this.mousemove,this))}if(this.opts.allowTipHover){this.$tip.hover(e.proxy(this.clearTimeouts,this),e.proxy(this.mouseleave,this))}break;case"focus":this.$elm.bind({"focus.poshytip":e.proxy(this.show,this),"blur.poshytip":e.proxy(this.hide,this)});break}}},mouseenter:function(g){if(this.disabled){return true}this.$elm.attr("title","");if(this.opts.showOn=="focus"){return true}this.clearTimeouts();this.showTimeout=setTimeout(e.proxy(this.show,this),this.opts.showTimeout)},mouseleave:function(g){if(this.disabled||this.asyncAnimating&&(this.$tip[0]===g.relatedTarget||jQuery.contains(this.$tip[0],g.relatedTarget))){return true}var h=this.$elm.data("title.poshytip");if(h!==null){this.$elm.attr("title",h)}if(this.opts.showOn=="focus"){return true}this.clearTimeouts();this.hideTimeout=setTimeout(e.proxy(this.hide,this),this.opts.hideTimeout)},mousemove:function(g){if(this.disabled){return true}this.eventX=g.pageX;this.eventY=g.pageY;if(this.opts.followCursor&&this.$tip.data("active")){this.calcPos();this.$tip.css({left:this.pos.l,top:this.pos.t});if(this.pos.arrow){this.$arrow[0].className="tip-arrow tip-arrow-"+this.pos.arrow}}},show:function(){if(this.disabled||this.$tip.data("active")){return}this.reset();this.update();this.display();if(this.opts.timeOnScreen){setTimeout(e.proxy(this.hide,this),this.opts.timeOnScreen)}},hide:function(){if(this.disabled||!this.$tip.data("active")){return}this.display(true)},reset:function(){this.$tip.queue([]).detach().css("visibility","hidden").data("active",false);this.$inner.find("*").poshytip("hide");if(this.opts.fade){this.$tip.css("opacity",this.opacity)}this.$arrow[0].className="tip-arrow tip-arrow-top tip-arrow-right tip-arrow-bottom tip-arrow-left";this.asyncAnimating=false},update:function(j,k){if(this.disabled){return}var i=j!==undefined;if(i){if(!k){this.opts.content=j}if(!this.$tip.data("active")){return}}else{j=this.opts.content}var h=this,g=typeof j=="function"?j.call(this.$elm[0],function(l){h.update(l)}):j=="[title]"?this.$elm.data("title.poshytip"):j;if(this.content!==g){this.$inner.empty().append(g);this.content=g}this.refresh(i)},refresh:function(h){if(this.disabled){return}if(h){if(!this.$tip.data("active")){return}var k={left:this.$tip.css("left"),top:this.$tip.css("top")}}this.$tip.css({left:0,top:0}).appendTo(document.body);if(this.opacity===undefined){this.opacity=this.$tip.css("opacity")}var l=this.$tip.css("background-image").match(d),m=this.$arrow.css("background-image").match(d);if(l){var i=c.test(l[1]);if(b&&i){this.$tip.css("background-image","none");this.$inner.css({margin:0,border:0,padding:0});l=i=false}else{this.$tip.prepend('<table border="0" cellpadding="0" cellspacing="0"><tr><td class="tip-top tip-bg-image" colspan="2"><span></span></td><td class="tip-right tip-bg-image" rowspan="2"><span></span></td></tr><tr><td class="tip-left tip-bg-image" rowspan="2"><span></span></td><td></td></tr><tr><td class="tip-bottom tip-bg-image" colspan="2"><span></span></td></tr></table>').css({border:0,padding:0,"background-image":"none","background-color":"transparent"}).find(".tip-bg-image").css("background-image",'url("'+l[1]+'")').end().find("td").eq(3).append(this.$inner)}if(i&&!e.support.opacity){this.opts.fade=false}}if(m&&!e.support.opacity){if(b&&c.test(m[1])){m=false;this.$arrow.css("background-image","none")}this.opts.fade=false}var o=this.$tip.find("table");if(b){this.$tip[0].style.width="";o.width("auto").find("td").eq(3).width("auto");var n=this.$tip.width(),j=parseInt(this.$tip.css("min-width")),g=parseInt(this.$tip.css("max-width"));if(!isNaN(j)&&n<j){n=j}else{if(!isNaN(g)&&n>g){n=g}}this.$tip.add(o).width(n).eq(0).find("td").eq(3).width("100%")}else{if(o[0]){o.width("auto").find("td").eq(3).width("auto").end().end().width(document.defaultView&&document.defaultView.getComputedStyle&&parseFloat(document.defaultView.getComputedStyle(this.$tip[0],null).width)||this.$tip.width()).find("td").eq(3).width("100%")}}this.tipOuterW=this.$tip.outerWidth();this.tipOuterH=this.$tip.outerHeight();this.calcPos();if(m&&this.pos.arrow){this.$arrow[0].className="tip-arrow tip-arrow-"+this.pos.arrow;this.$arrow.css("visibility","inherit")}if(h){this.asyncAnimating=true;var p=this;this.$tip.css(k).animate({left:this.pos.l,top:this.pos.t},200,function(){p.asyncAnimating=false})}else{this.$tip.css({left:this.pos.l,top:this.pos.t})}},display:function(h){var i=this.$tip.data("active");if(i&&!h||!i&&h){return}this.$tip.stop();if((this.opts.slide&&this.pos.arrow||this.opts.fade)&&(h&&this.opts.hideAniDuration||!h&&this.opts.showAniDuration)){var m={},l={};if(this.opts.slide&&this.pos.arrow){var k,g;if(this.pos.arrow=="bottom"||this.pos.arrow=="top"){k="top";g="bottom"}else{k="left";g="right"}var j=parseInt(this.$tip.css(k));m[k]=j+(h?0:(this.pos.arrow==g?-this.opts.slideOffset:this.opts.slideOffset));l[k]=j+(h?(this.pos.arrow==g?this.opts.slideOffset:-this.opts.slideOffset):0)+"px"}if(this.opts.fade){m.opacity=h?this.$tip.css("opacity"):0;l.opacity=h?0:this.opacity}this.$tip.css(m).animate(l,this.opts[h?"hideAniDuration":"showAniDuration"])}h?this.$tip.queue(e.proxy(this.reset,this)):this.$tip.css("visibility","inherit");this.$tip.data("active",!i)},disable:function(){this.reset();this.disabled=true},enable:function(){this.disabled=false},destroy:function(){this.reset();this.$tip.remove();delete this.$tip;this.content=null;this.$elm.unbind(".poshytip").removeData("title.poshytip").removeData("poshytip");a.splice(e.inArray(this,a),1)},clearTimeouts:function(){if(this.showTimeout){clearTimeout(this.showTimeout);this.showTimeout=0}if(this.hideTimeout){clearTimeout(this.hideTimeout);this.hideTimeout=0}},calcPos:function(){var n={l:0,t:0,arrow:""},h=e(window),k={l:h.scrollLeft(),t:h.scrollTop(),w:h.width(),h:h.height()},p,j,m,i,q,g;if(this.opts.alignTo=="cursor"){p=j=m=this.eventX;i=q=g=this.eventY}else{var o=this.$elm.offset(),l={l:o.left,t:o.top,w:this.$elm.outerWidth(),h:this.$elm.outerHeight()};p=l.l+(this.opts.alignX!="inner-right"?0:l.w);j=p+Math.floor(l.w/2);m=p+(this.opts.alignX!="inner-left"?l.w:0);i=l.t+(this.opts.alignY!="inner-bottom"?0:l.h);q=i+Math.floor(l.h/2);g=i+(this.opts.alignY!="inner-top"?l.h:0)}switch(this.opts.alignX){case"right":case"inner-left":n.l=m+this.opts.offsetX;if(n.l+this.tipOuterW>k.l+k.w){n.l=k.l+k.w-this.tipOuterW}if(this.opts.alignX=="right"||this.opts.alignY=="center"){n.arrow="left"}break;case"center":n.l=j-Math.floor(this.tipOuterW/2);if(n.l+this.tipOuterW>k.l+k.w){n.l=k.l+k.w-this.tipOuterW}else{if(n.l<k.l){n.l=k.l}}break;default:n.l=p-this.tipOuterW-this.opts.offsetX;if(n.l<k.l){n.l=k.l}if(this.opts.alignX=="left"||this.opts.alignY=="center"){n.arrow="right"}}switch(this.opts.alignY){case"bottom":case"inner-top":n.t=g+this.opts.offsetY;if(!n.arrow||this.opts.alignTo=="cursor"){n.arrow="top"}if(n.t+this.tipOuterH>k.t+k.h){n.t=i-this.tipOuterH-this.opts.offsetY;if(n.arrow=="top"){n.arrow="bottom"}}break;case"center":n.t=q-Math.floor(this.tipOuterH/2);if(n.t+this.tipOuterH>k.t+k.h){n.t=k.t+k.h-this.tipOuterH}else{if(n.t<k.t){n.t=k.t}}break;default:n.t=i-this.tipOuterH-this.opts.offsetY;if(!n.arrow||this.opts.alignTo=="cursor"){n.arrow="bottom"}if(n.t<k.t){n.t=g+this.opts.offsetY;if(n.arrow=="bottom"){n.arrow="top"}}}this.pos=n}};e.fn.poshytip=function(h){if(typeof h=="string"){var g=arguments,k=h;Array.prototype.shift.call(g);if(k=="destroy"){this.off("mouseenter.poshytip").off("focus.poshytip")}return this.each(function(){var l=e(this).data("poshytip");if(l&&l[k]){l[k].apply(l,g)}})}var i=e.extend({},e.fn.poshytip.defaults,h);if(!e("#poshytip-css-"+i.className)[0]){e(['<style id="poshytip-css-',i.className,'" type="text/css">',"div.",i.className,"{visibility:hidden;position:absolute;top:0;left:0;}","div.",i.className," table, div.",i.className," td{margin:0;font-family:inherit;font-size:inherit;font-weight:inherit;font-style:inherit;font-variant:inherit;}","div.",i.className," td.tip-bg-image span{display:block;font:1px/1px sans-serif;height:",i.bgImageFrameSize,"px;width:",i.bgImageFrameSize,"px;overflow:hidden;}","div.",i.className," td.tip-right{background-position:100% 0;}","div.",i.className," td.tip-bottom{background-position:100% 100%;}","div.",i.className," td.tip-left{background-position:0 100%;}","div.",i.className," div.tip-inner{background-position:-",i.bgImageFrameSize,"px -",i.bgImageFrameSize,"px;}","div.",i.className," div.tip-arrow{visibility:hidden;position:absolute;overflow:hidden;font:1px/1px sans-serif;}","</style>"].join("")).appendTo("head")}if(i.liveEvents&&i.showOn!="none"){var j=e.extend({},i,{liveEvents:false});switch(i.showOn){case"hover":this.on("mouseenter.poshytip",function(){var l=e(this);if(!l.data("poshytip")){l.poshytip(j).poshytip("mouseenter")}});break;case"focus":this.on("focus.poshytip",function(){var l=e(this);if(!l.data("poshytip")){l.poshytip(j).poshytip("show")}});break}return this}return this.each(function(){new e.Poshytip(this,i)})};e.fn.poshytip.defaults={content:"[title]",className:"tip-yellow",bgImageFrameSize:10,showTimeout:500,hideTimeout:100,timeOnScreen:0,showOn:"hover",liveEvents:false,alignTo:"cursor",alignX:"right",alignY:"top",offsetX:-22,offsetY:18,allowTipHover:true,followCursor:false,fade:true,slide:true,slideOffset:8,showAniDuration:300,hideAniDuration:300}})(jQuery);

// syze v1.1.1 MIT/GPL2 https://github.com/rezitech/syze
(function(a,b){function j(){var f=/^device$/i.test(String(e))?!a.orientation||orientation==180?screen.width:screen.height:/^browser$/i.test(String(e))?b.clientWidth:e instanceof String?Function("return "+e)():parseInt(e,10)||0,h=b.className.replace(/^\s+|(^|\s)(gt|is|lt)[^\s]+|\s+$/g,"").split(/\s+/),i=[],j=-1,k,l=c,m=l.length;l.sort(function(a,b){return a-b});while(++j<m)if(f<l[j])break;f=l[Math.max(Math.min(--j,m-1),0)];j=-1;while(++j<m){i.push((f>l[j]?"gt":f<l[j]?"lt":"is")+(d[l[j]]||l[j]))}b.className=(!h[0]?[]:h).concat(i).join(" ");if(g)g(f)}function i(a){var b;return function(){function e(){a.apply(c,d);b=null}var c=this,d=arguments;if(b)clearTimeout(b);b=setTimeout(e,f)}}function h(b,c){if(a.addEventListener)addEventListener(b,c,false);else attachEvent("on"+b,c)}var c=[],d={},e="browser",f=50,g;a.syze={sizes:function(a){c=[].concat.apply([],arguments);j();return this},names:function(a){if(a instanceof Object){d=a;j()}return this},from:function(a){e=a;j();return this},debounceRate:function(a){f=parseInt(a,10)||0;j();return this},callback:function(a){if(a instanceof Function){g=a;j()}return this}};h("resize",i(j));h("orientationchange",j);j()})(this,document.documentElement);

// moment.js
// version : 2.8.4
// authors : Tim Wood, Iskren Chernev, Moment.js contributors
// license : MIT
// momentjs.com
(function(a){function b(a,b,c){switch(arguments.length){case 2:return null!=a?a:b;case 3:return null!=a?a:null!=b?b:c;default:throw new Error("Implement me")}}function c(a,b){return zb.call(a,b)}function d(){return{empty:!1,unusedTokens:[],unusedInput:[],overflow:-2,charsLeftOver:0,nullInput:!1,invalidMonth:null,invalidFormat:!1,userInvalidated:!1,iso:!1}}function e(a){tb.suppressDeprecationWarnings===!1&&"undefined"!=typeof console&&console.warn&&console.warn("Deprecation warning: "+a)}function f(a,b){var c=!0;return m(function(){return c&&(e(a),c=!1),b.apply(this,arguments)},b)}function g(a,b){qc[a]||(e(b),qc[a]=!0)}function h(a,b){return function(c){return p(a.call(this,c),b)}}function i(a,b){return function(c){return this.localeData().ordinal(a.call(this,c),b)}}function j(){}function k(a,b){b!==!1&&F(a),n(this,a),this._d=new Date(+a._d)}function l(a){var b=y(a),c=b.year||0,d=b.quarter||0,e=b.month||0,f=b.week||0,g=b.day||0,h=b.hour||0,i=b.minute||0,j=b.second||0,k=b.millisecond||0;this._milliseconds=+k+1e3*j+6e4*i+36e5*h,this._days=+g+7*f,this._months=+e+3*d+12*c,this._data={},this._locale=tb.localeData(),this._bubble()}function m(a,b){for(var d in b)c(b,d)&&(a[d]=b[d]);return c(b,"toString")&&(a.toString=b.toString),c(b,"valueOf")&&(a.valueOf=b.valueOf),a}function n(a,b){var c,d,e;if("undefined"!=typeof b._isAMomentObject&&(a._isAMomentObject=b._isAMomentObject),"undefined"!=typeof b._i&&(a._i=b._i),"undefined"!=typeof b._f&&(a._f=b._f),"undefined"!=typeof b._l&&(a._l=b._l),"undefined"!=typeof b._strict&&(a._strict=b._strict),"undefined"!=typeof b._tzm&&(a._tzm=b._tzm),"undefined"!=typeof b._isUTC&&(a._isUTC=b._isUTC),"undefined"!=typeof b._offset&&(a._offset=b._offset),"undefined"!=typeof b._pf&&(a._pf=b._pf),"undefined"!=typeof b._locale&&(a._locale=b._locale),Ib.length>0)for(c in Ib)d=Ib[c],e=b[d],"undefined"!=typeof e&&(a[d]=e);return a}function o(a){return 0>a?Math.ceil(a):Math.floor(a)}function p(a,b,c){for(var d=""+Math.abs(a),e=a>=0;d.length<b;)d="0"+d;return(e?c?"+":"":"-")+d}function q(a,b){var c={milliseconds:0,months:0};return c.months=b.month()-a.month()+12*(b.year()-a.year()),a.clone().add(c.months,"M").isAfter(b)&&--c.months,c.milliseconds=+b-+a.clone().add(c.months,"M"),c}function r(a,b){var c;return b=K(b,a),a.isBefore(b)?c=q(a,b):(c=q(b,a),c.milliseconds=-c.milliseconds,c.months=-c.months),c}function s(a,b){return function(c,d){var e,f;return null===d||isNaN(+d)||(g(b,"moment()."+b+"(period, number) is deprecated. Please use moment()."+b+"(number, period)."),f=c,c=d,d=f),c="string"==typeof c?+c:c,e=tb.duration(c,d),t(this,e,a),this}}function t(a,b,c,d){var e=b._milliseconds,f=b._days,g=b._months;d=null==d?!0:d,e&&a._d.setTime(+a._d+e*c),f&&nb(a,"Date",mb(a,"Date")+f*c),g&&lb(a,mb(a,"Month")+g*c),d&&tb.updateOffset(a,f||g)}function u(a){return"[object Array]"===Object.prototype.toString.call(a)}function v(a){return"[object Date]"===Object.prototype.toString.call(a)||a instanceof Date}function w(a,b,c){var d,e=Math.min(a.length,b.length),f=Math.abs(a.length-b.length),g=0;for(d=0;e>d;d++)(c&&a[d]!==b[d]||!c&&A(a[d])!==A(b[d]))&&g++;return g+f}function x(a){if(a){var b=a.toLowerCase().replace(/(.)s$/,"$1");a=jc[a]||kc[b]||b}return a}function y(a){var b,d,e={};for(d in a)c(a,d)&&(b=x(d),b&&(e[b]=a[d]));return e}function z(b){var c,d;if(0===b.indexOf("week"))c=7,d="day";else{if(0!==b.indexOf("month"))return;c=12,d="month"}tb[b]=function(e,f){var g,h,i=tb._locale[b],j=[];if("number"==typeof e&&(f=e,e=a),h=function(a){var b=tb().utc().set(d,a);return i.call(tb._locale,b,e||"")},null!=f)return h(f);for(g=0;c>g;g++)j.push(h(g));return j}}function A(a){var b=+a,c=0;return 0!==b&&isFinite(b)&&(c=b>=0?Math.floor(b):Math.ceil(b)),c}function B(a,b){return new Date(Date.UTC(a,b+1,0)).getUTCDate()}function C(a,b,c){return hb(tb([a,11,31+b-c]),b,c).week}function D(a){return E(a)?366:365}function E(a){return a%4===0&&a%100!==0||a%400===0}function F(a){var b;a._a&&-2===a._pf.overflow&&(b=a._a[Bb]<0||a._a[Bb]>11?Bb:a._a[Cb]<1||a._a[Cb]>B(a._a[Ab],a._a[Bb])?Cb:a._a[Db]<0||a._a[Db]>24||24===a._a[Db]&&(0!==a._a[Eb]||0!==a._a[Fb]||0!==a._a[Gb])?Db:a._a[Eb]<0||a._a[Eb]>59?Eb:a._a[Fb]<0||a._a[Fb]>59?Fb:a._a[Gb]<0||a._a[Gb]>999?Gb:-1,a._pf._overflowDayOfYear&&(Ab>b||b>Cb)&&(b=Cb),a._pf.overflow=b)}function G(b){return null==b._isValid&&(b._isValid=!isNaN(b._d.getTime())&&b._pf.overflow<0&&!b._pf.empty&&!b._pf.invalidMonth&&!b._pf.nullInput&&!b._pf.invalidFormat&&!b._pf.userInvalidated,b._strict&&(b._isValid=b._isValid&&0===b._pf.charsLeftOver&&0===b._pf.unusedTokens.length&&b._pf.bigHour===a)),b._isValid}function H(a){return a?a.toLowerCase().replace("_","-"):a}function I(a){for(var b,c,d,e,f=0;f<a.length;){for(e=H(a[f]).split("-"),b=e.length,c=H(a[f+1]),c=c?c.split("-"):null;b>0;){if(d=J(e.slice(0,b).join("-")))return d;if(c&&c.length>=b&&w(e,c,!0)>=b-1)break;b--}f++}return null}function J(a){var b=null;if(!Hb[a]&&Jb)try{b=tb.locale(),require("./locale/"+a),tb.locale(b)}catch(c){}return Hb[a]}function K(a,b){var c,d;return b._isUTC?(c=b.clone(),d=(tb.isMoment(a)||v(a)?+a:+tb(a))-+c,c._d.setTime(+c._d+d),tb.updateOffset(c,!1),c):tb(a).local()}function L(a){return a.match(/\[[\s\S]/)?a.replace(/^\[|\]$/g,""):a.replace(/\\/g,"")}function M(a){var b,c,d=a.match(Nb);for(b=0,c=d.length;c>b;b++)d[b]=pc[d[b]]?pc[d[b]]:L(d[b]);return function(e){var f="";for(b=0;c>b;b++)f+=d[b]instanceof Function?d[b].call(e,a):d[b];return f}}function N(a,b){return a.isValid()?(b=O(b,a.localeData()),lc[b]||(lc[b]=M(b)),lc[b](a)):a.localeData().invalidDate()}function O(a,b){function c(a){return b.longDateFormat(a)||a}var d=5;for(Ob.lastIndex=0;d>=0&&Ob.test(a);)a=a.replace(Ob,c),Ob.lastIndex=0,d-=1;return a}function P(a,b){var c,d=b._strict;switch(a){case"Q":return Zb;case"DDDD":return _b;case"YYYY":case"GGGG":case"gggg":return d?ac:Rb;case"Y":case"G":case"g":return cc;case"YYYYYY":case"YYYYY":case"GGGGG":case"ggggg":return d?bc:Sb;case"S":if(d)return Zb;case"SS":if(d)return $b;case"SSS":if(d)return _b;case"DDD":return Qb;case"MMM":case"MMMM":case"dd":case"ddd":case"dddd":return Ub;case"a":case"A":return b._locale._meridiemParse;case"x":return Xb;case"X":return Yb;case"Z":case"ZZ":return Vb;case"T":return Wb;case"SSSS":return Tb;case"MM":case"DD":case"YY":case"GG":case"gg":case"HH":case"hh":case"mm":case"ss":case"ww":case"WW":return d?$b:Pb;case"M":case"D":case"d":case"H":case"h":case"m":case"s":case"w":case"W":case"e":case"E":return Pb;case"Do":return d?b._locale._ordinalParse:b._locale._ordinalParseLenient;default:return c=new RegExp(Y(X(a.replace("\\","")),"i"))}}function Q(a){a=a||"";var b=a.match(Vb)||[],c=b[b.length-1]||[],d=(c+"").match(hc)||["-",0,0],e=+(60*d[1])+A(d[2]);return"+"===d[0]?-e:e}function R(a,b,c){var d,e=c._a;switch(a){case"Q":null!=b&&(e[Bb]=3*(A(b)-1));break;case"M":case"MM":null!=b&&(e[Bb]=A(b)-1);break;case"MMM":case"MMMM":d=c._locale.monthsParse(b,a,c._strict),null!=d?e[Bb]=d:c._pf.invalidMonth=b;break;case"D":case"DD":null!=b&&(e[Cb]=A(b));break;case"Do":null!=b&&(e[Cb]=A(parseInt(b.match(/\d{1,2}/)[0],10)));break;case"DDD":case"DDDD":null!=b&&(c._dayOfYear=A(b));break;case"YY":e[Ab]=tb.parseTwoDigitYear(b);break;case"YYYY":case"YYYYY":case"YYYYYY":e[Ab]=A(b);break;case"a":case"A":c._isPm=c._locale.isPM(b);break;case"h":case"hh":c._pf.bigHour=!0;case"H":case"HH":e[Db]=A(b);break;case"m":case"mm":e[Eb]=A(b);break;case"s":case"ss":e[Fb]=A(b);break;case"S":case"SS":case"SSS":case"SSSS":e[Gb]=A(1e3*("0."+b));break;case"x":c._d=new Date(A(b));break;case"X":c._d=new Date(1e3*parseFloat(b));break;case"Z":case"ZZ":c._useUTC=!0,c._tzm=Q(b);break;case"dd":case"ddd":case"dddd":d=c._locale.weekdaysParse(b),null!=d?(c._w=c._w||{},c._w.d=d):c._pf.invalidWeekday=b;break;case"w":case"ww":case"W":case"WW":case"d":case"e":case"E":a=a.substr(0,1);case"gggg":case"GGGG":case"GGGGG":a=a.substr(0,2),b&&(c._w=c._w||{},c._w[a]=A(b));break;case"gg":case"GG":c._w=c._w||{},c._w[a]=tb.parseTwoDigitYear(b)}}function S(a){var c,d,e,f,g,h,i;c=a._w,null!=c.GG||null!=c.W||null!=c.E?(g=1,h=4,d=b(c.GG,a._a[Ab],hb(tb(),1,4).year),e=b(c.W,1),f=b(c.E,1)):(g=a._locale._week.dow,h=a._locale._week.doy,d=b(c.gg,a._a[Ab],hb(tb(),g,h).year),e=b(c.w,1),null!=c.d?(f=c.d,g>f&&++e):f=null!=c.e?c.e+g:g),i=ib(d,e,f,h,g),a._a[Ab]=i.year,a._dayOfYear=i.dayOfYear}function T(a){var c,d,e,f,g=[];if(!a._d){for(e=V(a),a._w&&null==a._a[Cb]&&null==a._a[Bb]&&S(a),a._dayOfYear&&(f=b(a._a[Ab],e[Ab]),a._dayOfYear>D(f)&&(a._pf._overflowDayOfYear=!0),d=db(f,0,a._dayOfYear),a._a[Bb]=d.getUTCMonth(),a._a[Cb]=d.getUTCDate()),c=0;3>c&&null==a._a[c];++c)a._a[c]=g[c]=e[c];for(;7>c;c++)a._a[c]=g[c]=null==a._a[c]?2===c?1:0:a._a[c];24===a._a[Db]&&0===a._a[Eb]&&0===a._a[Fb]&&0===a._a[Gb]&&(a._nextDay=!0,a._a[Db]=0),a._d=(a._useUTC?db:cb).apply(null,g),null!=a._tzm&&a._d.setUTCMinutes(a._d.getUTCMinutes()+a._tzm),a._nextDay&&(a._a[Db]=24)}}function U(a){var b;a._d||(b=y(a._i),a._a=[b.year,b.month,b.day||b.date,b.hour,b.minute,b.second,b.millisecond],T(a))}function V(a){var b=new Date;return a._useUTC?[b.getUTCFullYear(),b.getUTCMonth(),b.getUTCDate()]:[b.getFullYear(),b.getMonth(),b.getDate()]}function W(b){if(b._f===tb.ISO_8601)return void $(b);b._a=[],b._pf.empty=!0;var c,d,e,f,g,h=""+b._i,i=h.length,j=0;for(e=O(b._f,b._locale).match(Nb)||[],c=0;c<e.length;c++)f=e[c],d=(h.match(P(f,b))||[])[0],d&&(g=h.substr(0,h.indexOf(d)),g.length>0&&b._pf.unusedInput.push(g),h=h.slice(h.indexOf(d)+d.length),j+=d.length),pc[f]?(d?b._pf.empty=!1:b._pf.unusedTokens.push(f),R(f,d,b)):b._strict&&!d&&b._pf.unusedTokens.push(f);b._pf.charsLeftOver=i-j,h.length>0&&b._pf.unusedInput.push(h),b._pf.bigHour===!0&&b._a[Db]<=12&&(b._pf.bigHour=a),b._isPm&&b._a[Db]<12&&(b._a[Db]+=12),b._isPm===!1&&12===b._a[Db]&&(b._a[Db]=0),T(b),F(b)}function X(a){return a.replace(/\\(\[)|\\(\])|\[([^\]\[]*)\]|\\(.)/g,function(a,b,c,d,e){return b||c||d||e})}function Y(a){return a.replace(/[-\/\\^$*+?.()|[\]{}]/g,"\\$&")}function Z(a){var b,c,e,f,g;if(0===a._f.length)return a._pf.invalidFormat=!0,void(a._d=new Date(0/0));for(f=0;f<a._f.length;f++)g=0,b=n({},a),null!=a._useUTC&&(b._useUTC=a._useUTC),b._pf=d(),b._f=a._f[f],W(b),G(b)&&(g+=b._pf.charsLeftOver,g+=10*b._pf.unusedTokens.length,b._pf.score=g,(null==e||e>g)&&(e=g,c=b));m(a,c||b)}function $(a){var b,c,d=a._i,e=dc.exec(d);if(e){for(a._pf.iso=!0,b=0,c=fc.length;c>b;b++)if(fc[b][1].exec(d)){a._f=fc[b][0]+(e[6]||" ");break}for(b=0,c=gc.length;c>b;b++)if(gc[b][1].exec(d)){a._f+=gc[b][0];break}d.match(Vb)&&(a._f+="Z"),W(a)}else a._isValid=!1}function _(a){$(a),a._isValid===!1&&(delete a._isValid,tb.createFromInputFallback(a))}function ab(a,b){var c,d=[];for(c=0;c<a.length;++c)d.push(b(a[c],c));return d}function bb(b){var c,d=b._i;d===a?b._d=new Date:v(d)?b._d=new Date(+d):null!==(c=Kb.exec(d))?b._d=new Date(+c[1]):"string"==typeof d?_(b):u(d)?(b._a=ab(d.slice(0),function(a){return parseInt(a,10)}),T(b)):"object"==typeof d?U(b):"number"==typeof d?b._d=new Date(d):tb.createFromInputFallback(b)}function cb(a,b,c,d,e,f,g){var h=new Date(a,b,c,d,e,f,g);return 1970>a&&h.setFullYear(a),h}function db(a){var b=new Date(Date.UTC.apply(null,arguments));return 1970>a&&b.setUTCFullYear(a),b}function eb(a,b){if("string"==typeof a)if(isNaN(a)){if(a=b.weekdaysParse(a),"number"!=typeof a)return null}else a=parseInt(a,10);return a}function fb(a,b,c,d,e){return e.relativeTime(b||1,!!c,a,d)}function gb(a,b,c){var d=tb.duration(a).abs(),e=yb(d.as("s")),f=yb(d.as("m")),g=yb(d.as("h")),h=yb(d.as("d")),i=yb(d.as("M")),j=yb(d.as("y")),k=e<mc.s&&["s",e]||1===f&&["m"]||f<mc.m&&["mm",f]||1===g&&["h"]||g<mc.h&&["hh",g]||1===h&&["d"]||h<mc.d&&["dd",h]||1===i&&["M"]||i<mc.M&&["MM",i]||1===j&&["y"]||["yy",j];return k[2]=b,k[3]=+a>0,k[4]=c,fb.apply({},k)}function hb(a,b,c){var d,e=c-b,f=c-a.day();return f>e&&(f-=7),e-7>f&&(f+=7),d=tb(a).add(f,"d"),{week:Math.ceil(d.dayOfYear()/7),year:d.year()}}function ib(a,b,c,d,e){var f,g,h=db(a,0,1).getUTCDay();return h=0===h?7:h,c=null!=c?c:e,f=e-h+(h>d?7:0)-(e>h?7:0),g=7*(b-1)+(c-e)+f+1,{year:g>0?a:a-1,dayOfYear:g>0?g:D(a-1)+g}}function jb(b){var c,d=b._i,e=b._f;return b._locale=b._locale||tb.localeData(b._l),null===d||e===a&&""===d?tb.invalid({nullInput:!0}):("string"==typeof d&&(b._i=d=b._locale.preparse(d)),tb.isMoment(d)?new k(d,!0):(e?u(e)?Z(b):W(b):bb(b),c=new k(b),c._nextDay&&(c.add(1,"d"),c._nextDay=a),c))}function kb(a,b){var c,d;if(1===b.length&&u(b[0])&&(b=b[0]),!b.length)return tb();for(c=b[0],d=1;d<b.length;++d)b[d][a](c)&&(c=b[d]);return c}function lb(a,b){var c;return"string"==typeof b&&(b=a.localeData().monthsParse(b),"number"!=typeof b)?a:(c=Math.min(a.date(),B(a.year(),b)),a._d["set"+(a._isUTC?"UTC":"")+"Month"](b,c),a)}function mb(a,b){return a._d["get"+(a._isUTC?"UTC":"")+b]()}function nb(a,b,c){return"Month"===b?lb(a,c):a._d["set"+(a._isUTC?"UTC":"")+b](c)}function ob(a,b){return function(c){return null!=c?(nb(this,a,c),tb.updateOffset(this,b),this):mb(this,a)}}function pb(a){return 400*a/146097}function qb(a){return 146097*a/400}function rb(a){tb.duration.fn[a]=function(){return this._data[a]}}function sb(a){"undefined"==typeof ender&&(ub=xb.moment,xb.moment=a?f("Accessing Moment through the global scope is deprecated, and will be removed in an upcoming release.",tb):tb)}for(var tb,ub,vb,wb="2.8.4",xb="undefined"!=typeof global?global:this,yb=Math.round,zb=Object.prototype.hasOwnProperty,Ab=0,Bb=1,Cb=2,Db=3,Eb=4,Fb=5,Gb=6,Hb={},Ib=[],Jb="undefined"!=typeof module&&module&&module.exports,Kb=/^\/?Date\((\-?\d+)/i,Lb=/(\-)?(?:(\d*)\.)?(\d+)\:(\d+)(?:\:(\d+)\.?(\d{3})?)?/,Mb=/^(-)?P(?:(?:([0-9,.]*)Y)?(?:([0-9,.]*)M)?(?:([0-9,.]*)D)?(?:T(?:([0-9,.]*)H)?(?:([0-9,.]*)M)?(?:([0-9,.]*)S)?)?|([0-9,.]*)W)$/,Nb=/(\[[^\[]*\])|(\\)?(Mo|MM?M?M?|Do|DDDo|DD?D?D?|ddd?d?|do?|w[o|w]?|W[o|W]?|Q|YYYYYY|YYYYY|YYYY|YY|gg(ggg?)?|GG(GGG?)?|e|E|a|A|hh?|HH?|mm?|ss?|S{1,4}|x|X|zz?|ZZ?|.)/g,Ob=/(\[[^\[]*\])|(\\)?(LTS|LT|LL?L?L?|l{1,4})/g,Pb=/\d\d?/,Qb=/\d{1,3}/,Rb=/\d{1,4}/,Sb=/[+\-]?\d{1,6}/,Tb=/\d+/,Ub=/[0-9]*['a-z\u00A0-\u05FF\u0700-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+|[\u0600-\u06FF\/]+(\s*?[\u0600-\u06FF]+){1,2}/i,Vb=/Z|[\+\-]\d\d:?\d\d/gi,Wb=/T/i,Xb=/[\+\-]?\d+/,Yb=/[\+\-]?\d+(\.\d{1,3})?/,Zb=/\d/,$b=/\d\d/,_b=/\d{3}/,ac=/\d{4}/,bc=/[+-]?\d{6}/,cc=/[+-]?\d+/,dc=/^\s*(?:[+-]\d{6}|\d{4})-(?:(\d\d-\d\d)|(W\d\d$)|(W\d\d-\d)|(\d\d\d))((T| )(\d\d(:\d\d(:\d\d(\.\d+)?)?)?)?([\+\-]\d\d(?::?\d\d)?|\s*Z)?)?$/,ec="YYYY-MM-DDTHH:mm:ssZ",fc=[["YYYYYY-MM-DD",/[+-]\d{6}-\d{2}-\d{2}/],["YYYY-MM-DD",/\d{4}-\d{2}-\d{2}/],["GGGG-[W]WW-E",/\d{4}-W\d{2}-\d/],["GGGG-[W]WW",/\d{4}-W\d{2}/],["YYYY-DDD",/\d{4}-\d{3}/]],gc=[["HH:mm:ss.SSSS",/(T| )\d\d:\d\d:\d\d\.\d+/],["HH:mm:ss",/(T| )\d\d:\d\d:\d\d/],["HH:mm",/(T| )\d\d:\d\d/],["HH",/(T| )\d\d/]],hc=/([\+\-]|\d\d)/gi,ic=("Date|Hours|Minutes|Seconds|Milliseconds".split("|"),{Milliseconds:1,Seconds:1e3,Minutes:6e4,Hours:36e5,Days:864e5,Months:2592e6,Years:31536e6}),jc={ms:"millisecond",s:"second",m:"minute",h:"hour",d:"day",D:"date",w:"week",W:"isoWeek",M:"month",Q:"quarter",y:"year",DDD:"dayOfYear",e:"weekday",E:"isoWeekday",gg:"weekYear",GG:"isoWeekYear"},kc={dayofyear:"dayOfYear",isoweekday:"isoWeekday",isoweek:"isoWeek",weekyear:"weekYear",isoweekyear:"isoWeekYear"},lc={},mc={s:45,m:45,h:22,d:26,M:11},nc="DDD w W M D d".split(" "),oc="M D H h m s w W".split(" "),pc={M:function(){return this.month()+1},MMM:function(a){return this.localeData().monthsShort(this,a)},MMMM:function(a){return this.localeData().months(this,a)},D:function(){return this.date()},DDD:function(){return this.dayOfYear()},d:function(){return this.day()},dd:function(a){return this.localeData().weekdaysMin(this,a)},ddd:function(a){return this.localeData().weekdaysShort(this,a)},dddd:function(a){return this.localeData().weekdays(this,a)},w:function(){return this.week()},W:function(){return this.isoWeek()},YY:function(){return p(this.year()%100,2)},YYYY:function(){return p(this.year(),4)},YYYYY:function(){return p(this.year(),5)},YYYYYY:function(){var a=this.year(),b=a>=0?"+":"-";return b+p(Math.abs(a),6)},gg:function(){return p(this.weekYear()%100,2)},gggg:function(){return p(this.weekYear(),4)},ggggg:function(){return p(this.weekYear(),5)},GG:function(){return p(this.isoWeekYear()%100,2)},GGGG:function(){return p(this.isoWeekYear(),4)},GGGGG:function(){return p(this.isoWeekYear(),5)},e:function(){return this.weekday()},E:function(){return this.isoWeekday()},a:function(){return this.localeData().meridiem(this.hours(),this.minutes(),!0)},A:function(){return this.localeData().meridiem(this.hours(),this.minutes(),!1)},H:function(){return this.hours()},h:function(){return this.hours()%12||12},m:function(){return this.minutes()},s:function(){return this.seconds()},S:function(){return A(this.milliseconds()/100)},SS:function(){return p(A(this.milliseconds()/10),2)},SSS:function(){return p(this.milliseconds(),3)},SSSS:function(){return p(this.milliseconds(),3)},Z:function(){var a=-this.zone(),b="+";return 0>a&&(a=-a,b="-"),b+p(A(a/60),2)+":"+p(A(a)%60,2)},ZZ:function(){var a=-this.zone(),b="+";return 0>a&&(a=-a,b="-"),b+p(A(a/60),2)+p(A(a)%60,2)},z:function(){return this.zoneAbbr()},zz:function(){return this.zoneName()},x:function(){return this.valueOf()},X:function(){return this.unix()},Q:function(){return this.quarter()}},qc={},rc=["months","monthsShort","weekdays","weekdaysShort","weekdaysMin"];nc.length;)vb=nc.pop(),pc[vb+"o"]=i(pc[vb],vb);for(;oc.length;)vb=oc.pop(),pc[vb+vb]=h(pc[vb],2);pc.DDDD=h(pc.DDD,3),m(j.prototype,{set:function(a){var b,c;for(c in a)b=a[c],"function"==typeof b?this[c]=b:this["_"+c]=b;this._ordinalParseLenient=new RegExp(this._ordinalParse.source+"|"+/\d{1,2}/.source)},_months:"January_February_March_April_May_June_July_August_September_October_November_December".split("_"),months:function(a){return this._months[a.month()]},_monthsShort:"Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_"),monthsShort:function(a){return this._monthsShort[a.month()]},monthsParse:function(a,b,c){var d,e,f;for(this._monthsParse||(this._monthsParse=[],this._longMonthsParse=[],this._shortMonthsParse=[]),d=0;12>d;d++){if(e=tb.utc([2e3,d]),c&&!this._longMonthsParse[d]&&(this._longMonthsParse[d]=new RegExp("^"+this.months(e,"").replace(".","")+"$","i"),this._shortMonthsParse[d]=new RegExp("^"+this.monthsShort(e,"").replace(".","")+"$","i")),c||this._monthsParse[d]||(f="^"+this.months(e,"")+"|^"+this.monthsShort(e,""),this._monthsParse[d]=new RegExp(f.replace(".",""),"i")),c&&"MMMM"===b&&this._longMonthsParse[d].test(a))return d;if(c&&"MMM"===b&&this._shortMonthsParse[d].test(a))return d;if(!c&&this._monthsParse[d].test(a))return d}},_weekdays:"Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"),weekdays:function(a){return this._weekdays[a.day()]},_weekdaysShort:"Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_"),weekdaysShort:function(a){return this._weekdaysShort[a.day()]},_weekdaysMin:"Su_Mo_Tu_We_Th_Fr_Sa".split("_"),weekdaysMin:function(a){return this._weekdaysMin[a.day()]},weekdaysParse:function(a){var b,c,d;for(this._weekdaysParse||(this._weekdaysParse=[]),b=0;7>b;b++)if(this._weekdaysParse[b]||(c=tb([2e3,1]).day(b),d="^"+this.weekdays(c,"")+"|^"+this.weekdaysShort(c,"")+"|^"+this.weekdaysMin(c,""),this._weekdaysParse[b]=new RegExp(d.replace(".",""),"i")),this._weekdaysParse[b].test(a))return b},_longDateFormat:{LTS:"h:mm:ss A",LT:"h:mm A",L:"MM/DD/YYYY",LL:"MMMM D, YYYY",LLL:"MMMM D, YYYY LT",LLLL:"dddd, MMMM D, YYYY LT"},longDateFormat:function(a){var b=this._longDateFormat[a];return!b&&this._longDateFormat[a.toUpperCase()]&&(b=this._longDateFormat[a.toUpperCase()].replace(/MMMM|MM|DD|dddd/g,function(a){return a.slice(1)}),this._longDateFormat[a]=b),b},isPM:function(a){return"p"===(a+"").toLowerCase().charAt(0)},_meridiemParse:/[ap]\.?m?\.?/i,meridiem:function(a,b,c){return a>11?c?"pm":"PM":c?"am":"AM"},_calendar:{sameDay:"[Today at] LT",nextDay:"[Tomorrow at] LT",nextWeek:"dddd [at] LT",lastDay:"[Yesterday at] LT",lastWeek:"[Last] dddd [at] LT",sameElse:"L"},calendar:function(a,b,c){var d=this._calendar[a];return"function"==typeof d?d.apply(b,[c]):d},_relativeTime:{future:"in %s",past:"%s ago",s:"a few seconds",m:"a minute",mm:"%d minutes",h:"an hour",hh:"%d hours",d:"a day",dd:"%d days",M:"a month",MM:"%d months",y:"a year",yy:"%d years"},relativeTime:function(a,b,c,d){var e=this._relativeTime[c];return"function"==typeof e?e(a,b,c,d):e.replace(/%d/i,a)},pastFuture:function(a,b){var c=this._relativeTime[a>0?"future":"past"];return"function"==typeof c?c(b):c.replace(/%s/i,b)},ordinal:function(a){return this._ordinal.replace("%d",a)},_ordinal:"%d",_ordinalParse:/\d{1,2}/,preparse:function(a){return a},postformat:function(a){return a},week:function(a){return hb(a,this._week.dow,this._week.doy).week},_week:{dow:0,doy:6},_invalidDate:"Invalid date",invalidDate:function(){return this._invalidDate}}),tb=function(b,c,e,f){var g;return"boolean"==typeof e&&(f=e,e=a),g={},g._isAMomentObject=!0,g._i=b,g._f=c,g._l=e,g._strict=f,g._isUTC=!1,g._pf=d(),jb(g)},tb.suppressDeprecationWarnings=!1,tb.createFromInputFallback=f("moment construction falls back to js Date. This is discouraged and will be removed in upcoming major release. Please refer to https://github.com/moment/moment/issues/1407 for more info.",function(a){a._d=new Date(a._i+(a._useUTC?" UTC":""))}),tb.min=function(){var a=[].slice.call(arguments,0);return kb("isBefore",a)},tb.max=function(){var a=[].slice.call(arguments,0);return kb("isAfter",a)},tb.utc=function(b,c,e,f){var g;return"boolean"==typeof e&&(f=e,e=a),g={},g._isAMomentObject=!0,g._useUTC=!0,g._isUTC=!0,g._l=e,g._i=b,g._f=c,g._strict=f,g._pf=d(),jb(g).utc()},tb.unix=function(a){return tb(1e3*a)},tb.duration=function(a,b){var d,e,f,g,h=a,i=null;return tb.isDuration(a)?h={ms:a._milliseconds,d:a._days,M:a._months}:"number"==typeof a?(h={},b?h[b]=a:h.milliseconds=a):(i=Lb.exec(a))?(d="-"===i[1]?-1:1,h={y:0,d:A(i[Cb])*d,h:A(i[Db])*d,m:A(i[Eb])*d,s:A(i[Fb])*d,ms:A(i[Gb])*d}):(i=Mb.exec(a))?(d="-"===i[1]?-1:1,f=function(a){var b=a&&parseFloat(a.replace(",","."));return(isNaN(b)?0:b)*d},h={y:f(i[2]),M:f(i[3]),d:f(i[4]),h:f(i[5]),m:f(i[6]),s:f(i[7]),w:f(i[8])}):"object"==typeof h&&("from"in h||"to"in h)&&(g=r(tb(h.from),tb(h.to)),h={},h.ms=g.milliseconds,h.M=g.months),e=new l(h),tb.isDuration(a)&&c(a,"_locale")&&(e._locale=a._locale),e},tb.version=wb,tb.defaultFormat=ec,tb.ISO_8601=function(){},tb.momentProperties=Ib,tb.updateOffset=function(){},tb.relativeTimeThreshold=function(b,c){return mc[b]===a?!1:c===a?mc[b]:(mc[b]=c,!0)},tb.lang=f("moment.lang is deprecated. Use moment.locale instead.",function(a,b){return tb.locale(a,b)}),tb.locale=function(a,b){var c;return a&&(c="undefined"!=typeof b?tb.defineLocale(a,b):tb.localeData(a),c&&(tb.duration._locale=tb._locale=c)),tb._locale._abbr},tb.defineLocale=function(a,b){return null!==b?(b.abbr=a,Hb[a]||(Hb[a]=new j),Hb[a].set(b),tb.locale(a),Hb[a]):(delete Hb[a],null)},tb.langData=f("moment.langData is deprecated. Use moment.localeData instead.",function(a){return tb.localeData(a)}),tb.localeData=function(a){var b;if(a&&a._locale&&a._locale._abbr&&(a=a._locale._abbr),!a)return tb._locale;if(!u(a)){if(b=J(a))return b;a=[a]}return I(a)},tb.isMoment=function(a){return a instanceof k||null!=a&&c(a,"_isAMomentObject")},tb.isDuration=function(a){return a instanceof l};for(vb=rc.length-1;vb>=0;--vb)z(rc[vb]);tb.normalizeUnits=function(a){return x(a)},tb.invalid=function(a){var b=tb.utc(0/0);return null!=a?m(b._pf,a):b._pf.userInvalidated=!0,b},tb.parseZone=function(){return tb.apply(null,arguments).parseZone()},tb.parseTwoDigitYear=function(a){return A(a)+(A(a)>68?1900:2e3)},m(tb.fn=k.prototype,{clone:function(){return tb(this)},valueOf:function(){return+this._d+6e4*(this._offset||0)},unix:function(){return Math.floor(+this/1e3)},toString:function(){return this.clone().locale("en").format("ddd MMM DD YYYY HH:mm:ss [GMT]ZZ")},toDate:function(){return this._offset?new Date(+this):this._d},toISOString:function(){var a=tb(this).utc();return 0<a.year()&&a.year()<=9999?"function"==typeof Date.prototype.toISOString?this.toDate().toISOString():N(a,"YYYY-MM-DD[T]HH:mm:ss.SSS[Z]"):N(a,"YYYYYY-MM-DD[T]HH:mm:ss.SSS[Z]")},toArray:function(){var a=this;return[a.year(),a.month(),a.date(),a.hours(),a.minutes(),a.seconds(),a.milliseconds()]},isValid:function(){return G(this)},isDSTShifted:function(){return this._a?this.isValid()&&w(this._a,(this._isUTC?tb.utc(this._a):tb(this._a)).toArray())>0:!1},parsingFlags:function(){return m({},this._pf)},invalidAt:function(){return this._pf.overflow},utc:function(a){return this.zone(0,a)},local:function(a){return this._isUTC&&(this.zone(0,a),this._isUTC=!1,a&&this.add(this._dateTzOffset(),"m")),this},format:function(a){var b=N(this,a||tb.defaultFormat);return this.localeData().postformat(b)},add:s(1,"add"),subtract:s(-1,"subtract"),diff:function(a,b,c){var d,e,f,g=K(a,this),h=6e4*(this.zone()-g.zone());return b=x(b),"year"===b||"month"===b?(d=432e5*(this.daysInMonth()+g.daysInMonth()),e=12*(this.year()-g.year())+(this.month()-g.month()),f=this-tb(this).startOf("month")-(g-tb(g).startOf("month")),f-=6e4*(this.zone()-tb(this).startOf("month").zone()-(g.zone()-tb(g).startOf("month").zone())),e+=f/d,"year"===b&&(e/=12)):(d=this-g,e="second"===b?d/1e3:"minute"===b?d/6e4:"hour"===b?d/36e5:"day"===b?(d-h)/864e5:"week"===b?(d-h)/6048e5:d),c?e:o(e)},from:function(a,b){return tb.duration({to:this,from:a}).locale(this.locale()).humanize(!b)},fromNow:function(a){return this.from(tb(),a)},calendar:function(a){var b=a||tb(),c=K(b,this).startOf("day"),d=this.diff(c,"days",!0),e=-6>d?"sameElse":-1>d?"lastWeek":0>d?"lastDay":1>d?"sameDay":2>d?"nextDay":7>d?"nextWeek":"sameElse";return this.format(this.localeData().calendar(e,this,tb(b)))},isLeapYear:function(){return E(this.year())},isDST:function(){return this.zone()<this.clone().month(0).zone()||this.zone()<this.clone().month(5).zone()},day:function(a){var b=this._isUTC?this._d.getUTCDay():this._d.getDay();return null!=a?(a=eb(a,this.localeData()),this.add(a-b,"d")):b},month:ob("Month",!0),startOf:function(a){switch(a=x(a)){case"year":this.month(0);case"quarter":case"month":this.date(1);case"week":case"isoWeek":case"day":this.hours(0);case"hour":this.minutes(0);case"minute":this.seconds(0);case"second":this.milliseconds(0)}return"week"===a?this.weekday(0):"isoWeek"===a&&this.isoWeekday(1),"quarter"===a&&this.month(3*Math.floor(this.month()/3)),this},endOf:function(b){return b=x(b),b===a||"millisecond"===b?this:this.startOf(b).add(1,"isoWeek"===b?"week":b).subtract(1,"ms")},isAfter:function(a,b){var c;return b=x("undefined"!=typeof b?b:"millisecond"),"millisecond"===b?(a=tb.isMoment(a)?a:tb(a),+this>+a):(c=tb.isMoment(a)?+a:+tb(a),c<+this.clone().startOf(b))},isBefore:function(a,b){var c;return b=x("undefined"!=typeof b?b:"millisecond"),"millisecond"===b?(a=tb.isMoment(a)?a:tb(a),+a>+this):(c=tb.isMoment(a)?+a:+tb(a),+this.clone().endOf(b)<c)},isSame:function(a,b){var c;return b=x(b||"millisecond"),"millisecond"===b?(a=tb.isMoment(a)?a:tb(a),+this===+a):(c=+tb(a),+this.clone().startOf(b)<=c&&c<=+this.clone().endOf(b))},min:f("moment().min is deprecated, use moment.min instead. https://github.com/moment/moment/issues/1548",function(a){return a=tb.apply(null,arguments),this>a?this:a}),max:f("moment().max is deprecated, use moment.max instead. https://github.com/moment/moment/issues/1548",function(a){return a=tb.apply(null,arguments),a>this?this:a}),zone:function(a,b){var c,d=this._offset||0;return null==a?this._isUTC?d:this._dateTzOffset():("string"==typeof a&&(a=Q(a)),Math.abs(a)<16&&(a=60*a),!this._isUTC&&b&&(c=this._dateTzOffset()),this._offset=a,this._isUTC=!0,null!=c&&this.subtract(c,"m"),d!==a&&(!b||this._changeInProgress?t(this,tb.duration(d-a,"m"),1,!1):this._changeInProgress||(this._changeInProgress=!0,tb.updateOffset(this,!0),this._changeInProgress=null)),this)},zoneAbbr:function(){return this._isUTC?"UTC":""},zoneName:function(){return this._isUTC?"Coordinated Universal Time":""},parseZone:function(){return this._tzm?this.zone(this._tzm):"string"==typeof this._i&&this.zone(this._i),this},hasAlignedHourOffset:function(a){return a=a?tb(a).zone():0,(this.zone()-a)%60===0},daysInMonth:function(){return B(this.year(),this.month())},dayOfYear:function(a){var b=yb((tb(this).startOf("day")-tb(this).startOf("year"))/864e5)+1;return null==a?b:this.add(a-b,"d")},quarter:function(a){return null==a?Math.ceil((this.month()+1)/3):this.month(3*(a-1)+this.month()%3)},weekYear:function(a){var b=hb(this,this.localeData()._week.dow,this.localeData()._week.doy).year;return null==a?b:this.add(a-b,"y")},isoWeekYear:function(a){var b=hb(this,1,4).year;return null==a?b:this.add(a-b,"y")},week:function(a){var b=this.localeData().week(this);return null==a?b:this.add(7*(a-b),"d")},isoWeek:function(a){var b=hb(this,1,4).week;return null==a?b:this.add(7*(a-b),"d")},weekday:function(a){var b=(this.day()+7-this.localeData()._week.dow)%7;return null==a?b:this.add(a-b,"d")},isoWeekday:function(a){return null==a?this.day()||7:this.day(this.day()%7?a:a-7)},isoWeeksInYear:function(){return C(this.year(),1,4)},weeksInYear:function(){var a=this.localeData()._week;return C(this.year(),a.dow,a.doy)},get:function(a){return a=x(a),this[a]()},set:function(a,b){return a=x(a),"function"==typeof this[a]&&this[a](b),this},locale:function(b){var c;return b===a?this._locale._abbr:(c=tb.localeData(b),null!=c&&(this._locale=c),this)},lang:f("moment().lang() is deprecated. Instead, use moment().localeData() to get the language configuration. Use moment().locale() to change languages.",function(b){return b===a?this.localeData():this.locale(b)}),localeData:function(){return this._locale},_dateTzOffset:function(){return 15*Math.round(this._d.getTimezoneOffset()/15)}}),tb.fn.millisecond=tb.fn.milliseconds=ob("Milliseconds",!1),tb.fn.second=tb.fn.seconds=ob("Seconds",!1),tb.fn.minute=tb.fn.minutes=ob("Minutes",!1),tb.fn.hour=tb.fn.hours=ob("Hours",!0),tb.fn.date=ob("Date",!0),tb.fn.dates=f("dates accessor is deprecated. Use date instead.",ob("Date",!0)),tb.fn.year=ob("FullYear",!0),tb.fn.years=f("years accessor is deprecated. Use year instead.",ob("FullYear",!0)),tb.fn.days=tb.fn.day,tb.fn.months=tb.fn.month,tb.fn.weeks=tb.fn.week,tb.fn.isoWeeks=tb.fn.isoWeek,tb.fn.quarters=tb.fn.quarter,tb.fn.toJSON=tb.fn.toISOString,m(tb.duration.fn=l.prototype,{_bubble:function(){var a,b,c,d=this._milliseconds,e=this._days,f=this._months,g=this._data,h=0;g.milliseconds=d%1e3,a=o(d/1e3),g.seconds=a%60,b=o(a/60),g.minutes=b%60,c=o(b/60),g.hours=c%24,e+=o(c/24),h=o(pb(e)),e-=o(qb(h)),f+=o(e/30),e%=30,h+=o(f/12),f%=12,g.days=e,g.months=f,g.years=h},abs:function(){return this._milliseconds=Math.abs(this._milliseconds),this._days=Math.abs(this._days),this._months=Math.abs(this._months),this._data.milliseconds=Math.abs(this._data.milliseconds),this._data.seconds=Math.abs(this._data.seconds),this._data.minutes=Math.abs(this._data.minutes),this._data.hours=Math.abs(this._data.hours),this._data.months=Math.abs(this._data.months),this._data.years=Math.abs(this._data.years),this},weeks:function(){return o(this.days()/7)},valueOf:function(){return this._milliseconds+864e5*this._days+this._months%12*2592e6+31536e6*A(this._months/12)},humanize:function(a){var b=gb(this,!a,this.localeData());return a&&(b=this.localeData().pastFuture(+this,b)),this.localeData().postformat(b)},add:function(a,b){var c=tb.duration(a,b);return this._milliseconds+=c._milliseconds,this._days+=c._days,this._months+=c._months,this._bubble(),this},subtract:function(a,b){var c=tb.duration(a,b);return this._milliseconds-=c._milliseconds,this._days-=c._days,this._months-=c._months,this._bubble(),this},get:function(a){return a=x(a),this[a.toLowerCase()+"s"]()},as:function(a){var b,c;if(a=x(a),"month"===a||"year"===a)return b=this._days+this._milliseconds/864e5,c=this._months+12*pb(b),"month"===a?c:c/12;switch(b=this._days+Math.round(qb(this._months/12)),a){case"week":return b/7+this._milliseconds/6048e5;case"day":return b+this._milliseconds/864e5;case"hour":return 24*b+this._milliseconds/36e5;case"minute":return 24*b*60+this._milliseconds/6e4;case"second":return 24*b*60*60+this._milliseconds/1e3;
case"millisecond":return Math.floor(24*b*60*60*1e3)+this._milliseconds;default:throw new Error("Unknown unit "+a)}},lang:tb.fn.lang,locale:tb.fn.locale,toIsoString:f("toIsoString() is deprecated. Please use toISOString() instead (notice the capitals)",function(){return this.toISOString()}),toISOString:function(){var a=Math.abs(this.years()),b=Math.abs(this.months()),c=Math.abs(this.days()),d=Math.abs(this.hours()),e=Math.abs(this.minutes()),f=Math.abs(this.seconds()+this.milliseconds()/1e3);return this.asSeconds()?(this.asSeconds()<0?"-":"")+"P"+(a?a+"Y":"")+(b?b+"M":"")+(c?c+"D":"")+(d||e||f?"T":"")+(d?d+"H":"")+(e?e+"M":"")+(f?f+"S":""):"P0D"},localeData:function(){return this._locale}}),tb.duration.fn.toString=tb.duration.fn.toISOString;for(vb in ic)c(ic,vb)&&rb(vb.toLowerCase());tb.duration.fn.asMilliseconds=function(){return this.as("ms")},tb.duration.fn.asSeconds=function(){return this.as("s")},tb.duration.fn.asMinutes=function(){return this.as("m")},tb.duration.fn.asHours=function(){return this.as("h")},tb.duration.fn.asDays=function(){return this.as("d")},tb.duration.fn.asWeeks=function(){return this.as("weeks")},tb.duration.fn.asMonths=function(){return this.as("M")},tb.duration.fn.asYears=function(){return this.as("y")},tb.locale("en",{ordinalParse:/\d{1,2}(th|st|nd|rd)/,ordinal:function(a){var b=a%10,c=1===A(a%100/10)?"th":1===b?"st":2===b?"nd":3===b?"rd":"th";return a+c}}),Jb?module.exports=tb:"function"==typeof define&&define.amd?(define("moment",function(a,b,c){return c.config&&c.config()&&c.config().noGlobal===!0&&(xb.moment=ub),tb}),sb(!0)):sb()}).call(this);

/**
* hoverIntent r6 // 2011.02.26 // jQuery 1.5.1+
*/
(function($){$.fn.hoverIntent=function(f,g){var cfg={sensitivity:7,interval:100,timeout:0};cfg=$.extend(cfg,g?{over:f,out:g}:f);var cX,cY,pX,pY;var track=function(ev){cX=ev.pageX;cY=ev.pageY};var compare=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);if((Math.abs(pX-cX)+Math.abs(pY-cY))<cfg.sensitivity){$(ob).unbind("mousemove",track);ob.hoverIntent_s=1;return cfg.over.apply(ob,[ev])}else{pX=cX;pY=cY;ob.hoverIntent_t=setTimeout(function(){compare(ev,ob)},cfg.interval)}};var delay=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);ob.hoverIntent_s=0;return cfg.out.apply(ob,[ev])};var handleHover=function(e){var ev=jQuery.extend({},e);var ob=this;if(ob.hoverIntent_t){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t)}if(e.type=="mouseenter"){pX=ev.pageX;pY=ev.pageY;$(ob).bind("mousemove",track);if(ob.hoverIntent_s!=1){ob.hoverIntent_t=setTimeout(function(){compare(ev,ob)},cfg.interval)}}else{$(ob).unbind("mousemove",track);if(ob.hoverIntent_s==1){ob.hoverIntent_t=setTimeout(function(){delay(ev,ob)},cfg.timeout)}}};return this.bind('mouseenter',handleHover).bind('mouseleave',handleHover)}})(jQuery);


/*
    tablesorter 2.0.5
*/
(function($){$.extend({tablesorter:new
function(){var parsers=[],widgets=[];this.defaults={cssHeader:"header",cssAsc:"headerSortUp",cssDesc:"headerSortDown",cssChildRow:"expand-child",sortInitialOrder:"asc",sortMultiSortKey:"shiftKey",sortForce:null,sortAppend:null,sortLocaleCompare:true,textExtraction:"simple",parsers:{},widgets:[],widgetZebra:{css:["even","odd"]},headers:{},widthFixed:false,cancelSelection:true,sortList:[],headerList:[],dateFormat:"us",decimal:'/\.|\,/g',onRenderHeader:null,selectorHeaders:'thead th',debug:false};function benchmark(s,d){log(s+","+(new Date().getTime()-d.getTime())+"ms");}this.benchmark=benchmark;function log(s){if(typeof console!="undefined"&&typeof console.debug!="undefined"){console.log(s);}else{alert(s);}}function buildParserCache(table,$headers){if(table.config.debug){var parsersDebug="";}if(table.tBodies.length==0)return;var rows=table.tBodies[0].rows;if(rows[0]){var list=[],cells=rows[0].cells,l=cells.length;for(var i=0;i<l;i++){var p=false;if($.metadata&&($($headers[i]).metadata()&&$($headers[i]).metadata().sorter)){p=getParserById($($headers[i]).metadata().sorter);}else if((table.config.headers[i]&&table.config.headers[i].sorter)){p=getParserById(table.config.headers[i].sorter);}if(!p){p=detectParserForColumn(table,rows,-1,i);}if(table.config.debug){parsersDebug+="column:"+i+" parser:"+p.id+"\n";}list.push(p);}}if(table.config.debug){log(parsersDebug);}return list;};function detectParserForColumn(table,rows,rowIndex,cellIndex){var l=parsers.length,node=false,nodeValue=false,keepLooking=true;while(nodeValue==''&&keepLooking){rowIndex++;if(rows[rowIndex]){node=getNodeFromRowAndCellIndex(rows,rowIndex,cellIndex);nodeValue=trimAndGetNodeText(table.config,node);if(table.config.debug){log('Checking if value was empty on row:'+rowIndex);}}else{keepLooking=false;}}for(var i=1;i<l;i++){if(parsers[i].is(nodeValue,table,node)){return parsers[i];}}return parsers[0];}function getNodeFromRowAndCellIndex(rows,rowIndex,cellIndex){return rows[rowIndex].cells[cellIndex];}function trimAndGetNodeText(config,node){return $.trim(getElementText(config,node));}function getParserById(name){var l=parsers.length;for(var i=0;i<l;i++){if(parsers[i].id.toLowerCase()==name.toLowerCase()){return parsers[i];}}return false;}function buildCache(table){if(table.config.debug){var cacheTime=new Date();}var totalRows=(table.tBodies[0]&&table.tBodies[0].rows.length)||0,totalCells=(table.tBodies[0].rows[0]&&table.tBodies[0].rows[0].cells.length)||0,parsers=table.config.parsers,cache={row:[],normalized:[]};for(var i=0;i<totalRows;++i){var c=$(table.tBodies[0].rows[i]),cols=[];if(c.hasClass(table.config.cssChildRow)){cache.row[cache.row.length-1]=cache.row[cache.row.length-1].add(c);continue;}cache.row.push(c);for(var j=0;j<totalCells;++j){cols.push(parsers[j].format(getElementText(table.config,c[0].cells[j]),table,c[0].cells[j]));}cols.push(cache.normalized.length);cache.normalized.push(cols);cols=null;};if(table.config.debug){benchmark("Building cache for "+totalRows+" rows:",cacheTime);}return cache;};function getElementText(config,node){var text="";if(!node)return"";if(!config.supportsTextContent)config.supportsTextContent=node.textContent||false;if(config.textExtraction=="simple"){if(config.supportsTextContent){text=node.textContent;}else{if(node.childNodes[0]&&node.childNodes[0].hasChildNodes()){text=node.childNodes[0].innerHTML;}else{text=node.innerHTML;}}}else{if(typeof(config.textExtraction)=="function"){text=config.textExtraction(node);}else{text=$(node).text();}}return text;}function appendToTable(table,cache){if(table.config.debug){var appendTime=new Date()}var c=cache,r=c.row,n=c.normalized,totalRows=n.length,checkCell=(n[0].length-1),tableBody=$(table.tBodies[0]),rows=[];for(var i=0;i<totalRows;i++){var pos=n[i][checkCell];rows.push(r[pos]);if(!table.config.appender){var l=r[pos].length;for(var j=0;j<l;j++){tableBody[0].appendChild(r[pos][j]);}}}if(table.config.appender){table.config.appender(table,rows);}rows=null;if(table.config.debug){benchmark("Rebuilt table:",appendTime);}applyWidget(table);setTimeout(function(){$(table).trigger("sortEnd");},0);};function buildHeaders(table){if(table.config.debug){var time=new Date();}var meta=($.metadata)?true:false;var header_index=computeTableHeaderCellIndexes(table);$tableHeaders=$(table.config.selectorHeaders,table).each(function(index){this.column=header_index[this.parentNode.rowIndex+"-"+this.cellIndex];this.order=formatSortingOrder(table.config.sortInitialOrder);this.count=this.order;if(checkHeaderMetadata(this)||checkHeaderOptions(table,index))this.sortDisabled=true;if(checkHeaderOptionsSortingLocked(table,index))this.order=this.lockedOrder=checkHeaderOptionsSortingLocked(table,index);if(!this.sortDisabled){var $th=$(this).addClass(table.config.cssHeader);if(table.config.onRenderHeader)table.config.onRenderHeader.apply($th);}table.config.headerList[index]=this;});if(table.config.debug){benchmark("Built headers:",time);log($tableHeaders);}return $tableHeaders;};function computeTableHeaderCellIndexes(t){var matrix=[];var lookup={};var thead=t.getElementsByTagName('THEAD')[0];var trs=thead.getElementsByTagName('TR');for(var i=0;i<trs.length;i++){var cells=trs[i].cells;for(var j=0;j<cells.length;j++){var c=cells[j];var rowIndex=c.parentNode.rowIndex;var cellId=rowIndex+"-"+c.cellIndex;var rowSpan=c.rowSpan||1;var colSpan=c.colSpan||1
var firstAvailCol;if(typeof(matrix[rowIndex])=="undefined"){matrix[rowIndex]=[];}for(var k=0;k<matrix[rowIndex].length+1;k++){if(typeof(matrix[rowIndex][k])=="undefined"){firstAvailCol=k;break;}}lookup[cellId]=firstAvailCol;for(var k=rowIndex;k<rowIndex+rowSpan;k++){if(typeof(matrix[k])=="undefined"){matrix[k]=[];}var matrixrow=matrix[k];for(var l=firstAvailCol;l<firstAvailCol+colSpan;l++){matrixrow[l]="x";}}}}return lookup;}function checkCellColSpan(table,rows,row){var arr=[],r=table.tHead.rows,c=r[row].cells;for(var i=0;i<c.length;i++){var cell=c[i];if(cell.colSpan>1){arr=arr.concat(checkCellColSpan(table,headerArr,row++));}else{if(table.tHead.length==1||(cell.rowSpan>1||!r[row+1])){arr.push(cell);}}}return arr;};function checkHeaderMetadata(cell){if(($.metadata)&&($(cell).metadata().sorter===false)){return true;};return false;}function checkHeaderOptions(table,i){if((table.config.headers[i])&&(table.config.headers[i].sorter===false)){return true;};return false;}function checkHeaderOptionsSortingLocked(table,i){if((table.config.headers[i])&&(table.config.headers[i].lockedOrder))return table.config.headers[i].lockedOrder;return false;}function applyWidget(table){var c=table.config.widgets;var l=c.length;for(var i=0;i<l;i++){getWidgetById(c[i]).format(table);}}function getWidgetById(name){var l=widgets.length;for(var i=0;i<l;i++){if(widgets[i].id.toLowerCase()==name.toLowerCase()){return widgets[i];}}};function formatSortingOrder(v){if(typeof(v)!="Number"){return(v.toLowerCase()=="desc")?1:0;}else{return(v==1)?1:0;}}function isValueInArray(v,a){var l=a.length;for(var i=0;i<l;i++){if(a[i][0]==v){return true;}}return false;}function setHeadersCss(table,$headers,list,css){$headers.removeClass(css[0]).removeClass(css[1]);var h=[];$headers.each(function(offset){if(!this.sortDisabled){h[this.column]=$(this);}});var l=list.length;for(var i=0;i<l;i++){h[list[i][0]].addClass(css[list[i][1]]);}}function fixColumnWidth(table,$headers){var c=table.config;if(c.widthFixed){var colgroup=$('<colgroup>');$("tr:first td",table.tBodies[0]).each(function(){colgroup.append($('<col>').css('width',$(this).width()));});$(table).prepend(colgroup);};}function updateHeaderSortCount(table,sortList){var c=table.config,l=sortList.length;for(var i=0;i<l;i++){var s=sortList[i],o=c.headerList[s[0]];o.count=s[1];o.count++;}}function multisort(table,sortList,cache){if(table.config.debug){var sortTime=new Date();}var dynamicExp="var sortWrapper = function(a,b) {",l=sortList.length;for(var i=0;i<l;i++){var c=sortList[i][0];var order=sortList[i][1];var s=(table.config.parsers[c].type=="text")?((order==0)?makeSortFunction("text","asc",c):makeSortFunction("text","desc",c)):((order==0)?makeSortFunction("numeric","asc",c):makeSortFunction("numeric","desc",c));var e="e"+i;dynamicExp+="var "+e+" = "+s;dynamicExp+="if("+e+") { return "+e+"; } ";dynamicExp+="else { ";}var orgOrderCol=cache.normalized[0].length-1;dynamicExp+="return a["+orgOrderCol+"]-b["+orgOrderCol+"];";for(var i=0;i<l;i++){dynamicExp+="}; ";}dynamicExp+="return 0; ";dynamicExp+="}; ";if(table.config.debug){benchmark("Evaling expression:"+dynamicExp,new Date());}eval(dynamicExp);cache.normalized.sort(sortWrapper);if(table.config.debug){benchmark("Sorting on "+sortList.toString()+" and dir "+order+" time:",sortTime);}return cache;};function makeSortFunction(type,direction,index){var a="a["+index+"]",b="b["+index+"]";if(type=='text'&&direction=='asc'){return"("+a+" == "+b+" ? 0 : ("+a+" === null ? Number.POSITIVE_INFINITY : ("+b+" === null ? Number.NEGATIVE_INFINITY : ("+a+" < "+b+") ? -1 : 1 )));";}else if(type=='text'&&direction=='desc'){return"("+a+" == "+b+" ? 0 : ("+a+" === null ? Number.POSITIVE_INFINITY : ("+b+" === null ? Number.NEGATIVE_INFINITY : ("+b+" < "+a+") ? -1 : 1 )));";}else if(type=='numeric'&&direction=='asc'){return"("+a+" === null && "+b+" === null) ? 0 :("+a+" === null ? Number.POSITIVE_INFINITY : ("+b+" === null ? Number.NEGATIVE_INFINITY : "+a+" - "+b+"));";}else if(type=='numeric'&&direction=='desc'){return"("+a+" === null && "+b+" === null) ? 0 :("+a+" === null ? Number.POSITIVE_INFINITY : ("+b+" === null ? Number.NEGATIVE_INFINITY : "+b+" - "+a+"));";}};function makeSortText(i){return"((a["+i+"] < b["+i+"]) ? -1 : ((a["+i+"] > b["+i+"]) ? 1 : 0));";};function makeSortTextDesc(i){return"((b["+i+"] < a["+i+"]) ? -1 : ((b["+i+"] > a["+i+"]) ? 1 : 0));";};function makeSortNumeric(i){return"a["+i+"]-b["+i+"];";};function makeSortNumericDesc(i){return"b["+i+"]-a["+i+"];";};function sortText(a,b){if(table.config.sortLocaleCompare)return a.localeCompare(b);return((a<b)?-1:((a>b)?1:0));};function sortTextDesc(a,b){if(table.config.sortLocaleCompare)return b.localeCompare(a);return((b<a)?-1:((b>a)?1:0));};function sortNumeric(a,b){return a-b;};function sortNumericDesc(a,b){return b-a;};function getCachedSortType(parsers,i){return parsers[i].type;};this.construct=function(settings){return this.each(function(){if(!this.tHead||!this.tBodies)return;var $this,$document,$headers,cache,config,shiftDown=0,sortOrder;this.config={};config=$.extend(this.config,$.tablesorter.defaults,settings);$this=$(this);$.data(this,"tablesorter",config);$headers=buildHeaders(this);this.config.parsers=buildParserCache(this,$headers);cache=buildCache(this);var sortCSS=[config.cssDesc,config.cssAsc];fixColumnWidth(this);$headers.click(function(e){var totalRows=($this[0].tBodies[0]&&$this[0].tBodies[0].rows.length)||0;if(!this.sortDisabled&&totalRows>0){$this.trigger("sortStart");var $cell=$(this);var i=this.column;this.order=this.count++%2;if(this.lockedOrder)this.order=this.lockedOrder;if(!e[config.sortMultiSortKey]){config.sortList=[];if(config.sortForce!=null){var a=config.sortForce;for(var j=0;j<a.length;j++){if(a[j][0]!=i){config.sortList.push(a[j]);}}}config.sortList.push([i,this.order]);}else{if(isValueInArray(i,config.sortList)){for(var j=0;j<config.sortList.length;j++){var s=config.sortList[j],o=config.headerList[s[0]];if(s[0]==i){o.count=s[1];o.count++;s[1]=o.count%2;}}}else{config.sortList.push([i,this.order]);}};setTimeout(function(){setHeadersCss($this[0],$headers,config.sortList,sortCSS);appendToTable($this[0],multisort($this[0],config.sortList,cache));},1);return false;}}).mousedown(function(){if(config.cancelSelection){this.onselectstart=function(){return false};return false;}});$this.bind("update",function(){var me=this;setTimeout(function(){me.config.parsers=buildParserCache(me,$headers);cache=buildCache(me);},1);}).bind("updateCell",function(e,cell){var config=this.config;var pos=[(cell.parentNode.rowIndex-1),cell.cellIndex];cache.normalized[pos[0]][pos[1]]=config.parsers[pos[1]].format(getElementText(config,cell),cell);}).bind("sorton",function(e,list){$(this).trigger("sortStart");config.sortList=list;var sortList=config.sortList;updateHeaderSortCount(this,sortList);setHeadersCss(this,$headers,sortList,sortCSS);appendToTable(this,multisort(this,sortList,cache));}).bind("appendCache",function(){appendToTable(this,cache);}).bind("applyWidgetId",function(e,id){getWidgetById(id).format(this);}).bind("applyWidgets",function(){applyWidget(this);});if($.metadata&&($(this).metadata()&&$(this).metadata().sortlist)){config.sortList=$(this).metadata().sortlist;}if(config.sortList.length>0){$this.trigger("sorton",[config.sortList]);}applyWidget(this);});};this.addParser=function(parser){var l=parsers.length,a=true;for(var i=0;i<l;i++){if(parsers[i].id.toLowerCase()==parser.id.toLowerCase()){a=false;}}if(a){parsers.push(parser);};};this.addWidget=function(widget){widgets.push(widget);};this.formatFloat=function(s){var i=parseFloat(s);return(isNaN(i))?0:i;};this.formatInt=function(s){var i=parseInt(s);return(isNaN(i))?0:i;};this.isDigit=function(s,config){return/^[-+]?\d*$/.test($.trim(s.replace(/[,.']/g,'')));};this.clearTableBody=function(table){if($.browser.msie){function empty(){while(this.firstChild)this.removeChild(this.firstChild);}empty.apply(table.tBodies[0]);}else{table.tBodies[0].innerHTML="";}};}});$.fn.extend({tablesorter:$.tablesorter.construct});var ts=$.tablesorter;ts.addParser({id:"text",is:function(s){return true;},format:function(s){return $.trim(s.toLocaleLowerCase());},type:"text"});ts.addParser({id:"digit",is:function(s,table){var c=table.config;return $.tablesorter.isDigit(s,c);},format:function(s){return $.tablesorter.formatFloat(s);},type:"numeric"});ts.addParser({id:"currency",is:function(s){return/^[£$€?.]/.test(s);},format:function(s){return $.tablesorter.formatFloat(s.replace(new RegExp(/[£$€]/g),""));},type:"numeric"});ts.addParser({id:"ipAddress",is:function(s){return/^\d{2,3}[\.]\d{2,3}[\.]\d{2,3}[\.]\d{2,3}$/.test(s);},format:function(s){var a=s.split("."),r="",l=a.length;for(var i=0;i<l;i++){var item=a[i];if(item.length==2){r+="0"+item;}else{r+=item;}}return $.tablesorter.formatFloat(r);},type:"numeric"});ts.addParser({id:"url",is:function(s){return/^(https?|ftp|file):\/\/$/.test(s);},format:function(s){return jQuery.trim(s.replace(new RegExp(/(https?|ftp|file):\/\//),''));},type:"text"});ts.addParser({id:"isoDate",is:function(s){return/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(s);},format:function(s){return $.tablesorter.formatFloat((s!="")?new Date(s.replace(new RegExp(/-/g),"/")).getTime():"0");},type:"numeric"});ts.addParser({id:"percent",is:function(s){return/\%$/.test($.trim(s));},format:function(s){return $.tablesorter.formatFloat(s.replace(new RegExp(/%/g),""));},type:"numeric"});ts.addParser({id:"usLongDate",is:function(s){return s.match(new RegExp(/^[A-Za-z]{3,10}\.? [0-9]{1,2}, ([0-9]{4}|'?[0-9]{2}) (([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(AM|PM)))$/));},format:function(s){return $.tablesorter.formatFloat(new Date(s).getTime());},type:"numeric"});ts.addParser({id:"shortDate",is:function(s){return/\d{1,2}[\/\-]\d{1,2}[\/\-]\d{2,4}/.test(s);},format:function(s,table){var c=table.config;s=s.replace(/\-/g,"/");if(c.dateFormat=="us"){s=s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/,"$3/$1/$2");}else if(c.dateFormat=="uk"){s=s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/,"$3/$2/$1");}else if(c.dateFormat=="dd/mm/yy"||c.dateFormat=="dd-mm-yy"){s=s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2})/,"$1/$2/$3");}return $.tablesorter.formatFloat(new Date(s).getTime());},type:"numeric"});ts.addParser({id:"time",is:function(s){return/^(([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(am|pm)))$/.test(s);},format:function(s){return $.tablesorter.formatFloat(new Date("2000/01/01 "+s).getTime());},type:"numeric"});ts.addParser({id:"metadata",is:function(s){return false;},format:function(s,table,cell){var c=table.config,p=(!c.parserMetadataName)?'sortValue':c.parserMetadataName;return $(cell).metadata()[p];},type:"numeric"});ts.addWidget({id:"zebra",format:function(table){if(table.config.debug){var time=new Date();}var $tr,row=-1,odd;$("tr:visible",table.tBodies[0]).each(function(i){$tr=$(this);if(!$tr.hasClass(table.config.cssChildRow))row++;odd=(row%2==0);$tr.removeClass(table.config.widgetZebra.css[odd?0:1]).addClass(table.config.widgetZebra.css[odd?1:0])});if(table.config.debug){$.tablesorter.benchmark("Applying Zebra widget",time);}}});})(jQuery);


/*
Masked Input plugin for jQuery
Copyright (c) 2007-2013 Josh Bush (digitalbush.com)
Licensed under the MIT license (http://digitalbush.com/projects/masked-input-plugin/#license)
Version: 1.3.1
*/
(function(e){function t(){var e=document.createElement("input"),t="onpaste";return e.setAttribute(t,""),"function"==typeof e[t]?"paste":"input"}var n,a=t()+".mask",r=navigator.userAgent,i=/iphone/i.test(r),o=/android/i.test(r);e.mask={definitions:{9:"[0-9]",a:"[A-Za-z]","*":"[A-Za-z0-9]"},dataName:"rawMaskFn",placeholder:"_"},e.fn.extend({caret:function(e,t){var n;if(0!==this.length&&!this.is(":hidden"))return"number"==typeof e?(t="number"==typeof t?t:e,this.each(function(){this.setSelectionRange?this.setSelectionRange(e,t):this.createTextRange&&(n=this.createTextRange(),n.collapse(!0),n.moveEnd("character",t),n.moveStart("character",e),n.select())})):(this[0].setSelectionRange?(e=this[0].selectionStart,t=this[0].selectionEnd):document.selection&&document.selection.createRange&&(n=document.selection.createRange(),e=0-n.duplicate().moveStart("character",-1e5),t=e+n.text.length),{begin:e,end:t})},unmask:function(){return this.trigger("unmask")},mask:function(t,r){var c,l,s,u,f,h;return!t&&this.length>0?(c=e(this[0]),c.data(e.mask.dataName)()):(r=e.extend({placeholder:e.mask.placeholder,completed:null},r),l=e.mask.definitions,s=[],u=h=t.length,f=null,e.each(t.split(""),function(e,t){"?"==t?(h--,u=e):l[t]?(s.push(RegExp(l[t])),null===f&&(f=s.length-1)):s.push(null)}),this.trigger("unmask").each(function(){function c(e){for(;h>++e&&!s[e];);return e}function d(e){for(;--e>=0&&!s[e];);return e}function m(e,t){var n,a;if(!(0>e)){for(n=e,a=c(t);h>n;n++)if(s[n]){if(!(h>a&&s[n].test(R[a])))break;R[n]=R[a],R[a]=r.placeholder,a=c(a)}b(),x.caret(Math.max(f,e))}}function p(e){var t,n,a,i;for(t=e,n=r.placeholder;h>t;t++)if(s[t]){if(a=c(t),i=R[t],R[t]=n,!(h>a&&s[a].test(i)))break;n=i}}function g(e){var t,n,a,r=e.which;8===r||46===r||i&&127===r?(t=x.caret(),n=t.begin,a=t.end,0===a-n&&(n=46!==r?d(n):a=c(n-1),a=46===r?c(a):a),k(n,a),m(n,a-1),e.preventDefault()):27==r&&(x.val(S),x.caret(0,y()),e.preventDefault())}function v(t){var n,a,i,l=t.which,u=x.caret();t.ctrlKey||t.altKey||t.metaKey||32>l||l&&(0!==u.end-u.begin&&(k(u.begin,u.end),m(u.begin,u.end-1)),n=c(u.begin-1),h>n&&(a=String.fromCharCode(l),s[n].test(a)&&(p(n),R[n]=a,b(),i=c(n),o?setTimeout(e.proxy(e.fn.caret,x,i),0):x.caret(i),r.completed&&i>=h&&r.completed.call(x))),t.preventDefault())}function k(e,t){var n;for(n=e;t>n&&h>n;n++)s[n]&&(R[n]=r.placeholder)}function b(){x.val(R.join(""))}function y(e){var t,n,a=x.val(),i=-1;for(t=0,pos=0;h>t;t++)if(s[t]){for(R[t]=r.placeholder;pos++<a.length;)if(n=a.charAt(pos-1),s[t].test(n)){R[t]=n,i=t;break}if(pos>a.length)break}else R[t]===a.charAt(pos)&&t!==u&&(pos++,i=t);return e?b():u>i+1?(x.val(""),k(0,h)):(b(),x.val(x.val().substring(0,i+1))),u?t:f}var x=e(this),R=e.map(t.split(""),function(e){return"?"!=e?l[e]?r.placeholder:e:void 0}),S=x.val();x.data(e.mask.dataName,function(){return e.map(R,function(e,t){return s[t]&&e!=r.placeholder?e:null}).join("")}),x.attr("readonly")||x.one("unmask",function(){x.unbind(".mask").removeData(e.mask.dataName)}).bind("focus.mask",function(){clearTimeout(n);var e;S=x.val(),e=y(),n=setTimeout(function(){b(),e==t.length?x.caret(0,e):x.caret(e)},10)}).bind("blur.mask",function(){y(),x.val()!=S&&x.change()}).bind("keydown.mask",g).bind("keypress.mask",v).bind(a,function(){setTimeout(function(){var e=y(!0);x.caret(e),r.completed&&e==x.val().length&&r.completed.call(x)},0)}),y()}))}})})(jQuery);

/* Placeholder pluginhttp://mths.be/placeholder v2.0.7 by @mathias */
;(function(f,h,$){var a='placeholder' in h.createElement('input'),d='placeholder' in h.createElement('textarea'),i=$.fn,c=$.valHooks,k,j;if(a&&d){j=i.placeholder=function(){return this};j.input=j.textarea=true}else{j=i.placeholder=function(){var l=this;l.filter((a?'textarea':':input')+'[placeholder]').not('.placeholder').bind({'focus.placeholder':b,'blur.placeholder':e}).data('placeholder-enabled',true).trigger('blur.placeholder');return l};j.input=a;j.textarea=d;k={get:function(m){var l=$(m);return l.data('placeholder-enabled')&&l.hasClass('placeholder')?'':m.value},set:function(m,n){var l=$(m);if(!l.data('placeholder-enabled')){return m.value=n}if(n==''){m.value=n;if(m!=h.activeElement){e.call(m)}}else{if(l.hasClass('placeholder')){b.call(m,true,n)||(m.value=n)}else{m.value=n}}return l}};a||(c.input=k);d||(c.textarea=k);$(function(){$(h).delegate('form','submit.placeholder',function(){var l=$('.placeholder',this).each(b);setTimeout(function(){l.each(e)},10)})});$(f).bind('beforeunload.placeholder',function(){$('.placeholder').each(function(){this.value=''})})}function g(m){var l={},n=/^jQuery\d+$/;$.each(m.attributes,function(p,o){if(o.specified&&!n.test(o.name)){l[o.name]=o.value}});return l}function b(m,n){var l=this,o=$(l);if(l.value==o.attr('placeholder')&&o.hasClass('placeholder')){if(o.data('placeholder-password')){o=o.hide().next().show().attr('id',o.removeAttr('id').data('placeholder-id'));if(m===true){return o[0].value=n}o.focus()}else{l.value='';o.removeClass('placeholder');l==h.activeElement&&l.select()}}}function e(){var q,l=this,p=$(l),m=p,o=this.id;if(l.value==''){if(l.type=='password'){if(!p.data('placeholder-textinput')){try{q=p.clone().attr({type:'text'})}catch(n){q=$('<input>').attr($.extend(g(this),{type:'text'}))}q.removeAttr('name').data({'placeholder-password':true,'placeholder-id':o}).bind('focus.placeholder',b);p.data({'placeholder-textinput':q,'placeholder-id':o}).before(q)}p=p.removeAttr('id').hide().prev().attr('id',o).show()}p.addClass('placeholder');p[0].value=p.attr('placeholder')}else{p.removeClass('placeholder')}}}(this,document,jQuery));

/*
 * jQuery UI Stars v3.0.1
 */
(function(A){A.widget("ui.stars",{options:{inputType:"radio",split:0,disabled:false,cancelTitle:"Cancel Rating",cancelValue:0,cancelShow:true,disableValue:true,oneVoteOnly:false,showTitles:false,captionEl:null,callback:null,starWidth:16,cancelClass:"ui-stars-cancel",starClass:"ui-stars-star",starOnClass:"ui-stars-star-on",starHoverClass:"ui-stars-star-hover",starDisabledClass:"ui-stars-star-disabled",cancelHoverClass:"ui-stars-cancel-hover",cancelDisabledClass:"ui-stars-cancel-disabled"},_create:function(){var C=this,F=this.options,B=0;this.element.data("former.stars",this.element.html());F.isSelect=F.inputType=="select";this.$form=A(this.element).closest("form");this.$selec=F.isSelect?A("select",this.element):null;this.$rboxs=F.isSelect?A("option",this.$selec):A(":radio",this.element);this.$stars=this.$rboxs.map(function(I){var J={value:this.value,title:(F.isSelect?this.text:this.title)||this.value,isDefault:(F.isSelect&&this.defaultSelected)||this.defaultChecked};if(I==0){F.split=typeof F.split!="number"?0:F.split;F.val2id=[];F.id2val=[];F.id2title=[];F.name=F.isSelect?C.$selec.get(0).name:this.name;F.disabled=F.disabled||(F.isSelect?A(C.$selec).attr("disabled"):A(this).attr("disabled"))}if(J.value==F.cancelValue){F.cancelTitle=J.title;return null}F.val2id[J.value]=B;F.id2val[B]=J.value;F.id2title[B]=J.title;if(J.isDefault){F.checked=B;F.value=F.defaultValue=J.value;F.title=J.title}var H=A("<div/>").addClass(F.starClass);var K=A("<a/>").attr("title",F.showTitles?J.title:"").text(J.value);if(F.split){var G=(B%F.split);var L=Math.floor(F.starWidth/F.split);H.width(L);K.css("margin-left","-"+(G*L)+"px")}B++;return H.append(K).get(0)});F.items=B;F.isSelect?this.$selec.remove():this.$rboxs.remove();this.$cancel=A("<div/>").addClass(F.cancelClass).append(A("<a/>").attr("title",F.showTitles?F.cancelTitle:"").text(F.cancelValue));F.cancelShow&=!F.disabled&&!F.oneVoteOnly;F.cancelShow&&this.element.append(this.$cancel);this.element.append(this.$stars);if(F.checked===undefined){F.checked=-1;F.value=F.defaultValue=F.cancelValue;F.title=""}this.$value=A("<input type='hidden' name='"+F.name+"' value='"+F.value+"' />");this.element.append(this.$value);this.$stars.bind("click.stars",function(H){if(!F.forceSelect&&F.disabled){return false}var G=C.$stars.index(this);F.checked=G;F.value=F.id2val[G];F.title=F.id2title[G];C.$value.attr({disabled:F.disabled?"disabled":"",value:F.value});D(G,false);C._disableCancel();!F.forceSelect&&C.callback(H,"star")}).bind("mouseover.stars",function(){if(F.disabled){return false}var G=C.$stars.index(this);D(G,true)}).bind("mouseout.stars",function(){if(F.disabled){return false}D(C.options.checked,false)});this.$cancel.bind("click.stars",function(G){if(!F.forceSelect&&(F.disabled||F.value==F.cancelValue)){return false}F.checked=-1;F.value=F.cancelValue;F.title="";C.$value.val(F.value);F.disableValue&&C.$value.attr({disabled:"disabled"});E();C._disableCancel();!F.forceSelect&&C.callback(G,"cancel")}).bind("mouseover.stars",function(){if(C._disableCancel()){return false}C.$cancel.addClass(F.cancelHoverClass);E();C._showCap(F.cancelTitle)}).bind("mouseout.stars",function(){if(C._disableCancel()){return false}C.$cancel.removeClass(F.cancelHoverClass);C.$stars.triggerHandler("mouseout.stars")});this.$form.bind("reset.stars",function(){!F.disabled&&C.select(F.defaultValue)});A(window).unload(function(){C.$cancel.unbind(".stars");C.$stars.unbind(".stars");C.$form.unbind(".stars");C.$selec=C.$rboxs=C.$stars=C.$value=C.$cancel=C.$form=null});function D(G,I){if(G!=-1){var J=I?F.starHoverClass:F.starOnClass;var H=I?F.starOnClass:F.starHoverClass;C.$stars.eq(G).prevAll("."+F.starClass).andSelf().removeClass(H).addClass(J);C.$stars.eq(G).nextAll("."+F.starClass).removeClass(F.starHoverClass+" "+F.starOnClass);C._showCap(F.id2title[G])}else{E()}}function E(){C.$stars.removeClass(F.starOnClass+" "+F.starHoverClass);C._showCap("")}this.select(F.value);F.disabled&&this.disable()},_disableCancel:function(){var C=this.options,B=C.disabled||C.oneVoteOnly||(C.value==C.cancelValue);if(B){this.$cancel.removeClass(C.cancelHoverClass).addClass(C.cancelDisabledClass)}else{this.$cancel.removeClass(C.cancelDisabledClass)}this.$cancel.css("opacity",B?0.5:1);return B},_disableAll:function(){var B=this.options;this._disableCancel();if(B.disabled){this.$stars.filter("div").addClass(B.starDisabledClass)}else{this.$stars.filter("div").removeClass(B.starDisabledClass)}},_showCap:function(B){var C=this.options;if(C.captionEl){C.captionEl.text(B)}},value:function(){return this.options.value},select:function(D){var C=this.options,B=(D==C.cancelValue)?this.$cancel:this.$stars.eq(C.val2id[D]);C.forceSelect=true;B.triggerHandler("click.stars");C.forceSelect=false},selectID:function(D){var C=this.options,B=(D==-1)?this.$cancel:this.$stars.eq(D);C.forceSelect=true;B.triggerHandler("click.stars");C.forceSelect=false},enable:function(){this.options.disabled=false;this._disableAll()},disable:function(){this.options.disabled=true;this._disableAll()},destroy:function(){this.$form.unbind(".stars");this.$cancel.unbind(".stars").remove();this.$stars.unbind(".stars").remove();this.$value.remove();this.element.unbind(".stars").html(this.element.data("former.stars")).removeData("stars");return this},callback:function(C,B){var D=this.options;D.callback&&D.callback(this,B,D.value,C);D.oneVoteOnly&&!D.disabled&&this.disable()}});A.extend(A.ui.stars,{version:"3.0.1"})})(jQuery);

/* jQuery NOTICE (WARNING : customized for jSCAF !!!)    */
(function(jQuery)
{
    jQuery.extend({


        noticeAdd: function(options)
        {

            var defaults = {
                inEffect:             {opacity: 'show'},    // in effect
                inEffectDuration:     0,                    // in effect duration in miliseconds
                stayTime:             5000,                // time in miliseconds before the item has to disappear
                text:                 '',                    // content of the item
                stay:                 false,                // should the notice item stay or not?
                type:                 'notice',             // could also be error, succes
                resetFirst:            true,
                position:             'bottom',
                tooltipIconId:        '',
                showClose:            true
            }


            // declare varaibles
            var options, noticeWrapAll, noticeItemOuter, noticeItemInner, noticeItemClose;

            options = jQuery.extend({}, defaults, options);
            noticeWrapAll = (!jQuery('.notice-wrap').length) ? jQuery('<div></div>').addClass('notice-wrap').appendTo('body') : jQuery('.notice-wrap');

            if (options.text!=null && $.trim(options.text).length!=0) {

                //if (!jQuery('.notice-item p:contains(' + options.text + ')').length)
                //{

                    if (options.resetFirst)
                    {
                        $('.notice-item-wrapper').remove();
                    }

                    if (options.tooltipIconId != '')
                    {
                        $('#' + options.tooltipIconId).addClass('hidden');
                    }

                    noticeItemOuter = jQuery('<div></div>').addClass('notice-item-wrapper');
                    noticeItemInner = jQuery('<div></div>').hide().addClass('notice-item ' + options.type).appendTo(noticeWrapAll).html('<p>' + options.text + '</p>' + '<span id="notice-tooltipIconId" class="hidden">' + options.tooltipIconId + '</span>').animate(options.inEffect, options.inEffectDuration).wrap(noticeItemOuter);
                    if (options.showClose) {
                        noticeItemClose = jQuery('<div></div>').addClass('notice-item-close').prependTo(noticeItemInner).click(function() {
                            jQuery.noticeRemoveObj(noticeItemInner)
                        });
                    }
                //}

                if (navigator.userAgent.match(/MSIE 6/i))
                {
                    noticeWrapAll.css({top: document.documentElement.scrollTop});
                }

                if (!options.stay)
                {
                    setTimeout(function()
                    {
                        jQuery.noticeRemove(options.text);
                    },
                            options.stayTime);
                }

                if (options.position == 'bottom') {
                    noticeWrapAll.css({bottom: 30});
                } else {
                    noticeWrapAll.css({top: 100});
                }

            }
        },

        noticeRemoveAll: function(obj)
        {
            if (obj == null || obj == 'undefined') {
                obj = $('.notice-item-wrapper');
            }

            if (obj) {
                obj.animate({opacity: '0'}, 600, function()
                {
                    obj.parent().animate({height: '0px'}, 300, function()
                    {
                        obj.parent().remove();
                    });
                });
            }
        },

        noticeRemoveObj: function(obj)
        {
            if (obj) {
                obj.animate({opacity: '0'}, 600, function()
                {
                    obj.parent().animate({height: '0px'}, 300, function()
                    {
                        obj.parent().remove();
                    });
                });
            }

            if ($('#notice-tooltipIconId').html().length != 0) {

                $('#' + $('#notice-tooltipIconId').html()).removeClass('hidden');

            }

        },

        noticeRemove: function(text)
        {
            var obj = jQuery('.notice-item p:contains(' + text + ')').parent();
            if (obj) {
                obj.animate({opacity: '0'}, 600, function()
                {
                    obj.parent().animate({height: '0px'}, 300, function()
                    {
                        obj.parent().remove();
                    });
                });
            }

            if ($('#' + $('#notice-tooltipIconId').html()).length != 0) {

                $('#' + $('#notice-tooltipIconId').html()).removeClass('hidden');

            }
        }
    });
})(jQuery);





/*
 *   CUSTOMISABLE FUNCTIONS
 */


// for disabling button on click, preventing double-clicking

(function($) {

    $.fn.onButtonClick = function(
            eventType,
            selector,
            eventHandler
            ) {

        // Create a new proxy function that wraps around the
        // given bind callback.
        var proxy = function(event) {

            // Execute the IF condition first to see if
            // the event handler should be executed.
            if (!$(selector).find('.button').hasClass('disabled')) {

                //$(selector).find('.button').addClass('disabled');

                // Pass the event onto the original event
                // handler.

                eventHandler.apply(this, arguments);

            }
        };

        // Bind the proxy method to the target.
        this.on(eventType, selector, proxy);

        // Return this to keep jQuery method chaining.
        return( this );
    };

})(jQuery);




// custom tablesorter parsers

$.tablesorter.addParser({
    // set a unique id
    id: 'missionIdParser',
    is: function(s) {
        // return false so this parser is not auto detected
        return false;
    },
    format: function(s) {
        return s.substr(3);
    },
    // set type, either numeric or text
    type: 'text'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'dateParser',
    is: function(s) {
        // return false so this parser is not auto detected
        return false;
    },
    format: function(s) {
        var src = $.trim(s);
        return src.substr(6, 4) + src.substr(3, 2) + src.substr(0, 2);
    },
    // set type, either numeric or text
    type: 'text'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'smallDateParser',
    is: function(s) {
        // return false so this parser is not auto detected
        return false;
    },
    format: function(s) {
        var src = $.trim(s);
        return src.substr(6, 2) + src.substr(3, 2) + src.substr(0, 2);
    },
    // set type, either numeric or text
    type: 'text'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'smallDateTimeParser',
    is: function(s) {
        // return false so this parser is not auto detected
        return false;
    },
    format: function(s) {
        var src = $.trim(s);
        return src.substr(6, 2) + src.substr(3, 2) + src.substr(0, 2) + src.substr(9);
    },
    // set type, either numeric or text
    type: 'text'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'numberParser',
    is: function(s) {
        return false;
    },
    format: function(s) {
        return $.tablesorter.formatFloat(s.replace(/\./g, '').replace(/,/g,'.').replace(/[^\d\.]/g,''));
    },
    type: 'numeric'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'sortKeyParser',
    is: function(s) {
        return false;
    },
    format: function(s) {
        var src = $.trim(s).toUpperCase();
        var idx1 = src.indexOf('TABLESORTKEY="');
        if (idx1>0) {
            src = src.substring(idx1+14);
            var idx2 = src.indexOf('"');
            src = src.substring(0, idx2);
            return src;
        } else {
            return src;
        }
    },
    type: 'text'
});

$.tablesorter.addParser({
    // set a unique id
    id: 'sortNumberKeyParser',
    is: function(s) {
        return false;
    },
    format: function(s) {
        var src = $.trim(s).toUpperCase();
        var idx1 = src.indexOf('TABLESORTNUMBERKEY="');
        if (idx1>0) {
            src = src.substring(idx1+20);
            var idx2 = src.indexOf('"');
            src = src.substring(0, idx2);
            return $.tablesorter.formatFloat(src.replace(/\./g, '').replace(/,/g,'.').replace(/[^\d\.]/g,''));
        } else {
            return $.tablesorter.formatFloat(src.replace(/\./g, '').replace(/,/g,'.').replace(/[^\d\.]/g,''));
        }
    },
    type: 'numeric'
});




//HoverIntent mandatory functions, and customisable
//-------------------------------------------------
function megaHoverOver() {
    $(this).find(".sub").stop().fadeTo('fast', 1).show();

    //Calculate width of all ul's
    (function($) {
        jQuery.fn.calcSubWidth = function() {
            rowWidth = 0;
            //Calculate row
            $(this).find("ul").each(function() {
                rowWidth += $(this).width();
            });
        };
    })(jQuery);

    if ($(this).find(".row").length > 0) { //If row exists...
        var biggestRow = 0;
        //Calculate each row
        $(this).find(".row").each(function() {
            $(this).calcSubWidth();
            //Find biggest row
            if (rowWidth > biggestRow) {
                biggestRow = rowWidth;
            }
        });
        //Set width
        $(this).find(".sub").css({'width' :biggestRow});
        $(this).find(".row:last").css({'margin':'0'});

    } else { //If row does not exist...

        $(this).calcSubWidth();
        //Set Width
        $(this).find(".sub").css({'width' : rowWidth});

    }
}

function megaHoverOut() {
    $(this).find(".sub").stop().fadeTo('fast', 0, function() {
        $(this).hide();
    });
}

/* jsviews.js v1.0.0-alpha single-file version:
includes JsRender, JsObservable and JsViews  http://github.com/BorisMoore/jsrender and http://jsviews.com/jsviews
informal pre V1.0 commit counter: 60 (Beta Candidate) */
(function(n,t,i){"use strict";function et(n,t){for(var i in t.props)wt.test(i)&&(n[i]=t.props[i])}function ot(n){return n}function ir(n){return n}function dt(n){s._dbgMode=n;pt=n?"Unavailable (nested view): use #getIndex()":"";it("dbg",ci.dbg=tt.dbg=n?ir:ot)}function st(n){this.name=(u.link?"JsViews":"JsRender")+" Error";this.message=n||this.name}function f(n,t){for(var i in t)n[i]=t[i];return n}function d(n){return typeof n=="function"}function gt(n,t,i){return(!o.rTag||n)&&(p=n?n.charAt(0):p,w=n?n.charAt(1):w,h=t?t.charAt(0):h,v=t?t.charAt(1):v,nt=i||nt,n="\\"+p+"(\\"+nt+")?\\"+w,t="\\"+h+"\\"+v,y="(?:(?:(\\w+(?=[\\/\\s\\"+h+"]))|(?:(\\w+)?(:)|(>)|!--((?:[^-]|-(?!-))*)--|(\\*)))\\s*((?:[^\\"+h+"]|\\"+h+"(?!\\"+v+"))*?)",o.rTag=y+")",y=new RegExp(n+y+"(\\/)?|(?:\\/(\\w+)))"+t,"g"),yt=new RegExp("<.*>|([^\\\\]|^)[{}]|"+n+".*"+t)),[p,w,h,v,nt]}function rr(n,t){t||(t=n,n=i);var e,f,o,u,r=this,s=!t||t==="root";if(n){if(u=r.type===t?r:i,!u)if(e=r.views,r._.useKey){for(f in e)if(u=e[f].get(n,t))break}else for(f=0,o=e.length;!u&&f<o;f++)u=e[f].get(n,t)}else if(s)while(r.parent.parent)u=r=r.parent;else while(r&&!u)u=r.type===t?r:i,r=r.parent;return u}function ni(){var n=this.get("item");return n?n.index:i}function ti(){return this.index}function ur(t){var u,e=this,o=e.linkCtx,r=(e.ctx||{})[t];return r===i&&o&&o.ctx&&(r=o.ctx[t]),r===i&&(r=ci[t]),r&&d(r)&&!r._wrp&&(u=function(){return r.apply(!this||this===n?e:this,arguments)},u._wrp=!0,f(u,r)),u||r}function fr(n,t,u,f){var e,s,c=+u===u&&t.tmpl.bnds[u-1],h=t.linkCtx;return f=f!==i&&{props:{},args:[f]},u=f||(c?c(t.data,t,r):u),s=u.args[0],(n||c)&&(e=h&&h.tag,e||(e={_:{inline:!h,bnd:c},tagName:":",cvt:n,flow:!0,tagCtx:u,_is:"tag"},h&&(h.tag=e,e.linkCtx=h,u.ctx=a(u.ctx,h.view.ctx)),o._lnk(e)),e._er=f&&s,et(e,u),u.view=t,e.ctx=u.ctx||{},delete u.ctx,t._.tag=e,s=ht(e,e.convert||n!=="true"&&n)[0],s=c&&t._.onRender?t._.onRender(s,t,c):s,t._.tag=i),s!=i?s:""}function ht(n,t){var r=n.tagCtx,u=r.view,i=r.args;return t=t&&(""+t===t?u.getRsc("converters",t)||c("Unknown converter: '"+t+"'"):t),i=!i.length&&!r.index?[u.data]:t?i.slice():i,t&&(t.depends&&(n.depends=o.getDeps(n.depends,n,t.depends,t)),i[0]=t.apply(n,i)),i}function er(n,t){for(var f,e,u=this;f===i&&u;)e=u.tmpl[n],f=e&&e[t],u=u.parent;return f||r[n][t]}function or(n,t,u,s,h,l){var v,lt,at,ot,p,vt,nt,y,st,rt,it,yt,pt,d,k,ct,wt,g="",w=t.linkCtx||0,ut=t.ctx,bt=u||t.tmpl,ft=+s===s&&bt.bnds[s-1];for(n._is==="tag"&&(v=n,n=v.tagName,s=v.tagCtxs),v=v||w.tag,l=l!==i&&(g+=l,[{props:{},args:[]}]),s=l||(ft?ft(t.data,t,r):s),vt=s.length,p=0;p<vt;p++)p||u&&v||(it=t.getRsc("tags",n)||c("Unknown tag: {{"+n+"}}")),y=s[p],(!w.tag||v._er)&&(rt=y.tmpl,rt=y.content=rt&&bt.tmpls[rt-1],f(y,{tmpl:(v?v:it).template||rt,render:ei,index:p,view:t,ctx:a(y.ctx,ut)})),(u=y.props.tmpl)&&(u=""+u===u?t.getRsc("templates",u)||e(u):u,y.tmpl=u),v||(it._ctr?(v=new it._ctr,yt=!!v.init):o._lnk(v={render:it.render}),v._={inline:!w},w&&(w.tag=v,v.linkCtx=w),(v._.bnd=ft||w.fn)?v._.arrVws={}:v.dataBoundOnly&&c("{^{"+n+"}} tag must be data-bound"),v.tagName=n,v.parent=ot=ut&&ut.tag,v._is="tag",v._def=it,v.tagCtxs=s),y.tag=v,v.dataMap&&v.tagCtxs&&(y.map=v.tagCtxs[p].map),v.flow||(st=y.ctx=y.ctx||{},lt=v.parents=st.parentTags=ut&&a(st.parentTags,ut.parentTags)||{},ot&&(lt[ot.tagName]=ot),lt[v.tagName]=st.tag=v);if(t._.tag=v,!(v._er=l)){for(et(v,s[0]),v.rendering={},p=0;p<vt;p++)y=v.tagCtx=v.tagCtxs[p],ct=y.props,k=ht(v,v.convert),(pt=ct.dataMap||v.dataMap)&&(k.length||ct.dataMap)&&(d=y.map,(!d||d.src!==k[0]||h)&&(d&&d.src&&d.unmap(),d=y.map=pt.map(k[0],ct)),k=[d.tgt]),v.ctx=y.ctx,!p&&yt&&(wt=v.template,v.init(y,w,v.ctx),yt=i,v.template!==wt&&(v._.tmpl=v.template),w&&(w.attr=v.attr=w.attr||v.attr)),nt=i,v.render&&(nt=v.render.apply(v,k)),k=k.length?k:[t],nt=nt!==i?nt:y.render(k[0],!0)||(h?i:""),g=g?g+(nt||""):nt;delete v.rendering}return v.tagCtx=v.tagCtxs[0],v.ctx=v.tagCtx.ctx,v._.inline&&(at=v.attr)&&at!==b&&(g=at==="text"?tt.html(g):""),ft&&t._.onRender?t._.onRender(g,t,ft):g}function g(n,t,i,r,u,f,e,o){var a,h,c,s=this,v=t==="array",l={key:0,useKey:v?0:1,id:""+tr++,onRender:o,bnds:{}};s.data=r;s.tmpl=u;s.content=e;s.views=v?[]:{};s.parent=i;s.type=t||"top";s._=l;s.linked=!!o;i?(a=i.views,h=i._,h.useKey?(a[l.key="_"+h.useKey++]=s,s.index=pt,s.getIndex=ni,c=h.tag,l.bnd=v&&(!c||!!c._.bnd&&c)):a.splice(l.key=s.index=f,0,s),s.ctx=n||i.ctx):s.ctx=n}function sr(n){var i,r,t,u,e,f,s;for(i in k)if(e=k[i],(f=e.compile)&&(r=n[i+"s"]))for(t in r)u=r[t]=f(t,r[t],n),u&&(s=o.onStore[i])&&s(t,u,f)}function hr(n,t,r){var o,u;return d(t)?t={depends:t.depends,render:t}:(t.baseTag&&(t.flow=!!t.flow,t=f(f({},t.baseTag),t)),(u=t.template)!==i&&(t.template=""+u===u?e[u]||e(u):u),t.init!==!1&&(o=t._ctr=function(){},(o.prototype=t).constructor=o)),r&&(t._parentTmpl=r),t}function ii(r,u,f,o){function c(u){if(""+u===u||u.nodeType>0){try{h=u.nodeType>0?u:!yt.test(u)&&t&&t(n.document).find(u)[0]}catch(s){}return h&&(u=e[r=r||h.getAttribute(ft)],u||(r=r||"_"+nr++,h.setAttribute(ft,r),u=e[r]=ii(r,h.innerHTML,f,o)),h=i),u}}var s,h;return u=u||"",s=c(u),o=o||(u.markup?u:{}),o.tmplName=r,f&&(o._parentTmpl=f),!s&&u.markup&&(s=c(u.markup))&&s.fn&&(s.debug!==u.debug||s.allowCode!==u.allowCode)&&(s=s.markup),s!==i?(r&&!f&&(kt[r]=function(){return u.render.apply(u,arguments)}),s.fn||u.fn?s.fn&&(u=r&&r!==s.tmplName?a(o,s):s):(u=ui(s,o),lt(s.replace(pi,"\\$&"),u)),sr(o),u):void 0}function ri(n){function t(t,i){this.tgt=n.getTgt(t,i)}return d(n)&&(n={getTgt:n}),n.baseMap&&(n=f(f({},n.baseMap),n)),n.map=function(n,i){return new t(n,i)},n}function ui(n,t){var i,e=s.wrapMap||{},r=f({markup:n,tmpls:[],links:{},tags:{},bnds:[],_is:"template",render:fi},t);return t.htmlTag||(i=ki.exec(n),r.htmlTag=i?i[1].toLowerCase():""),i=e[r.htmlTag],i&&i!==e.div&&(r.markup=u.trim(r.markup)),r}function cr(n,t){function u(e,s,h){var v,c,l,a;if(e&&typeof e=="object"&&!e.nodeType&&!e.markup&&!e.getTgt){for(l in e)u(l,e[l],s);return r}return s===i&&(s=e,e=i),e&&""+e!==e&&(h=s,s=e,e=i),a=h?h[f]=h[f]||{}:u,c=t.compile,s===null?e&&delete a[e]:(s=c?s=c(e,s,h):s,e&&(a[e]=s)),c&&s&&(s._is=n),s&&(v=o.onStore[n])&&v(e,s,c),s}var f=n+"s";r[f]=u;k[n]=t}function lr(n,t,i){var r=this.jquery&&(this[0]||c('Unknown template: "'+this.selector+'"')),u=r.getAttribute(ft);return fi.call(u?e[u]:e(r),n,t,i)}function ct(n,t,i){if(s._dbgMode)try{return n.fn(t,i,r)}catch(u){return c(u,i)}return n.fn(t,i,r)}function fi(n,t,i,r,f,e){var o=this;return!r&&o.fn._nvw&&!u.isArray(n)?ct(o,n,{tmpl:o}):ei.call(o,n,t,i,r,f,e)}function ei(n,t,r,f,o,s){var y,ut,d,l,nt,tt,it,p,v,rt,w,ft,h,et,c=this,k="";if(!!t===t&&(r=t,t=i),o===!0&&(it=!0,o=0),c.tag?(p=c,c=c.tag,rt=c._,ft=c.tagName,h=rt.tmpl||p.tmpl,et=c.attr&&c.attr!==b,t=a(t,c.ctx),v=p.content,p.props.link===!1&&(t=t||{},t.link=!1),f=f||p.view,n=arguments.length?n:f):h=c,h&&(!f&&n&&n._is==="view"&&(f=n),f&&(v=v||f.content,s=s||f._.onRender,n===f&&(n=f.data),t=a(t,f.ctx)),f&&f.type!=="top"||((t=t||{}).root=n),h.fn||(h=e[h]||e(h)),h)){if(s=(t&&t.link)!==!1&&!et&&s,w=s,s===!0&&(w=i,s=f._.onRender),t=h.helpers?a(h.helpers,t):t,u.isArray(n)&&!r)for(l=it?f:o!==i&&f||new g(t,"array",f,n,h,o,v,s),y=0,ut=n.length;y<ut;y++)d=n[y],nt=new g(t,"item",l,d,h,(o||0)+y,v,s),tt=ct(h,d,nt),k+=l._.onRender?l._.onRender(tt,nt):tt;else(f||!h.fn._nvw)&&(l=it?f:new g(t,ft||"data",f,n,h,o,v,s),rt&&!c.flow&&(l.tag=c)),k+=ct(h,n,l);return w?w(k,l):k}return""}function c(n,t,i){var r=s.onError(n,t,i);if(""+n===n)throw new o.Err(r);return!t.linkCtx&&t.linked?tt.html(r):r}function l(n){c("Syntax error\n"+n)}function lt(n,t,i,r){function k(t){t-=f;t&&h.push(n.substr(f,t).replace(ut,"\\n"))}function c(t){t&&l('Unmatched or missing tag: "{{/'+t+'}}" in template:\n'+n)}function d(e,o,v,y,p,d,nt,tt,it,rt,ft,et){d&&(p=":",y=b);rt=rt||i;var ot=(o||i)&&[[]],ht="",ct="",lt="",at="",vt="",yt="",pt="",bt="",st=!rt&&!p&&!nt;v=v||(it=it||"#data",p);k(et);f=et+e.length;tt?g&&h.push(["*","\n"+it.replace(yi,"$1")+"\n"]):v?(v==="else"&&(bi.test(it)&&l('for "{{else if expr}}" use "{{else expr}}"'),ot=u[7],u[8]=n.substring(u[8],et),u=s.pop(),h=u[2],st=!0),it&&(hi(it.replace(ut," "),ot,t).replace(wi,function(n,t,i,r,u,f,e,o){return e?(ct+=f+",",at+="'"+o+"',"):i?(lt+=r+f+",",yt+=r+"'"+o+"',"):t?pt+=f:(u==="trigger"&&(bt+=f),ht+=r+f+",",vt+=r+"'"+o+"',",w=w||wt.test(u)),""}).slice(0,-1),ot&&ot[0]&&ot.pop()),a=[v,y||!!r||w||"",st&&[],oi(at,vt,yt),oi(ct,ht,lt),pt,bt,ot||0],h.push(a),st&&(s.push(u),u=a,u[8]=f)):ft&&(c(ft!==u[0]&&u[0]!=="else"&&ft),u[8]=n.substring(u[8],et),u=s.pop());c(!u&&ft);h=u[2]}var o,a,w,g=t&&t.allowCode,e=[],f=0,s=[],h=e,u=[,,e];return i&&(n=p+n+v),c(s[0]&&s[0][2].pop()[0]),n.replace(y,d),k(n.length),(f=e[e.length-1])&&c(""+f!==f&&+f[8]===f[8]&&f[0]),i?(o=vt(e,n,i),at(o,e[0][7])):o=vt(e,t),o._nvw&&(o._nvw=!/[~#]/.test(n)),o}function at(n,t){n.deps=[];for(var i in t)i!=="_jsvto"&&t[i].length&&(n.deps=n.deps.concat(t[i]));n.paths=t}function oi(n,t,i){return[n.slice(0,-1),t.slice(0,-1),i.slice(0,-1)]}function si(n,t){return"\n\t"+(t?t+":{":"")+"args:["+n[0]+"]"+(n[1]||!t?",\n\tprops:{"+n[1]+"}":"")+(n[2]?",\n\tctx:{"+n[2]+"}":"")}function hi(n,t,i){function b(b,k,d,g,nt,tt,it,rt,ut,ft,et,ot,st,ht,ct,at,vt,yt,pt,wt){function ni(n,i,u,e,o,h,c,l){if(u&&(r&&(f==="linkTo"&&(s=t._jsvto=t._jsvto||[],s.push(nt)),(!f||a)&&r.push(nt.slice(i.length))),u!==".")){var v=(e?'view.hlp("'+e+'")':o?"view":"data")+(l?(h?"."+h:e?"":o?"":"."+u)+(c||""):(l=e?"":o?h||"":u,""));return v=v+(l?"."+l:""),i+(v.slice(0,9)==="view.data"?v.slice(5):v)}return n}tt=tt||"";d=d||k||ot;nt=nt||ut;ft=ft||vt||"";var kt,dt,bt,gt=wt.length-1;if(!it||o||e)return r&&at&&!o&&!e&&(!f||a||s)&&(kt=p[u],gt>pt-kt&&(kt=wt.slice(kt,pt+1),at=w+":"+kt+" onerror=''"+h,bt=y[at],bt||(y[at]=!0,y[at]=bt=lt(at,i||r,!0),bt.paths.push({_jsvOb:bt})),bt!==!0&&(s||r).push({_jsvOb:bt}))),o?(o=!st,o?b:'"'):e?(e=!ht,e?b:'"'):(d?(u++,p[u]=pt++,d):"")+(yt?u?"":(c=wt.slice(c,pt),f?(f=a=s=!1,"\b"):"\b,")+c+(c=pt+b.length,r&&t.push(r=[]),"\b"):rt?(u&&l(n),r&&t.pop(),f=nt,a=g,c=pt+b.length,g&&(r=t[f]=[]),nt+":"):nt?nt.split("^").join(".").replace(ai,ni)+(ft?(v[++u]=!0,nt.charAt(0)!=="."&&(p[u]=pt),dt?"":ft):tt):tt?tt:ct?(v[u--]=!1,ct)+(ft?(v[++u]=!0,ft):""):et?(v[u]||l(n),","):k?"":(o=st,e=ht,'"'));l(n)}var f,s,a,e,o,r=t&&t[0],c=0,y=i?i.links:r&&(r.links=r.links||{}),v={},p={0:-1},u=0;return(n+(i?" ":"")).replace(/\)\^/g,").").replace(vi,b)}function vt(n,t,r){var p,f,e,c,g,yt,pt,ni,wt,nt,ot,w,s,st,tt,it,v,ht,y,rt,k,ft,bt,d,kt,dt,ct,h,a,lt,gt,o=0,u="",et={},ti=n.length;for(""+t===t?(y=r?'data-link="'+t.replace(ut," ").slice(1,-1)+'"':t,t=0):(y=t.tmplName||"unnamed",t.allowCode&&(et.allowCode=!0),t.debug&&(et.debug=!0),w=t.bnds,ht=t.tmpls),p=0;p<ti;p++)if(f=n[p],""+f===f)u+='\n+"'+f+'"';else if(e=f[0],e==="*")u+=";\n"+f[1]+"\nret=ret";else{if(c=f[1],ft=f[2],g=si(f[3],"params")+"},"+si(st=f[4]),a=f[5],gt=f[6],bt=f[8],(dt=e==="else")||(o=0,w&&(s=f[7])&&(o=w.push(s))),(ct=e===":")?c&&(e=c===b?">":c+e):(ft&&(rt=ui(bt,et),rt.tmplName=y+"/"+e,vt(ft,rt),ht.push(rt)),dt||(k=e,kt=u,u=""),d=n[p+1],d=d&&d[0]==="else"),lt=a?";\ntry{\nret+=":"\n+",tt="",it="",ct&&(s||gt||c&&c!==b)){if(h="return {"+g+"};",v='c("'+c+'",view,',h=new Function("data,view,j,u"," // "+y+" "+o+" "+e+"\n"+h),h._er=a,tt=v+o+",",it=")",h._tag=e,r)return h;at(h,s);ot=!0}if(u+=ct?(r?(a?"\ntry{\n":"")+"return ":lt)+(ot?(ot=i,nt=wt=!0,v+(s?(w[o-1]=h,o):"{"+g+"}")+")"):e===">"?(pt=!0,"h("+st[0]+")"):(ni=!0,"((v="+st[0]+')!=null?v:"")')):(nt=yt=!0,"\n{view:view,tmpl:"+(ft?ht.length:"0")+","+g+"},"),k&&!d){if(u="["+u.slice(0,-1)+"]",v='t("'+k+'",view,this,',r||s){if(u=new Function("data,view,j,u"," // "+y+" "+o+" "+k+"\nreturn "+u+";"),u._er=a,u._tag=e,s&&at(w[o-1]=u,s),r)return u;tt=v+o+",undefined,";it=")"}u=kt+lt+v+(o||u)+")";s=0;k=0}a&&(nt=!0,u+=";\n}catch(e){ret"+(r?"urn ":"+=")+tt+"j._err(e,view,"+a+")"+it+";}\n"+(r?"":"ret=ret"))}u="// "+y+"\nvar v"+(yt?",t=j._tag":"")+(wt?",c=j._cnvt":"")+(pt?",h=j.converters.html":"")+(r?";\n":',ret=""\n')+(et.debug?"debugger;":"")+u+(r?"\n":";\nreturn ret;");try{u=new Function("data,view,j,u",u)}catch(ii){l("Compiled template code:\n\n"+u+'\n: "'+ii.message+'"')}return t&&(t.fn=u),nt||(u._nvw=!0),u}function a(n,t){return n&&n!==t?t?f(f({},t),n):n:t&&f({},t)}function ar(n){return bt[n]||(bt[n]="&#"+n.charCodeAt(0)+";")}function vr(n){var i,t,r=[];if(typeof n=="object")for(i in n)t=n[i],t&&t.toJSON&&!t.toJSON()||d(t)||r.push({key:i,prop:t});return r}function li(n){return n!=null?di.test(n)&&(""+n).replace(gi,ar)||n:""}if((!t||!t.render)&&!n.jsviews){var u,rt,y,yt,pt,p="{",w="{",h="}",v="}",nt="^",ai=/^(!*?)(?:null|true|false|\d[\d.]*|([\w$]+|\.|~([\w$]+)|#(view|([\w$]+))?)([\w$.^]*?)(?:[.[^]([\w$]+)\]?)?)$/g,vi=/(\()(?=\s*\()|(?:([([])\s*)?(?:(\^?)(!*?[#~]?[\w$.^]+)?\s*((\+\+|--)|\+|-|&&|\|\||===|!==|==|!=|<=|>=|[<>%*:?\/]|(=))\s*|(!*?[#~]?[\w$.^]+)([([])?)|(,\s*)|(\(?)\\?(?:(')|("))|(?:\s*(([)\]])(?=\s*\.|\s*\^|\s*$)|[)\]])([([]?))|(\s+)/g,ut=/[ \t]*(\r\n|\n|\r)/g,yi=/\\(['"])/g,pi=/['"\\]/g,wi=/(?:\x08|^)(onerror:)?(?:(~?)(([\w$]+):)?([^\x08]+))\x08(,)?([^\x08]+)/gi,bi=/^if\s/,ki=/<(\w+)[>\s]/,di=/[\x00`><\"'&]/,wt=/^on[A-Z]|^convert(Back)?$/,gi=/[\x00`><"'&]/g,nr=0,tr=0,bt={"&":"&amp;","<":"&lt;",">":"&gt;","\x00":"&#0;","'":"&#39;",'"':"&#34;","`":"&#96;"},b="html",ft="data-jsv-tmpl",kt={},k={template:{compile:ii},tag:{compile:hr},helper:{},converter:{}},r={jsviews:"v1.0.0-beta",settings:function(n){f(s,n);dt(s._dbgMode);s.jsv&&s.jsv()},sub:{View:g,Err:st,tmplFn:lt,cvt:ht,parse:hi,extend:f,syntaxErr:l,onStore:{},_lnk:ot,_ths:et},map:ri,_cnvt:fr,_tag:or,_err:c};(st.prototype=new Error).constructor=st;ni.depends=function(){return[this.get("item"),"index"]};ti.depends=function(){return["index"]};g.prototype={get:rr,getIndex:ti,getRsc:er,hlp:ur,_is:"view"};for(rt in k)cr(rt,k[rt]);var e=r.templates,tt=r.converters,ci=r.helpers,it=r.tags,o=r.sub,s=r.settings;t?(u=t,u.fn.render=lr,u.observable&&(f(o,u.views.sub),r.map=u.views.map)):(u=n.jsviews={},u.isArray=Array&&Array.isArray||function(n){return Object.prototype.toString.call(n)==="[object Array]"});u.render=kt;u.views=r;u.templates=e=r.templates;s({debugMode:dt,delimiters:gt,onError:function(n,t,r){return t&&(n=r===i?"{Error: "+n+"}":d(r)?r(n,t):r),n==i?"":n},_dbgMode:!0});it({"else":function(){},"if":{render:function(n){var t=this;return t.rendering.done||!n&&(arguments.length||!t.tagCtx.index)?"":(t.rendering.done=!0,t.selected=t.tagCtx.index,t.tagCtx.render(t.tagCtx.view,!0))},onUpdate:function(n,t,i){for(var r,f,u=0;(r=this.tagCtxs[u])&&r.args.length;u++)if(r=r.args[0],f=!r!=!i[u].args[0],!this.convert&&!!r||f)return f;return!1},flow:!0},"for":{render:function(n){var f,t=this,r=t.tagCtx,e="",o=0;return t.rendering.done||((f=!arguments.length)&&(n=r.view.data),n!==i&&(e+=r.render(n,f),o+=u.isArray(n)?n.length:1),(t.rendering.done=o)&&(t.selected=r.index)),e},flow:!0},include:{flow:!0},"*":{render:ot,flow:!0}});it("props",{baseTag:it["for"],dataMap:ri(vr)});tt({html:li,attr:li,url:function(n){return n!=i?encodeURI(""+n):n===null?n:""}});gt()}})(this,this.jQuery),function(n,t,i){"use strict";function r(n){return u(n)?new b(n):new w(n)}function w(n){return this._data=n,this}function b(n){return this._data=n,this}function at(n){return u(n)?[n]:n}function ut(n,t){n=u(n)?n:[n];for(var i,e=t,o=e,h=n.length,r=[],s=0;s<h;s++){if(i=n[s],f(i)){r=r.concat(ut(i.call(t,t),t));continue}else if(""+i!==i){t=o=i;o!==e&&r.push(e=o);continue}o!==e&&r.push(e=o);r.push(i)}return r}function ft(n,t){var r,i;for(r in n){i=!0;break}i||delete a[t]}function k(n,t){if(!(n.data&&n.data.off)){var v,y,p,w,b,c=t.oldValue,a=t.value,r=n.data,f=r.observeAll,o=!r.cb.noArray,h=r.paths;n.type===l?(r.cb.array||r.cb).call(r,n,t):(r.prop===t.path||r.prop==="*")&&(w=typeof c===s&&(h[0]||o&&u(c)),b=typeof a===s&&(h[0]||o&&u(a)),f?(v=f._path+"."+t.path,y=f.filter,p=[n.target].concat(f.parents()),w&&e(o,f.ns,[c],h,r.cb,!0,y,[p],v),b&&e(o,f.ns,[a],h,r.cb,i,y,[p],v)):(w&&e(o,[c],h,r.cb,!0),b&&e(o,[a],h,r.cb)),r.cb(n,t))}}function v(){function gt(i,r,u,f){var s,e,y=it(n),p=at(n);if(i=st?i+"."+st:i,g||f)y&&t(p).off(i,k);else{if(h=y&&t._data(n))for(h=h&&h.events,h=h&&h[u?l:c],dt=h&&h.length;dt--;)if((nt=h[dt].data)&&nt.cb._cId===o._cId&&nt.ns===st){if(u)return;(r==="*"&&nt.prop!==r||nt.prop===w)&&t(n).off(i,k)}e=u?{}:{fullPath:v,paths:r?[r]:[],prop:w};e.ns=st;e.cb=o;ot&&(e.observeAll={_path:ot,path:function(){return s=kt.length,ot.replace(/[[.]/g,function(n){return s--,n==="["?"["+t.inArray(kt[s-1],kt[s]):"."})},parents:function(){return kt},filter:oi,ns:st});t(p).on(i,null,e,k);ti&&((a[o._cId]=ti)[t.data(n,"obId")||t.data(n,"obId",lt++)]=n)}}function vi(n,t){n._ob=yt(n,rt);var i=rt;return function(r,f){var h=n._ob,c=t.length;typeof h===s&&(ni(h,!0),(c||vt&&u(h))&&e(vt,[h],t,o,yt,!0));h=n._ob=yt(n,i);typeof h===s&&(ni(h),(c||vt&&u(h))&&e(vt,[h],t,o,yt,[i]));o(r,f)}}function ni(t,f,e,s){if(vt){var h=n,c=ot;n=t;s&&(n=t[s],ot+="."+s);oi&&n&&(n=r._fltr(ot,n,s?[t].concat(kt):kt,oi));n&&(e||u(n))&&gt(l+".observe"+(o?".obs"+(bt=o._cId=o._cId||p++):""),i,!0,f);n=h;ot=c}}var wt,fi,si,et,w,v,ei,g,o,bt,dt,nt,h,yt,hi,ti,ii,ri,kt,ot,oi,st,ci,li,vt=this!=!1,ai=!0,ui=ct,b=Array.apply(0,arguments),d=b.pop(),rt=b.shift(),pt=rt,n=pt,tt=b.length;for(rt+""===rt&&vt&&(st=rt,rt=n=pt=b.shift(),tt--),f(d)?o=d:(d+""===d&&(ot=d,kt=b.pop(),oi=b.pop(),d=b.pop(),tt=tt-3),d===!0?g=d:d&&(rt=d,ai=i),d=b[tt-1],(tt&&d===i||f(d))&&(o=b.pop(),tt--)),tt&&f(b[tt-1])&&(yt=o,o=b.pop(),tt--),ui+=g?o?".obs"+o._cId:"":".obs"+(bt=o._cId=o._cId||p++),g||(ti=a[bt]=a[bt]||{}),ci=st&&st.match(ht)||[""],li=ci.length;li--;)for(st=ci[li],u(pt)?ni(pt,g,!0):g&&tt===0&&pt&&gt(ui,""),ii=0,wt=0;wt<tt;wt++)if(v=b[wt],v!==""){if(n=pt,""+v===v){if(et=v.split("^"),et[1]&&(ii=et[0].split(".").length,v=et.join("."),ii=v.split(".").length-ii),yt&&(hi=yt(v,pt))){tt+=hi.length-1;y.apply(b,[wt--,1].concat(hi));continue}et=v.split(".")}else ai&&!f(v)&&(v&&v._jsvOb&&(ri=vi(v,b.slice(wt+1)),ri.noArray=!vt,ri._cId=o._cId,e(vt,[rt],b.slice(0,wt),ri,yt),ri=i,v=v._ob),n=v),pt=v,et=[pt];while(n&&(w=et.shift())!==i)if(typeof n===s){if(""+w===w){if(w==="")continue;if(et.length<ii+1&&!n.nodeType){if(!g&&(h=it(n)&&t._data(n))){for(h=h.events,h=h&&h[c],dt=h&&h.length,si=0;dt--;)nt=h[dt].data,nt&&nt.cb===o&&nt.ns===st&&(nt.prop===w||nt.prop==="*")&&((fi=et.join("."))&&nt.paths.push(fi),si++);if(si){n=n[w];continue}}if(w==="*"){!g&&h&&h.length&&gt(ui,"",!1,!0);f(n)?(ei=n.depends)&&e(vt,[ei],o,g||rt):gt(ui,"");for(fi in n)ni(n,g,i,fi);break}else w&&gt(ui+"."+w,et.join("^"))}ot&&(ot+="."+w);w=n[w]}if(f(w)){(ei=w.depends)&&e(vt,[n],ut(ei,n),o,yt,g||[rt]);break}n=w}ni(n,g)}return bt&&ft(ti,bt),{cbId:bt,bnd:ti}}function vt(){return[].push.call(arguments,!0),v.apply(this,arguments)}function e(){var n=[].concat.apply([],arguments);return v.apply(n.shift(),n)}function d(n,t,i,r){n+""!==n&&(i=t,t=n,n="");ot(n,this._data,t,i,[],"root",r)}function et(n,t,i){d.call(this,n,t,i,!0)}function ot(n,t,f,e,o,h,c){function y(n,t){for(l=n.length,a=[[n]].concat(a),b=h+"[]";l--;)k(n,l,t,1)}function k(t,u,o,s){var h;u!==tt&&(h=r._fltr(b,t[u],a,e))&&ot(n,h,f,e||(s?i:0),a.slice(),b,o)}function d(n,t){var i=o;h=n.data.observeAll._path;a=[n.target].concat(o);switch(t.change){case"insert":y(t.items);break;case"remove":y(t.items,!0);break;case"refresh":y(t.oldItems,!0);y(n.target);break;case"set":b=h+"."+t.path;k(t,"oldValue",!0);k(t,"value")}f.apply(this,arguments);o=i}var l,w,b,a;if(typeof t===s)if(w=u(t)?"":"*",f?(w||e!==0)&&(d._cId=f._cId=f._cId||p++,v(n,t,w,d,c,e,o,h)):v(n,t,w,i,c,e,o,h),w){a=[t].concat(o);for(l in t)b=h+"."+l,k(t,l,c)}else y(t,c)}function st(n){return n.indexOf(".")<0&&n.indexOf("[")<0}if(!t)throw"jsViews/jsObservable require jQuery";if(!t.observable){var g=t.views=t.views||{jsviews:"v1.0.0-alpha",sub:{}},o=g.sub,nt=t.event.special,y=[].splice,u=t.isArray,tt=t.expando,s="object",h=parseInt,ht=/\S+/g,c=o.propChng=o.propChng||"propertyChange",l=o.arrChng=o.arrChng||"arrayChange",a=o._cbBnds=o._cbBnds||{},ct=c+".observe",f=t.isFunction,lt=1,p=1,it=t.hasData,rt={};o.getDeps=function(){var n=arguments;return function(){for(var i,t,r=[],u=n.length;u--;)i=n[u--],t=n[u],t&&(r=r.concat(f(t)?t(i,i):t));return r}};t.observable=r;r._fltr=function(n,t,i,r){if(r&&f(r)?r(n,t,i):!0)return t=f(t)?t.set&&t.call(i[0]):t,typeof t===s&&t};r.Object=w;r.Array=b;t.observe=r.observe=v;t.unobserve=r.unobserve=vt;r._apply=e;w.prototype={_data:null,observeAll:d,unobserveAll:et,data:function(){return this._data},setProperty:function(n,t,r){var f,h,s,o=this,e=o._data;if(n=n||"",e)if(u(n))for(f=n.length;f--;)h=n[f],o.setProperty(h.name,h.value,r===i||r);else if(""+n!==n)for(f in n)o.setProperty(f,n[f],t);else if(n!==tt){for(s=n.split(".");e&&s.length>1;)e=e[s.shift()];e&&o._setProperty(e,s[0],t,r)}return o},removeProperty:function(n){return this.setProperty(n,rt),this},_setProperty:function(n,t,r,u){var o,s,h,e=t?n[t]:n;f(e)&&e.set&&(s=e,o=e.set===!0?e:e.set,e=e.call(n));(e!==r||u&&e!=r)&&(!(e instanceof Date)||e>r||e<r)&&(o?(o.call(n,r),r=s.call(n)):(h=r===rt)?(delete n[t],r=i):t&&(n[t]=r),this._trigger(n,{change:"set",path:t,value:r,oldValue:e,remove:h}))},_trigger:function(n,i){t(n).triggerHandler(c,i)}};b.prototype={_data:null,observeAll:d,unobserveAll:et,data:function(){return this._data},insert:function(n,t){var i=this._data;return arguments.length===1&&(t=n,n=i.length),n=h(n),n>-1&&n<=i.length&&(t=u(t)?t:[t],t.length&&this._insert(n,t)),this},_insert:function(n,t){var i=this._data,r=i.length;y.apply(i,[n,0].concat(t));this._trigger({change:"insert",index:n,items:t},r)},remove:function(n,t){var r,u=this._data;return n===i&&(n=u.length-1),n=h(n),t=t?h(t):t===0?0:1,t>-1&&n>-1&&(r=u.slice(n,n+t),t=r.length,t&&this._remove(n,t,r)),this},_remove:function(n,t,i){var r=this._data,u=r.length;r.splice(n,t);this._trigger({change:"remove",index:n,items:i},u)},move:function(n,t,i){if(i=i?h(i):i===0?0:1,n=h(n),t=h(t),i>0&&n>-1&&t>-1&&n!==t){var r=this._data.slice(n,n+i);i=r.length;i&&this._move(n,t,i,r)}return this},_move:function(n,t,i,r){var u=this._data,f=u.length;u.splice(n,i);u.splice.apply(u,[t,0].concat(r));this._trigger({change:"move",oldIndex:n,index:t,items:r},f)},refresh:function(n){var t=this._data.slice();return this._refresh(t,n),this},_refresh:function(n,t){var i=this._data,r=i.length;y.apply(i,[0,i.length].concat(t));this._trigger({change:"refresh",oldItems:n},r)},_trigger:function(n,i){var r=this._data,u=r.length,f=t([r]);f.triggerHandler(l,n);u!==i&&f.triggerHandler(c,{change:"set",path:"length",value:u,oldValue:i})}};nt[c]=nt[l]={remove:function(n){var r,u,f,e,o,i=n.data;if(i&&(i.off=!0,i=i.cb)&&(r=a[i._cId])){for(f=t._data(this).events[n.type],e=f.length;e--&&!u;)u=(o=f[e].data)&&o.cb===i;u||(delete r[t.data(this,"obId")],ft(r,i._cId))}}};g.map=function(n){function u(t,u,f){var o,e=this;this.src&&this.unmap();typeof t=="object"&&(e.src=t,e.tgt=f||e.tgt||[],e.options=u||e.options,e.update(),n.obsSrc&&r(e.src).observeAll(e.obs=function(t,r){o||(o=!0,n.obsSrc(e,t,r),o=i)},e.srcFlt),n.obsTgt&&r(e.tgt).observeAll(e.obt=function(t,r){o||(o=!0,n.obsTgt(e,t,r),o=i)},e.tgtFlt))}return f(n)&&(n={getTgt:n}),n.baseMap&&(n=t.extend({},n.baseMap,n)),n.map=function(n,t,i){return new u(n,t,i)},(u.prototype={srcFlt:n.srcFlt||st,tgtFlt:n.tgtFlt||st,update:function(t){var i=this;r(i.tgt).refresh(n.getTgt(i.src,i.options=t||i.options))},unmap:function(){var n=this;n.src&&(n.obs&&r(n.src).unobserveAll(n.obs,n.srcFlt),n.obt&&r(n.tgt).unobserveAll(n.obt,n.tgtFlt),n.src=i)},map:u,_def:n}).constructor=u,n}}}(this,this.jQuery),function(n,t,i){"use strict";function nt(n,r,u){var b,tt,a,o,k,v,s,d,h,y,it,g,nt,e,l,p,w=n.target,rt=w._jsvBnd,ut=/&(\d+)\+?/g;if(rt)while(y=ut.exec(rt))if((y=c[y[1]])&&(l=y.to)){if(o=y.linkCtx,h=o.view,e=o.tag,d=t(w),g=h.hlp(ti),nt=h.hlp(ii),a=at(w),b=fi[a],u===i&&(u=et(a)?a(w):b?d[b]():d.attr(a)),v=l[1],l=l[0],v&&(k=et(v)?v:h.getRsc("converters",v)),k&&(u=k.call(e,u)),it=h.linkCtx,h.linkCtx=o,p={change:"change",oldValue:o._val,value:u},(!g||!(tt=g.call(o,n,p)===!1))&&(!e||!e.onBeforeChange||!(tt=e.onBeforeChange(n,p)===!1))&&u!==i&&(s=l[0],u!==i&&s)){if(s=s._jsvOb?s._ob:s,e&&(e._.chging=!0),f(s).setProperty(l[2]||l[1],u),nt&&nt.call(o,n,p),e){if(e.onAfterChange)e.onAfterChange(n,p);delete e._.chging}o._val=u}h.linkCtx=it}}function pu(n,t,r){var h,o,k,b,a,y,e=this,f=e.tag,l=e.data,p=e.elem,c=e.convert,nt=p.parentNode,s=e.view,g=s.linkCtx,w=s.hlp(ti);if(s.linkCtx=e,nt&&(!w||!(t&&w.call(e,n,t)===!1))&&!(t&&n.data.prop!=="*"&&n.data.prop!==t.path)){if(t&&(e.eventArgs=t),t||e._initVal){if(delete e._initVal,r._er)try{o=r(l,s)}catch(tt){a=r._er;y=d(tt,s,new Function("data,view","return "+a+";")(l,s));o=[{props:{},args:[y]}]}else o=r(l,s,u);if(h=wu(o,e,f=e.tag,e.attr||at(p,!0,c!==i)),f){if(b=a||f._er,o=o[0]?o:[o],k=!b&&t&&f.onUpdate&&f.onUpdate(n,t,o)===!1,dr(f,o,b),k||h===ct){h===v&&f.onBeforeLink&&f.onBeforeLink();wt(f,f.tagCtx);ai(e,l,p);s.linkCtx=g;return}if(f._.chging)return;o=f.tagName===":"?u._cnvt(f.cvt,s,o[0]):u._tag(f,s,s.tmpl,o,!0,y)}else r._tag&&(c=c===""?lt:c,o=c?u._cnvt(c,s,o[0]||o):u._tag(r._tag,s,s.tmpl,o,!0,y),f=e.tag,h=e.attr||h);lr(o,e,h,f)&&t&&(w=s.hlp(ii))&&w.call(e,n,t);f&&(f._er=a,wt(f,f.tagCtx))}ai(e,l,p);s.linkCtx=g}}function wu(n,r,u,f){var e,h,c,o,s=u&&u.parentElem||r.elem;if(n!==i){if(o=t(s),f=u&&u.attr||f,et(n)&&d(r.expr+": missing parens"),c=/^css-/.test(f)&&f.slice(4))e=t.style(s,c),+n===n&&(e=parseInt(e));else if(f!=="link"){if(f==="value")s.type===ri&&(e=o.prop(f=g));else if(f===ht)if(s.value===""+n)e=o.prop(g);else return f;e===i&&(h=fi[f],e=h?o[h]():o.attr(f))}r._val=e}return f}function y(n,t){n._df=t;n[(t?"set":"remove")+"Attribute"](rr,"")}function lr(r,u,f,o){var ot,h,c,tt,st,d,a,nt,p,b,k,it,rt,ut=r!==i,lt=u.data,s=o&&o.parentElem||u.elem,ft=t(s),w=u.view,et=u._val,at=w.linkCtx,l=o||f===v;if(o&&(o.parentElem=o.parentElem||u.expr||o._elCnt?s:s.parentNode,h=o._prv,c=o._nxt),!ut){f===v&&o&&o.onBeforeLink&&o.onBeforeLink();return}if(f==="visible"&&(f="css-display"),/^css-/.test(f))u.attr==="visible"&&(rt=(s.currentStyle||cr.call(n,s,"")).display,r?(r=s._jsvd||rt,r!==ct||(r=er[it=s.nodeName])||(k=e.createElement(it),e.body.appendChild(k),r=er[it]=(k.currentStyle||cr.call(n,k,"")).display,e.body.removeChild(k))):(s._jsvd=rt,r=ct)),(l=l||et!==r)&&t.style(s,f.slice(4),r);else if(f!=="link"){if(f===g)d=!0,r=r&&r!=="false";else if(f===ht)if(s.value===""+r)r=d=!0,f=g;else{ai(u,lt,s);return}else(f==="selected"||f==="disabled"||f==="multiple"||f==="readonly")&&(r=r&&r!=="false"?f:null);(ot=fi[f])?f===v?(w.linkCtx=u,o&&o._.inline?(st=o.nodes(!0),o._elCnt&&(h&&h!==c?wi(h,c,s,o._tgId,"^",!0):(a=s._df)&&(nt=o._tgId+"^",p=a.indexOf("#"+nt)+1,b=a.indexOf("/"+nt),p&&b>0&&(p+=nt.length,b>p&&(y(s,a.slice(0,p)+a.slice(b)),nu(a.slice(p,b))))),h=h?h.previousSibling:c?c.previousSibling:s.lastChild),t(st).remove(),o&&o.onBeforeLink&&o.onBeforeLink(),tt=w.link(w.data,s,h,c,r,o&&{tag:o._tgId,lazyLink:o.tagCtx.props.lazyLink})):(ut&&ft.empty(),o&&o.onBeforeLink&&o.onBeforeLink(),ut&&(tt=w.link(lt,s,h,c,r,o&&{tag:o._tgId}))),w.linkCtx=at):(l=l||et!==r)&&(f==="text"&&s.children&&!s.children[0]?s.textContent!==i?s.textContent=r:s.innerText=r===null?"":r:ft[ot](r)):(l=l||et!==r)&&ft[d?"prop":"attr"](f,r===i&&!d?null:r);u._val=r}return tt||l}function ar(n,t){var i=this,r=i.hlp(ti),u=i.hlp(ii);if(!r||r.call(this,n,t)!==!1){if(t){var o=t.change,f=t.index,e=t.items;switch(o){case"insert":i.addViews(f,e);break;case"remove":i.removeViews(f,e.length);break;case"move":i.refresh();break;case"refresh":i.refresh()}}u&&u.call(this,n,t)}}function li(n){var u,f,o=n.type,e=n.data,r=n._.bnd;if(!n._.useKey&&r)if((f=n._.bndArr)&&(t([f[1]]).off(ni,f[0]),n._.bndArr=i),r!==!!r&&r._.inline)o?r._.arrVws[n._.id]=n:delete r._.arrVws[n._.id];else if(o&&e){u=function(t){t.data&&t.data.off||ar.apply(n,arguments)};t([e]).on(ni,u);n._.bndArr=[u,e]}}function at(n,t,i){var u=n.nodeName.toLowerCase(),r=a.merge[u]||n.contentEditable===lt&&{to:v,from:v};return r?t?u==="input"&&n.type===ht?ht:r.to:r.from:t?i?"text":v:""}function vr(n,r,u,f,e,o,s){var p,c,v,w,b,l=n.parentElem,h=n._prv,a=n._nxt,y=n._elCnt;if(h&&h.parentNode!==l&&d("Missing parentNode"),s){w=n.nodes();y&&h&&h!==a&&wi(h,a,l,n._.id,"_",!0);n.removeViews(i,i,!0);c=a;y&&(h=h?h.previousSibling:a?a.previousSibling:l.lastChild);t(w).remove();for(b in n._.bnds)kt(b)}else{if(r){if(v=f[r-1],!v)return!1;h=v._nxt}y?(c=h,h=c?c.previousSibling:l.lastChild):c=h.nextSibling}p=u.render(e,o,n._.useKey&&s,n,s||r,!0);n.link(e,l,h,c,p,v)}function vt(n,t,r){var u,f,e;return r?(e="^`",f=t._.tag,u=f._tgId,u||(c[u=or++]=f,f._tgId=""+u)):(e="_`",w[u=t._.id]=t),"#"+u+e+(n!=i?n:"")+"/"+u+e}function ai(n,t,r){var s,a,e,u=n.tag,v=n.convertBack,h=[],l=n._bndId||""+or++,y=n._hdlr;if(delete n._bndId,u&&(h=u.depends||h,h=et(h)?u.depends(u):h,e=u.linkedElem),(!n._depends||""+n._depends!=""+h)&&(n._depends&&f._apply(!1,[t],n._depends,y,!0),s=f._apply(!1,[t],n.fn.deps,h,y,n._ctxCb),s.elem=r,s.linkCtx=n,s._tgId=l,r._jsvBnd=r._jsvBnd||"",r._jsvBnd+="&"+l,n._depends=h,n.view._.bnds[l]=l,c[l]=s,e&&(s.to=[[],v]),(e||v!==i)&&kr(s,u&&u.convertBack||v),u)){if(u.onAfterBind)u.onAfterBind(s);u.flow||u._.inline||(r.setAttribute(o,(r.getAttribute(o)||"")+"#"+l+"^/"+l+"^"),u._tgId=""+l)}if(e&&e[0])for(u._.radio&&(e=e.children("input[type=radio]")),a=e.length;a--;)e[a]._jsvBnd=e[a]._jsvBnd||r._jsvBnd+"+",e[a]._jsvLkEl=u}function yr(n,t,i,r,u,f,e){return vi(this,n,t,i,r,u,f,e)}function vi(n,r,u,f,o,h,c,a){if(n&&r){if(r=r.jquery?r:t(r),!tt){tt=e.body;t(tt).on(nr,nt).on("blur","[contenteditable]",nt)}for(var b,g,rt,d,p,k,v,ut,ft=vt,ot=f&&f.target==="replace",et=r.length;et--;)if(v=r[et],""+n===n)yt(n,v,it(v),i,!0,u,f);else{if(h=h||it(v),n.markup!==i)h.link===!1&&(f=f||{},f.link=ft=!1),ot&&(k=v.parentNode),rt=n.render(u,f,o,h,i,ft),k?(c=v.previousSibling,a=v.nextSibling,t.cleanData([v],!0),k.removeChild(v),v=k):(c=a=i,t(v).empty());else if(n===!0&&h===l)ut={lnk:1};else break;if(v._df&&!a){for(d=s(v._df,!0,sr),b=0,g=d.length;b<g;b++)p=d[b],(p=w[p.id])&&p.data!==i&&p.parent.removeViews(p._.key,i,!0);y(v)}h.link(u,v,c,a,rt,ut,f)}}return r}function bu(n,r,u,f,l,v,b,k){function pu(n,t,r,u,f,e,s,h,c,a,v,y,p){var w,b="";return p?(ui=0,n):(nt=h||c||"",u=u||a,r=r||y,ai&&!r&&(u||nt||e)&&(ai=i,ut=dt.shift()),u=u||r,u&&(ui=0,ai=i,kr&&(r||y?ci[ut]||/;svg;|;math;/.test(";"+dt.join(";")+";")||(w="'<"+ut+".../"):ci[u]?w="'<\/"+u:dt.length&&u===ut||(w="Mismatch: '<\/"+u),w&&oi(w+">' in:\n"+l)),sr=ft,ut=dt.shift(),ft=hi[ut],a=a?"<\/"+a+">":"",sr&&(ht+=at,at="",ft?ht+="-":(b=a+ir+"@"+ht+tr+(v||""),ht=au.shift()))),ft?(e?at+=e:t=a||y||"",nt&&(t+=nt,at&&(t+=" "+o+'="'+at+'"',at=""))):t=e?t+b+f+ir+e+tr+s+nt:b||n,ui&&e&&oi(" No {^{ tags within elem markup ("+ui+' ). Use data-link="..."'),nt&&(ui=nt,dt.unshift(ut),ut=nt.slice(1),dt[0]&&dt[0]===hu[ut]&&d("Parent of <tr> must be <tbody>"),ai=ci[ut],(ft=hi[ut])&&!sr&&(au.unshift(ht),ht=""),sr=ft,ht&&ft&&(ht+="+")),t)}function wi(n,t){var o,l,u,e,f,a,s,h=[];if(n){for(n._tkns.charAt(0)==="@"&&(t=g.previousSibling,g.parentNode.removeChild(g),g=i),bt=n.length;bt--;){if(rt=n[bt],u=rt.ch,o=rt.path)for(et=o.length-1;l=o.charAt(et--);)l==="+"?o.charAt(et)==="-"?(et--,t=t.previousSibling):t=t.parentNode:t=t.lastChild;u==="^"?(nt=c[f=rt.id])&&(s=t&&(!g||g.parentNode!==t),(!g||s)&&(nt.parentElem=t),rt.elCnt&&s&&y(t,(rt.open?"#":"/")+f+u+(t._df||"")),h.push([s?null:g,rt])):(tt=w[f=rt.id])&&(tt.parentElem||(tt.parentElem=t||g&&g.parentNode||r,tt._.onRender=vt,tt._.onArrayChange=ar,li(tt)),e=tt.parentElem,rt.open?(tt._elCnt=rt.elCnt,t&&!g?y(t,"#"+f+u+(t._df||"")):(tt._prv||y(e,pt(e._df,"#"+f+u)),tt._prv=g)):(t&&(!g||g.parentNode!==t)?(y(t,"/"+f+u+(t._df||"")),tt._nxt=i):g&&(tt._nxt||y(e,pt(e._df,"/"+f+u)),tt._nxt=g),fi=tt.linkCtx,(a=tt.ctx&&tt.ctx.onAfterCreate||wu)&&a.call(fi,tt)))}for(bt=h.length;bt--;)pi.push(h[bt])}return!n||n.elCnt}function tu(n){var t,i;if(n)for(bt=n.length,et=0;et<bt;et++)if(rt=n[et],i=nt=c[rt.id].linkCtx.tag,!nt.flow){if(!vr){for(t=1;i=i.parent;)t++;er=er||t}(vr||t===er)&&(!yr||nt.tagName===yr)&&ru.push(nt)}}function iu(){var h,a,e="",y={},v=di+(st?",["+rr+"]":"");for(bi=si?r.querySelectorAll(v):t(v,r).get(),gt=bi.length,u&&u.innerHTML&&(gi=si?u.querySelectorAll(v):t(v,u).get(),u=gi.length?gi[gi.length-1]:u),er=0,ct=0;ct<gt;ct++)if(g=bi[ct],u&&!su)su=g===u;else if(f&&g===f){st&&(e+=ot(g));break}else if(g.parentNode)if(st){if(e+=ot(g),g._df){for(h=ct+1;h<gt&&g.contains(bi[h]);)h++;y[h-1]=g._df}y[ct]&&(e+=y[ct]||"")}else gr&&(rt=s(g,i,hr))&&(rt=rt[0])&&(yi=yi?rt.id!==yi&&yi:rt.open&&rt.id),!yi&&yu(s(g))&&g.getAttribute(p)&&pi.push([g]);if(st&&(e+=r._df||"",(a=e.indexOf("#"+st.id)+1)&&(e=e.slice(a+st.id.length)),a=e.indexOf("/"+st.id),a+1&&(e=e.slice(0,a)),tu(s(e,i,vu))),l===i&&r.getAttribute(p)&&pi.push([r]),wr(u,ft),wr(f,ft),st){kt&&kt.resolve();return}for(ft&&ht+at&&(g=f,ht&&(f?wi(s(ht+"+",!0),f):wi(s(ht,!0),r)),wi(s(at,!0),r),f&&(e=f.getAttribute(o),(gt=e.indexOf(cr)+1)&&(e=e.slice(gt+cr.length-1)),f.setAttribute(o,at+e))),gt=pi.length,ct=0;ct<gt;ct++)g=pi[ct],ni=g[1],g=g[0],ni?(nt=c[ni.id])&&((fi=nt.linkCtx)&&(nt=fi.tag,nt.linkCtx=fi),ni.open?(g&&(nt.parentElem=g.parentNode,nt._prv=g),nt._elCnt=ni.elCnt,!nt||nt.onBeforeLink&&nt.onBeforeLink()===!1||nt._.bound||(nt._.bound=!0,tt=nt.tagCtx.view,yt(i,nt._prv,tt,ni.id)),nt._.linking=!0):(nt._nxt=g,nt._.linking&&(pr=nt.tagCtx,tt=pr.view,delete nt._.linking,nt._.bound||(nt._.bound=!0,yt(i,nt._prv,tt,ni.id)),wt(nt,pr)))):yt(g.getAttribute(p),g,it(g),i,gr,n,b);kt&&kt.resolve()}var ui,fi,nt,ct,gt,et,bt,bi,g,tt,rt,ni,gi,nr,lr,ur,ii,ru,vr,yr,pr,kr,er,fu,dr,eu,ei,ut,ai,vi,or,ti,ft,sr,ri,at,cr,su,yi,kt,gr,st,nu=this,cu=nu._.id+"_",ht="",pi=[],dt=[],au=[],wu=nu.hlp(uu),yu=wi;if(v&&(kt=v.lazyLink&&t.Deferred(),v.tmpl?lr="/"+v._.id+"_":(gr=v.lnk,v.tag&&(cu=v.tag+"^",v=!0),(st=v.get)&&(yu=tu,ru=st.tags,vr=st.deep,yr=st.name)),v=v===!0),r=r?""+r===r?t(r)[0]:r.jquery?r[0]:r:nu.parentElem||e.body,kr=!a.noValidate&&r.contentEditable!==lt,ut=r.tagName.toLowerCase(),ft=!!hi[ut],u=u&&br(u,ft),f=f&&br(f,ft)||null,l!=i){if(or=e.createElement("div"),vi=or,cr=at="",ri=r.namespaceURI==="http://www.w3.org/2000/svg"?"svg_ns":(ei=ou.exec(l))&&ei[1]||"",ki&&ei&&ei[2]&&d("Unsupported: "+ei[2]),ft){for(ii=f;ii&&!(ur=s(ii));)ii=ii.nextSibling;(ti=ur?ur._tkns:r._df)&&(nr=lr||"",(v||!lr)&&(nr+="#"+cu),et=ti.indexOf(nr),et+1&&(et+=nr.length,cr=at=ti.slice(0,et),ti=ti.slice(et),ur?ii.setAttribute(o,ti):y(r,ti)))}if(ai=i,l=(""+l).replace(lu,pu),kr&&dt.length&&oi("Mismatched '<"+ut+"...>' in:\n"+l),k)return;for(fr.appendChild(or),ri=h[ri]||h.div,fu=ri[0],vi.innerHTML=ri[1]+l+ri[2];fu--;)vi=vi.lastChild;for(fr.removeChild(or),dr=e.createDocumentFragment();eu=vi.firstChild;)dr.appendChild(eu);r.insertBefore(dr,f)}return kt?setTimeout(iu,0):iu(),kt&&kt.promise()}function yt(n,t,u,f,e,o,s){var k,y,w,h,d,g,p,b,a,l,nt;if(f)l=c[f],l=l.linkCtx?l.linkCtx.tag:l,a=l.linkCtx||{data:u.data,elem:l._elCnt?l.parentElem:t,view:u,ctx:u.ctx,attr:v,fn:l._.bnd,tag:l,_bndId:f},pr(a,a.fn);else if(n&&t)for(o=e?o:u.data,k=u.tmpl,n=ku(n,at(t)),rt.lastIndex=0;y=rt.exec(n);)nt=rt.lastIndex,w=y[1],p=y[3],d=y[10],h=i,a={data:o,elem:t,view:u,ctx:s,attr:w,isLk:e,_initVal:!y[2]},y[6]&&(!w&&(h=/:([\w$]*)$/.exec(d))&&(h=h[1],h!==i&&(g=-h.length-1,p=p.slice(0,g-1)+ut)),h===null&&(h=i),a.convert=y[5]||""),a.expr=w+p,b=k.links[p],b||(k.links[p]=b=r.tmplFn(p,k,!0,h)),a.fn=b,w||h===i||(a.convertBack=h),pr(a,b),rt.lastIndex=nt}function pr(n,t){function u(i,r){pu.call(n,i,r,t)}u.noArray=!0;n.isLk&&(n.view=new r.View(n.ctx,"link",l,n.data,l.tmpl,i,i,vt));n._ctxCb=gu(n.view);n._hdlr=u;u(!0)}function pt(n,t){var i;return n?(i=n.indexOf(t),i+1?n.slice(0,i)+n.slice(i+t.length):n):""}function ot(n){return n&&(""+n===n?n:n.tagName==="SCRIPT"?n.type.slice(3):n.nodeType===1&&n.getAttribute(o)||"")}function s(n,t,i){function e(n,t,i,r,e,o){u.push({elCnt:f,id:r,ch:e,open:t,close:i,path:o,token:n})}var f,r,u=[];if(r=t?n:ot(n))return u.elCnt=!n.type,f=r.charAt(0)==="@"||!n.type,u._tkns=r,r.replace(i||yu,e),u}function wr(n,t){n&&(n.type==="jsv"?n.parentNode.removeChild(n):t&&n.getAttribute(p)===""&&n.removeAttribute(p))}function br(n,t){for(var i=n;t&&i&&i.nodeType!==1;)i=i.previousSibling;return i&&(i.nodeType!==1?(i=e.createElement("SCRIPT"),i.type="jsv",n.parentNode.insertBefore(i,n)):ot(i)||i.getAttribute(p)||i.setAttribute(p,"")),i}function ku(n,i){return n=t.trim(n).replace(su,"\\$&"),n.slice(-1)!==ut?n=gt+":"+n+(i?":":"")+ut:n}function wt(n,u){var e,f,l,o,s,y,p,a,w,v,b=u.view,h=n.linkCtx=n.linkCtx||{tag:n,data:b.data,view:b,ctx:b.ctx};if(n.onAfterLink)n.onAfterLink(u,h);if(e=n.targetTag?n.targetTag.linkedElem:n.linkedElem,(f=e&&e[0])&&((l=n._.radio)&&(e=e.children("input[type=radio]")),l||!n._.chging)){if(o=r.cvt(n,n.convert)[0],l||f!==h.elem){for(p=e.length;p--;){if(f=e[p],a=f._jsvLkEl,n._.inline&&(!a||a!==n&&a.targetTag!==n))for(f._jsvLkEl=n,s=h.elem?h.elem._jsvBnd:n._prv._jsvBnd,f._jsvBnd=s+"+",s=s.slice(1).split("&"),y=s.length;y--;)kr(c[s[y]],n.convertBack);l&&(f[g]=o===f.value)}h._val=o}o!==i&&(l||f.value===i?f.contentEditable===lt&&(f.innerHTML=o):f.type===ri?f[g]=o&&o!=="false":e.val(o))}(f=f||n.tagName===":"&&h.elem)&&(w=f._jsvTr,v=u.props.trigger,w!==v&&(f._jsvTr=v,e=e||t(f),yi(e,w,"off"),yi(e,v,"on")))}function du(n){setTimeout(function(){nt(n)},0)}function yi(n,t,i){t&&n[i](t===!0?"keydown":t,t===!0?du:nt)}function kr(n,t){var o,f,s,r,e,u=n.linkCtx,h=u.data,i=u.fn.paths;if(n&&i){for(i=(o=i._jsvto)||i[0],f=i&&i.length;f&&""+(r=i[--f])!==r;);r&&(!u.tag||u.tag.tagCtx.args.length)?(r=r.split("^").join("."),n.to=r.charAt(0)==="."?[[e=i[f-1],r.slice(1)],t]:[u._ctxCb(s=f?i[0].split("^").join("."):r)||[h,s],t],o&&e&&(n.to[0][0]=u._ctxCb(e,h))):n.to=[[],t]}}function dr(n,t,i){var o,s,h=n.tagCtx.view,e=n.tagCtxs||[n.tagCtx],c=e.length,l=!t;if(t=t||n._.bnd.call(h.tmpl,(n.linkCtx||h).data,h,u),i)e=n.tagCtxs=t,n.tagCtx=e[0];else while(c--)o=e[c],s=t[c],f(o.props).setProperty(s.props),b(o.ctx,s.ctx),o.args=s.args,l&&(o.tmpl=s.tmpl);return r._ths(n,e[0]),e}function bt(n){for(var u,t,i,f=[],e=n.length,r=e;r--;)f.push(n[r]);for(r=e;r--;)if(t=f[r],t.parentNode){if(i=t._jsvBnd)for(i=i.slice(1).split("&"),t._jsvBnd="",u=i.length;u--;)kt(i[u],t._jsvLkEl,t);nu(ot(t)+(t._df||""))}}function kt(n,u,f){var y,h,e,l,a,p,w,b,v,s,k,o=c[n];if(u)f===u.linkedElem[0]&&(delete f._jsvLkEl,delete u.linkedElem);else if(o){delete c[n];for(y in o.bnd)l=o.bnd[y],a=".obs"+o.cbId,t.isArray(l)?t([l]).off(ni+a).off(gi+a):t(l).off(gi+a),delete o.bnd[y];if(h=o.linkCtx){if(e=h.tag){if(p=e.tagCtxs)for(w=p.length;w--;)(b=p[w].map)&&b.unmap();v=e.linkedElem;s=v&&v[0]||h.elem;(k=s&&s._jsvTr)&&(yi(v||t(s),k,"off"),s._jsvTr=i);e.onDispose&&e.onDispose();e._elCnt||(e._prv&&e._prv.parentNode.removeChild(e._prv),e._nxt&&e._nxt.parentNode.removeChild(e._nxt))}delete h.view._.bnds[n]}delete r._cbBnds[o.cbId]}}function pi(n,r){return n===i?(tt&&(t(tt).off(nr,nt).off("blur","[contenteditable]",nt),tt=i),n=!0,l.removeViews(),bt(e.body.getElementsByTagName("*"))):r&&n===!0&&(r=r.jquery?r:t(r),r.each(function(){for(var n;(n=it(this,!0))&&n.parent;)n.parent.removeViews(n._.key,i,!0);bt(this.getElementsByTagName("*"));bt([this])})),r}function gr(n,t){return pi(this,n,t)}function gu(n){return function(t,i){var f,r,e=[i];if(n&&t){if(t._jsvOb)return t._jsvOb.call(n.tmpl,i,n,u);if(t.charAt(0)==="~")return t.slice(0,4)==="~tag"&&(r=n.ctx,t.charAt(4)==="."&&(f=t.slice(5).split("."),r=r.tag),f)?r?[r,f.join("."),i]:[]:(t=t.slice(1).split("."),(i=n.hlp(t.shift()))&&(t.length&&e.unshift(t.join(".")),e.unshift(i)),i?e:[]);if(t.charAt(0)==="#")return t==="#data"?[]:[n,t.replace(cu,""),i]}}}function nf(n){return n.type===ri?n[g]:n.value}function wi(n,t,i,r,u,f){var v,l,p,h,k,a,e,b=0,d=n===t;if(n){for(p=s(n)||[],v=0,l=p.length;v<l;v++){if(h=p[v],a=h.id,a===r&&h.ch===u)if(f)l=0;else break;d||(k=h.ch==="_"?w[a]:c[a].linkCtx.tag,h.open?k._prv=t:h.close&&(k._nxt=t));b+=a.length+2}b&&n.setAttribute(o,n.getAttribute(o).slice(b));e=t?t.getAttribute(o):i._df;(l=e.indexOf("/"+r+u)+1)&&(e=p._tkns.slice(0,b)+e.slice(l+(f?-1:r.length+1)));e&&(t?t.setAttribute(o,e):y(i,e))}else y(i,pt(i._df,"#"+r+u)),f||t||y(i,pt(i._df,"/"+r+u))}function nu(n){var r,f,t,u;if(u=s(n,!0,au))for(r=0,f=u.length;r<f;r++)t=u[r],t.ch==="_"?(t=w[t.id])&&t.type&&t.parent.removeViews(t._.key,i,!0):kt(t.id)}function tf(n,t,i){if(i.change==="set"){for(var r=n.tgt,u=r.length;u--;)if(r[u].key===i.path)break;u===-1?i.path&&f(r).insert({key:i.path,prop:i.value}):i.remove?f(r).remove(u):f(r[u]).setProperty("prop",i.value)}}function rf(n,t,i){var r,u=n.src,e=i.change;e==="set"?i.path==="prop"?f(u).setProperty(t.target.key,i.value):(f(u).setProperty(i.oldValue,null),delete u[i.oldValue],f(u).setProperty(i.value,t.target.prop)):e==="remove"?(r=i.items[0],f(u).removeProperty(r.key),delete u[r.key]):e==="insert"&&(r=i.items[0],r.key&&f(u).setProperty(r.key,r.prop))}function uf(n){return n.indexOf(".")<0}var dt="JsViews requires ",tt,it,rt,tu,gt,ut,iu,bi,ki,d,p,ft,di,h,l,w,e=n.document,u=t.views,r=u.sub,a=u.settings,b=r.extend,et=t.isFunction,ru=u.converters,k=u.tags,f=t.observable,st=f.observe,o="data-jsv",gi=r.propChng=r.propChng||"propertyChange",ni=r.arrChng=r.arrChng||"arrayChange",nr="change.jsv",ti="onBeforeChange",ii="onAfterChange",uu="onAfterCreate",g="checked",ri="checkbox",ht="radio",ct="none",lt="true",tr='"><\/script>',ir='<script type="jsv',rr=o+"-df",ui="script,["+o+"]",v="html",fi={value:"val",input:"val",html:v,text:"text"},ur={from:"value",to:"value"},ei=0,fu=t.cleanData,eu=a.delimiters,oi=r.syntaxErr,ou=/<(?!script)(\w+)(?:[^>]*(on\w+)\s*=)?[^>]*>/,su=/['"\\]/g,fr=e.createDocumentFragment(),si=e.querySelector,hi={ol:1,ul:1,table:1,tbody:1,thead:1,tfoot:1,tr:1,colgroup:1,dl:1,select:1,optgroup:1,svg:1,svg_ns:1},hu={tr:"table"},ci={br:1,img:1,input:1,hr:1,area:1,base:1,col:1,link:1,meta:1,command:1,embed:1,keygen:1,param:1,source:1,track:1,wbr:1},er={},c={},or=1,cu=/^#(view\.?)?/,lu=/(^|(\/>)|<\/(\w+)>|)(\s*)([#\/]\d+[_^])`(\s*)(<\w+(?=[\s\/>]))?|\s*(?:(<\w+(?=[\s\/>]))|<\/(\w+)>(\s*)|(\/>)\s*|(>))/g,sr=/(#)()(\d+)(_)/g,au=/(#)()(\d+)([_^])/g,hr=/(?:(#)|(\/))(\d+)(_)/g,vu=/(#)()(\d+)(\^)/g,yu=/(?:(#)|(\/))(\d+)([_^])([-+@\d]+)?/g,cr=n.getComputedStyle;if(!t)throw dt+"jQuery";if(!u)throw dt+"JsRender";if(!f)throw dt+"jquery.observable";t.link||(ft={contents:function(n,r){n!==!!n&&(r=n,n=i);var f,u=t(this.nodes());return u[0]&&(f=r?u.filter(r):u,u=n&&r?f.add(u.find(r)):f),u},nodes:function(n,t,i){var r,u=this,f=u._elCnt,o=!t&&f,e=[];for(t=t||u._prv,i=i||u._nxt,r=o?t===u._nxt?u.parentElem.lastSibling:t:u._.inline===!1?t||u.linkCtx.elem.firstChild:t&&t.nextSibling;r&&(!i||r!==i);)(n||f||r.tagName!=="SCRIPT")&&e.push(r),r=r.nextSibling;return e},childTags:function(n,t){n!==!!n&&(t=n,n=i);var r=this,e=r.link?r:r.tagCtx.view,u=r._prv,o=r._elCnt,f=[];return e.link(i,r.parentElem,o?u&&u.previousSibling:u,r._nxt,i,{get:{tags:f,deep:n,name:t,id:r.link?r._.id+"_":r._tgId+"^"}}),f},refresh:function(n){var r,f,t=this,e=t.linkCtx,o=t.tagCtx.view;return t.disposed&&d("Removed tag"),n===i&&(n=u._tag(t,o,o.tmpl,dr(t),!0)),n+""===n&&(f=t._.inline?v:e.attr||at(t.parentElem,!0),r=lr(n,e,f,t)),wt(t,t.tagCtx),r||t},update:function(n){var t=this.linkedElem;t&&nt({target:t[0]},i,n)}},r.onStore.template=function(n,i){i.link=yr;i.unlink=gr;n&&(t.link[n]=function(){return yr.apply(i,arguments)},t.unlink[n]=function(){return gr.apply(i,arguments)})},r.onStore.tag=function(n,t){r._lnk(t)},r._lnk=function(n){return b(n,ft)},r.viewInfos=s,(a.delimiters=function(){var n=eu.apply(u,arguments);return tu=n[0],gt=n[1],ut=n[2],iu=n[3],bi=n[4],rt=new RegExp("(?:^|\\s*)([\\w-]*)(\\"+bi+")?(\\"+gt+r.rTag+"\\"+ut+")","g"),this})(),b(r._lnk(r.View.prototype),{addViews:function(n,t,i){var u,s,r=this,e=t.length,o=r.views;if(!r._.useKey&&e&&(i=r.tmpl)&&(s=o.length+e,vr(r,n,i,o,t,r.ctx)!==!1))for(u=n+e;u<s;u++)f(o[u]).setProperty("index",u);return r},removeViews:function(n,r,u){function s(n){var s,h,c,f,o,l,r=e[n];if(r&&r.link){if(s=r._.id,u||(l=r.nodes()),r.removeViews(i,i,!0),r.type=i,f=r._prv,o=r._nxt,c=r.parentElem,u||(r._elCnt&&wi(f,o,c,s,"_"),t(l).remove()),!r._elCnt)try{f.parentNode.removeChild(f);o.parentNode.removeChild(o)}catch(a){}li(r);for(h in r._.bnds)kt(h);delete w[s]}}var o,a,h,c=this,l=!c._.useKey,e=c.views;if(l&&(h=e.length),n===i)if(l){for(o=h;o--;)s(o);c.views=[]}else{for(a in e)s(a);c.views={}}else if(r===i&&(l?r=1:(s(n),delete e[n])),l&&r){for(o=n+r;o-->n;)s(o);if(e.splice(n,r),h=e.length)while(n<h)f(e[n]).setProperty("index",n++)}return this},refresh:function(n){var t=this,i=t.parent;return i&&(vr(t,t.index,t.tmpl,i.views,t.data,n,!0),li(t)),t},link:bu}),w={0:l=new r.View},ru.merge=function(n){var t,i=this.linkCtx._val||"",r=this.tagCtx.props.toggle;return r&&(t=r.replace(/[\\^$.|?*+()[{]/g,"\\$&"),t="(\\s(?="+t+"$)|(\\s)|^)("+t+"(\\s|$))",i=i.replace(new RegExp(t),"$2"),n=i+(n?(i&&" ")+r:"")),n},k("on",{attr:ct,onAfterLink:function(n,u){for(var l,o,s=this,h=0,f=n.args,v=f.length,a=n.props.data,c=n.view,e=n.props.context;h<v&&!(o=et(l=f[h++])););if(o){o=f.slice(h);f=f.slice(0,h-1);e||(e=/^(.*)[\.^][\w$]+$/.exec(n.params.args.slice(-o.length-1)[0]),e=e&&r.tmplFn("{:"+e[1]+"}",c.tmpl,!0)(u.data,c));s._evs&&s.onDispose();t(u.elem).on(s._evs=f[0]||"click",s._sel=f[1],a==i?null:a,s._hlr=function(n){return l.apply(e||u.data,[].concat(o,n,{change:n.type,view:c,linkCtx:u}))})}},onDispose:function(){t(this.parentElem).off(this._evs,this._sel,this._hlr)},flow:!0}),b(k["for"],{onArrayChange:function(n,t){var i,r=this,u=t.change;if(r.tagCtxs[1]&&(u==="insert"&&n.target.length===t.items.length||u==="remove"&&!n.target.length||u==="refresh"&&!t.oldItems.length!=!n.target.length))r.refresh();else for(i in r._.arrVws)i=r._.arrVws[i],i.data===n.target&&i._.onArrayChange.apply(i,arguments);n.done=!0},onAfterLink:function(){for(var u,o,i,f,e=this,r=e._ars||{},s=e.tagCtxs,c=s.length,h=e.selected||0,n=0;n<=h;n++)u=s[n],f=u.map?u.map.tgt:u.args.length?u.args[0]:u.view.data,(i=r[n])&&f!==i[0]&&(st(i[0],i[1],!0),delete r[n]),!r[n]&&t.isArray(f)&&(st(f,o=function(n,t){e.onArrayChange(n,t)}),r[n]=[f,o]);for(n=h+1;n<c;n++)(i=r[n])&&(st(i[0],i[1],!0),delete r[n]);e._ars=r},onDispose:function(){var n,t=this;for(n in t._ars)st(t._ars[n][0],t._ars[n][1],!0)}}),b(k["for"],ft),b(k["if"],ft),b(k.include,ft),k("props",{baseTag:k["for"],dataMap:u.map({getTgt:k.props.dataMap.getTgt,obsSrc:tf,obsTgt:rf,tgtFlt:uf})}),b(t,{view:u.view=it=function(n,r,u){function p(n,t){if(n)for(o=s(n,t,sr),a=0,b=o.length;a<b;a++)if((f=w[o[a].id])&&(f=f&&u?f.get(!0,u):f))break}r!==!!r&&(u=r,r=i);var f,o,c,a,b,h,v,y=0,k=e.body;if(n&&n!==k&&l._.useKey>1&&(n=""+n===n?t(n)[0]:n.jquery?n[0]:n,n)){if(r){if(p(n._df,!0),!f)for(v=si?n.querySelectorAll(ui):t(ui,n).get(),h=v.length,c=0;!f&&c<h;c++)p(v[c]);return f}while(n){if(o=s(n,i,hr))for(h=o.length;h--;)if(f=o[h],f.open){if(y<1)return f=w[f.id],f&&u?f.get(u):f||l;y--}else y++;n=n.previousSibling||n.parentNode}}return l},link:u.link=vi,unlink:u.unlink=pi,cleanData:function(n){n.length&&ei&&bt(n);fu.apply(t,arguments)}}),u.utility={validate:function(n){try{l.link(i,e.createElement("div"),i,i,n,i,i,1)}catch(t){return t.message}}},b(t.fn,{link:function(n,t,i,r,u,f,e){return vi(n,this,t,i,r,u,f,e)},unlink:function(n){return pi(n,this)},view:function(n){return it(this[0],n)}}),t.each([v,"replaceWith","empty","remove"],function(n,i){var r=t.fn[i];t.fn[i]=function(){var n;ei=1;try{n=r.apply(this,arguments)}finally{ei=0}return n}}),b(l,{tmpl:{links:{},tags:{}}}),l._.onRender=vt,a({wrapMap:h={option:[1,"<select multiple='multiple'>","<\/select>"],legend:[1,"<fieldset>","<\/fieldset>"],area:[1,"<map>","<\/map>"],param:[1,"<object>","<\/object>"],thead:[1,"<table>","<\/table>"],tr:[2,"<table><tbody>","<\/tbody><\/table>"],td:[3,"<table><tbody><tr>","<\/tr><\/tbody><\/table>"],col:[2,"<table><tbody><\/tbody><colgroup>","<\/colgroup><\/table>"],svg_ns:[1,"<svg>","<\/svg>"],div:jQuery.support.htmlSerialize?[0,"",""]:[1,"X<div>","<\/div>"]},linkAttr:p="data-link",merge:{input:{from:nf,to:"value"},textarea:ur,select:ur,optgroup:{to:"label"}},jsrDbgMode:a.debugMode,debugMode:function(t){a.jsrDbgMode(t);t?n._jsv={views:w,bindings:c}:delete n._jsv},jsv:function(){a.debugMode(a._dbgMode);p=a.linkAttr;d=u._err;di=ui+",["+p+"]";ki=a.noDomLevel0;h.optgroup=h.option;h.tbody=h.tfoot=h.colgroup=h.caption=h.thead;h.th=h.td}}))}(this,this.jQuery);

/*
 Author: Maggie Wachs, www.filamentgroup.com
 Date: November 2011
 Dependencies: jQuery, jQuery UI widget factory
 example : http://filamentgroup.com/examples/rwd-table-patterns/
 */
(function(b){b.widget("filament.table",{options:{idprefix:null,persist:null,checkContainer:null},_create:function(){var f=this.options,d=this.element,c=d.find("thead"),a=d.find("tbody"),c=c.find("th"),i=a.find("tr"),g=f.checkContainer?b(f.checkContainer):b('<div class="table-menu table-menu-hidden"><ul /></div>');d.addClass("enhanced");c.each(function(h){var e=b(this),c=e.attr("id"),d=e.attr("class");c||(c=(f.idprefix?f.idprefix:"col-")+h,e.attr("id",c));i.each(function(){var a=b(this).find("th, td").eq(h);
a.attr("headers",c);d&&a.addClass(d)});if(!e.is("."+f.persist)){var a=b('<li><input type="checkbox" name="toggle-cols" id="toggle-col-'+h+'" value="'+c+'" /> <label for="toggle-col-'+h+'">'+e.text()+"</label></li>");g.find("ul").append(a);a.find("input").change(function(){var c=b(this),a=c.val(),a=b("#"+a+", [headers="+a+"]");c.is(":checked")?a.show():a.hide()}).bind("updateCheck",function(){e.css("display")=="table-cell"||e.css("display")=="inline"?b(this).attr("checked",true):b(this).attr("checked",
false)}).trigger("updateCheck")}});f.checkContainer||(a=b('<div class="table-menu-wrapper" />'),c=b('<a href="#" class="table-menu-btn"></a>'),c.click(function(){g.toggleClass("table-menu-hidden");return!1}),a.append(c).append(g),d.before(a),b(document).click(function(a){!b(a.target).is(g)&&!b(a.target).is(g.find("*"))&&g.addClass("table-menu-hidden")}))},disable:function(){},enable:function(){}})})(jQuery);

/*
    John resig array.remove : http://ejohn.org/blog/javascript-array-remove/
 */
// Remove the second item from the array
//array.remove(1);
// Remove the second-to-last item from the array
//array.remove(-2);
// Remove the second and third items from the array
//array.remove(1,2);
// Remove the last and second-to-last items from the array
//array.remove(-2,-1);

Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

/*
    json2.js
    2011-10-19
    https://github.com/douglascrockford/JSON-js/
*/
var JSON;JSON||(JSON={});
(function(){function k(a){return 10>a?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return"string"===typeof c?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function m(a,j){var c,d,h,n,g=e,f,b=j[a];b&&("object"===typeof b&&"function"===typeof b.toJSON)&&(b=b.toJSON(a));"function"===typeof i&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=l;f=[];if("[object Array]"===Object.prototype.toString.apply(b)){n=b.length;for(c=0;c<n;c+=1)f[c]=m(c,b)||"null";h=0===f.length?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&"object"===typeof i){n=i.length;for(c=0;c<n;c+=1)"string"===typeof i[c]&&(d=i[c],(h=m(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=m(d,b))&&f.push(o(d)+(e?": ":":")+h);h=0===f.length?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}"function"!==typeof Date.prototype.toJSON&&(Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()});var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,l,r={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;"function"!==typeof JSON.stringify&&(JSON.stringify=function(a,j,c){var d;l=e="";if(typeof c==="number")for(d=0;d<c;d=d+1)l=l+" ";else typeof c==="string"&&(l=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return m("",{"":a})});
"function"!==typeof JSON.parse&&(JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)if(Object.prototype.hasOwnProperty.call(b,g)){f=c(b,g);f!==void 0?b[g]=f:delete b[g]}return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,""))){d=eval("("+a+")");return typeof e==="function"?c({"":d},""):d}throw new SyntaxError("JSON.parse");})})();

/* A fix for the iOS orientationchange zoom bug.
Script by @scottjehl, rebound by @wilto.
MIT / GPLv2 License.
*/
(function(w){

	// This fix addresses an iOS bug, so return early if the UA claims it's something else.
	var ua = navigator.userAgent;
	if( !( /iPhone|iPad|iPod/.test( navigator.platform ) && /OS [1-5]_[0-9_]* like Mac OS X/i.test(ua) && ua.indexOf( "AppleWebKit" ) > -1 ) ){
		return;
	}

    var doc = w.document;

    if( !doc.querySelector ){ return; }

    var meta = doc.querySelector( "meta[name=viewport]" ),
        initialContent = meta && meta.getAttribute( "content" ),
        disabledZoom = initialContent + ",maximum-scale=1",
        enabledZoom = initialContent + ",maximum-scale=10",
        enabled = true,
		x, y, z, aig;

    if( !meta ){ return; }

    function restoreZoom(){
        meta.setAttribute( "content", enabledZoom );
        enabled = true;
    }

    function disableZoom(){
        meta.setAttribute( "content", disabledZoom );
        enabled = false;
    }

    function checkTilt( e ){
		aig = e.accelerationIncludingGravity;
		x = Math.abs( aig.x );
		y = Math.abs( aig.y );
		z = Math.abs( aig.z );

		// If portrait orientation and in one of the danger zones
        if( (!w.orientation || w.orientation === 180) && ( x > 7 || ( ( z > 6 && y < 8 || z < 8 && y > 6 ) && x > 5 ) ) ){
			if( enabled ){
				disableZoom();
			}        	
        }
		else if( !enabled ){
			restoreZoom();
        }
    }

	w.addEventListener( "orientationchange", restoreZoom, false );
	w.addEventListener( "devicemotion", checkTilt, false );

})( this );

//     Underscore.js 1.7.0
//     http://underscorejs.org
//     (c) 2009-2014 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
//     Underscore may be freely distributed under the MIT license.
(function(){var n=this,t=n._,r=Array.prototype,e=Object.prototype,u=Function.prototype,i=r.push,a=r.slice,o=r.concat,l=e.toString,c=e.hasOwnProperty,f=Array.isArray,s=Object.keys,p=u.bind,h=function(n){return n instanceof h?n:this instanceof h?void(this._wrapped=n):new h(n)};"undefined"!=typeof exports?("undefined"!=typeof module&&module.exports&&(exports=module.exports=h),exports._=h):n._=h,h.VERSION="1.7.0";var g=function(n,t,r){if(t===void 0)return n;switch(null==r?3:r){case 1:return function(r){return n.call(t,r)};case 2:return function(r,e){return n.call(t,r,e)};case 3:return function(r,e,u){return n.call(t,r,e,u)};case 4:return function(r,e,u,i){return n.call(t,r,e,u,i)}}return function(){return n.apply(t,arguments)}};h.iteratee=function(n,t,r){return null==n?h.identity:h.isFunction(n)?g(n,t,r):h.isObject(n)?h.matches(n):h.property(n)},h.each=h.forEach=function(n,t,r){if(null==n)return n;t=g(t,r);var e,u=n.length;if(u===+u)for(e=0;u>e;e++)t(n[e],e,n);else{var i=h.keys(n);for(e=0,u=i.length;u>e;e++)t(n[i[e]],i[e],n)}return n},h.map=h.collect=function(n,t,r){if(null==n)return[];t=h.iteratee(t,r);for(var e,u=n.length!==+n.length&&h.keys(n),i=(u||n).length,a=Array(i),o=0;i>o;o++)e=u?u[o]:o,a[o]=t(n[e],e,n);return a};var v="Reduce of empty array with no initial value";h.reduce=h.foldl=h.inject=function(n,t,r,e){null==n&&(n=[]),t=g(t,e,4);var u,i=n.length!==+n.length&&h.keys(n),a=(i||n).length,o=0;if(arguments.length<3){if(!a)throw new TypeError(v);r=n[i?i[o++]:o++]}for(;a>o;o++)u=i?i[o]:o,r=t(r,n[u],u,n);return r},h.reduceRight=h.foldr=function(n,t,r,e){null==n&&(n=[]),t=g(t,e,4);var u,i=n.length!==+n.length&&h.keys(n),a=(i||n).length;if(arguments.length<3){if(!a)throw new TypeError(v);r=n[i?i[--a]:--a]}for(;a--;)u=i?i[a]:a,r=t(r,n[u],u,n);return r},h.find=h.detect=function(n,t,r){var e;return t=h.iteratee(t,r),h.some(n,function(n,r,u){return t(n,r,u)?(e=n,!0):void 0}),e},h.filter=h.select=function(n,t,r){var e=[];return null==n?e:(t=h.iteratee(t,r),h.each(n,function(n,r,u){t(n,r,u)&&e.push(n)}),e)},h.reject=function(n,t,r){return h.filter(n,h.negate(h.iteratee(t)),r)},h.every=h.all=function(n,t,r){if(null==n)return!0;t=h.iteratee(t,r);var e,u,i=n.length!==+n.length&&h.keys(n),a=(i||n).length;for(e=0;a>e;e++)if(u=i?i[e]:e,!t(n[u],u,n))return!1;return!0},h.some=h.any=function(n,t,r){if(null==n)return!1;t=h.iteratee(t,r);var e,u,i=n.length!==+n.length&&h.keys(n),a=(i||n).length;for(e=0;a>e;e++)if(u=i?i[e]:e,t(n[u],u,n))return!0;return!1},h.contains=h.include=function(n,t){return null==n?!1:(n.length!==+n.length&&(n=h.values(n)),h.indexOf(n,t)>=0)},h.invoke=function(n,t){var r=a.call(arguments,2),e=h.isFunction(t);return h.map(n,function(n){return(e?t:n[t]).apply(n,r)})},h.pluck=function(n,t){return h.map(n,h.property(t))},h.where=function(n,t){return h.filter(n,h.matches(t))},h.findWhere=function(n,t){return h.find(n,h.matches(t))},h.max=function(n,t,r){var e,u,i=-1/0,a=-1/0;if(null==t&&null!=n){n=n.length===+n.length?n:h.values(n);for(var o=0,l=n.length;l>o;o++)e=n[o],e>i&&(i=e)}else t=h.iteratee(t,r),h.each(n,function(n,r,e){u=t(n,r,e),(u>a||u===-1/0&&i===-1/0)&&(i=n,a=u)});return i},h.min=function(n,t,r){var e,u,i=1/0,a=1/0;if(null==t&&null!=n){n=n.length===+n.length?n:h.values(n);for(var o=0,l=n.length;l>o;o++)e=n[o],i>e&&(i=e)}else t=h.iteratee(t,r),h.each(n,function(n,r,e){u=t(n,r,e),(a>u||1/0===u&&1/0===i)&&(i=n,a=u)});return i},h.shuffle=function(n){for(var t,r=n&&n.length===+n.length?n:h.values(n),e=r.length,u=Array(e),i=0;e>i;i++)t=h.random(0,i),t!==i&&(u[i]=u[t]),u[t]=r[i];return u},h.sample=function(n,t,r){return null==t||r?(n.length!==+n.length&&(n=h.values(n)),n[h.random(n.length-1)]):h.shuffle(n).slice(0,Math.max(0,t))},h.sortBy=function(n,t,r){return t=h.iteratee(t,r),h.pluck(h.map(n,function(n,r,e){return{value:n,index:r,criteria:t(n,r,e)}}).sort(function(n,t){var r=n.criteria,e=t.criteria;if(r!==e){if(r>e||r===void 0)return 1;if(e>r||e===void 0)return-1}return n.index-t.index}),"value")};var m=function(n){return function(t,r,e){var u={};return r=h.iteratee(r,e),h.each(t,function(e,i){var a=r(e,i,t);n(u,e,a)}),u}};h.groupBy=m(function(n,t,r){h.has(n,r)?n[r].push(t):n[r]=[t]}),h.indexBy=m(function(n,t,r){n[r]=t}),h.countBy=m(function(n,t,r){h.has(n,r)?n[r]++:n[r]=1}),h.sortedIndex=function(n,t,r,e){r=h.iteratee(r,e,1);for(var u=r(t),i=0,a=n.length;a>i;){var o=i+a>>>1;r(n[o])<u?i=o+1:a=o}return i},h.toArray=function(n){return n?h.isArray(n)?a.call(n):n.length===+n.length?h.map(n,h.identity):h.values(n):[]},h.size=function(n){return null==n?0:n.length===+n.length?n.length:h.keys(n).length},h.partition=function(n,t,r){t=h.iteratee(t,r);var e=[],u=[];return h.each(n,function(n,r,i){(t(n,r,i)?e:u).push(n)}),[e,u]},h.first=h.head=h.take=function(n,t,r){return null==n?void 0:null==t||r?n[0]:0>t?[]:a.call(n,0,t)},h.initial=function(n,t,r){return a.call(n,0,Math.max(0,n.length-(null==t||r?1:t)))},h.last=function(n,t,r){return null==n?void 0:null==t||r?n[n.length-1]:a.call(n,Math.max(n.length-t,0))},h.rest=h.tail=h.drop=function(n,t,r){return a.call(n,null==t||r?1:t)},h.compact=function(n){return h.filter(n,h.identity)};var y=function(n,t,r,e){if(t&&h.every(n,h.isArray))return o.apply(e,n);for(var u=0,a=n.length;a>u;u++){var l=n[u];h.isArray(l)||h.isArguments(l)?t?i.apply(e,l):y(l,t,r,e):r||e.push(l)}return e};h.flatten=function(n,t){return y(n,t,!1,[])},h.without=function(n){return h.difference(n,a.call(arguments,1))},h.uniq=h.unique=function(n,t,r,e){if(null==n)return[];h.isBoolean(t)||(e=r,r=t,t=!1),null!=r&&(r=h.iteratee(r,e));for(var u=[],i=[],a=0,o=n.length;o>a;a++){var l=n[a];if(t)a&&i===l||u.push(l),i=l;else if(r){var c=r(l,a,n);h.indexOf(i,c)<0&&(i.push(c),u.push(l))}else h.indexOf(u,l)<0&&u.push(l)}return u},h.union=function(){return h.uniq(y(arguments,!0,!0,[]))},h.intersection=function(n){if(null==n)return[];for(var t=[],r=arguments.length,e=0,u=n.length;u>e;e++){var i=n[e];if(!h.contains(t,i)){for(var a=1;r>a&&h.contains(arguments[a],i);a++);a===r&&t.push(i)}}return t},h.difference=function(n){var t=y(a.call(arguments,1),!0,!0,[]);return h.filter(n,function(n){return!h.contains(t,n)})},h.zip=function(n){if(null==n)return[];for(var t=h.max(arguments,"length").length,r=Array(t),e=0;t>e;e++)r[e]=h.pluck(arguments,e);return r},h.object=function(n,t){if(null==n)return{};for(var r={},e=0,u=n.length;u>e;e++)t?r[n[e]]=t[e]:r[n[e][0]]=n[e][1];return r},h.indexOf=function(n,t,r){if(null==n)return-1;var e=0,u=n.length;if(r){if("number"!=typeof r)return e=h.sortedIndex(n,t),n[e]===t?e:-1;e=0>r?Math.max(0,u+r):r}for(;u>e;e++)if(n[e]===t)return e;return-1},h.lastIndexOf=function(n,t,r){if(null==n)return-1;var e=n.length;for("number"==typeof r&&(e=0>r?e+r+1:Math.min(e,r+1));--e>=0;)if(n[e]===t)return e;return-1},h.range=function(n,t,r){arguments.length<=1&&(t=n||0,n=0),r=r||1;for(var e=Math.max(Math.ceil((t-n)/r),0),u=Array(e),i=0;e>i;i++,n+=r)u[i]=n;return u};var d=function(){};h.bind=function(n,t){var r,e;if(p&&n.bind===p)return p.apply(n,a.call(arguments,1));if(!h.isFunction(n))throw new TypeError("Bind must be called on a function");return r=a.call(arguments,2),e=function(){if(!(this instanceof e))return n.apply(t,r.concat(a.call(arguments)));d.prototype=n.prototype;var u=new d;d.prototype=null;var i=n.apply(u,r.concat(a.call(arguments)));return h.isObject(i)?i:u}},h.partial=function(n){var t=a.call(arguments,1);return function(){for(var r=0,e=t.slice(),u=0,i=e.length;i>u;u++)e[u]===h&&(e[u]=arguments[r++]);for(;r<arguments.length;)e.push(arguments[r++]);return n.apply(this,e)}},h.bindAll=function(n){var t,r,e=arguments.length;if(1>=e)throw new Error("bindAll must be passed function names");for(t=1;e>t;t++)r=arguments[t],n[r]=h.bind(n[r],n);return n},h.memoize=function(n,t){var r=function(e){var u=r.cache,i=t?t.apply(this,arguments):e;return h.has(u,i)||(u[i]=n.apply(this,arguments)),u[i]};return r.cache={},r},h.delay=function(n,t){var r=a.call(arguments,2);return setTimeout(function(){return n.apply(null,r)},t)},h.defer=function(n){return h.delay.apply(h,[n,1].concat(a.call(arguments,1)))},h.throttle=function(n,t,r){var e,u,i,a=null,o=0;r||(r={});var l=function(){o=r.leading===!1?0:h.now(),a=null,i=n.apply(e,u),a||(e=u=null)};return function(){var c=h.now();o||r.leading!==!1||(o=c);var f=t-(c-o);return e=this,u=arguments,0>=f||f>t?(clearTimeout(a),a=null,o=c,i=n.apply(e,u),a||(e=u=null)):a||r.trailing===!1||(a=setTimeout(l,f)),i}},h.debounce=function(n,t,r){var e,u,i,a,o,l=function(){var c=h.now()-a;t>c&&c>0?e=setTimeout(l,t-c):(e=null,r||(o=n.apply(i,u),e||(i=u=null)))};return function(){i=this,u=arguments,a=h.now();var c=r&&!e;return e||(e=setTimeout(l,t)),c&&(o=n.apply(i,u),i=u=null),o}},h.wrap=function(n,t){return h.partial(t,n)},h.negate=function(n){return function(){return!n.apply(this,arguments)}},h.compose=function(){var n=arguments,t=n.length-1;return function(){for(var r=t,e=n[t].apply(this,arguments);r--;)e=n[r].call(this,e);return e}},h.after=function(n,t){return function(){return--n<1?t.apply(this,arguments):void 0}},h.before=function(n,t){var r;return function(){return--n>0?r=t.apply(this,arguments):t=null,r}},h.once=h.partial(h.before,2),h.keys=function(n){if(!h.isObject(n))return[];if(s)return s(n);var t=[];for(var r in n)h.has(n,r)&&t.push(r);return t},h.values=function(n){for(var t=h.keys(n),r=t.length,e=Array(r),u=0;r>u;u++)e[u]=n[t[u]];return e},h.pairs=function(n){for(var t=h.keys(n),r=t.length,e=Array(r),u=0;r>u;u++)e[u]=[t[u],n[t[u]]];return e},h.invert=function(n){for(var t={},r=h.keys(n),e=0,u=r.length;u>e;e++)t[n[r[e]]]=r[e];return t},h.functions=h.methods=function(n){var t=[];for(var r in n)h.isFunction(n[r])&&t.push(r);return t.sort()},h.extend=function(n){if(!h.isObject(n))return n;for(var t,r,e=1,u=arguments.length;u>e;e++){t=arguments[e];for(r in t)c.call(t,r)&&(n[r]=t[r])}return n},h.pick=function(n,t,r){var e,u={};if(null==n)return u;if(h.isFunction(t)){t=g(t,r);for(e in n){var i=n[e];t(i,e,n)&&(u[e]=i)}}else{var l=o.apply([],a.call(arguments,1));n=new Object(n);for(var c=0,f=l.length;f>c;c++)e=l[c],e in n&&(u[e]=n[e])}return u},h.omit=function(n,t,r){if(h.isFunction(t))t=h.negate(t);else{var e=h.map(o.apply([],a.call(arguments,1)),String);t=function(n,t){return!h.contains(e,t)}}return h.pick(n,t,r)},h.defaults=function(n){if(!h.isObject(n))return n;for(var t=1,r=arguments.length;r>t;t++){var e=arguments[t];for(var u in e)n[u]===void 0&&(n[u]=e[u])}return n},h.clone=function(n){return h.isObject(n)?h.isArray(n)?n.slice():h.extend({},n):n},h.tap=function(n,t){return t(n),n};var b=function(n,t,r,e){if(n===t)return 0!==n||1/n===1/t;if(null==n||null==t)return n===t;n instanceof h&&(n=n._wrapped),t instanceof h&&(t=t._wrapped);var u=l.call(n);if(u!==l.call(t))return!1;switch(u){case"[object RegExp]":case"[object String]":return""+n==""+t;case"[object Number]":return+n!==+n?+t!==+t:0===+n?1/+n===1/t:+n===+t;case"[object Date]":case"[object Boolean]":return+n===+t}if("object"!=typeof n||"object"!=typeof t)return!1;for(var i=r.length;i--;)if(r[i]===n)return e[i]===t;var a=n.constructor,o=t.constructor;if(a!==o&&"constructor"in n&&"constructor"in t&&!(h.isFunction(a)&&a instanceof a&&h.isFunction(o)&&o instanceof o))return!1;r.push(n),e.push(t);var c,f;if("[object Array]"===u){if(c=n.length,f=c===t.length)for(;c--&&(f=b(n[c],t[c],r,e)););}else{var s,p=h.keys(n);if(c=p.length,f=h.keys(t).length===c)for(;c--&&(s=p[c],f=h.has(t,s)&&b(n[s],t[s],r,e)););}return r.pop(),e.pop(),f};h.isEqual=function(n,t){return b(n,t,[],[])},h.isEmpty=function(n){if(null==n)return!0;if(h.isArray(n)||h.isString(n)||h.isArguments(n))return 0===n.length;for(var t in n)if(h.has(n,t))return!1;return!0},h.isElement=function(n){return!(!n||1!==n.nodeType)},h.isArray=f||function(n){return"[object Array]"===l.call(n)},h.isObject=function(n){var t=typeof n;return"function"===t||"object"===t&&!!n},h.each(["Arguments","Function","String","Number","Date","RegExp"],function(n){h["is"+n]=function(t){return l.call(t)==="[object "+n+"]"}}),h.isArguments(arguments)||(h.isArguments=function(n){return h.has(n,"callee")}),"function"!=typeof/./&&(h.isFunction=function(n){return"function"==typeof n||!1}),h.isFinite=function(n){return isFinite(n)&&!isNaN(parseFloat(n))},h.isNaN=function(n){return h.isNumber(n)&&n!==+n},h.isBoolean=function(n){return n===!0||n===!1||"[object Boolean]"===l.call(n)},h.isNull=function(n){return null===n},h.isUndefined=function(n){return n===void 0},h.has=function(n,t){return null!=n&&c.call(n,t)},h.noConflict=function(){return n._=t,this},h.identity=function(n){return n},h.constant=function(n){return function(){return n}},h.noop=function(){},h.property=function(n){return function(t){return t[n]}},h.matches=function(n){var t=h.pairs(n),r=t.length;return function(n){if(null==n)return!r;n=new Object(n);for(var e=0;r>e;e++){var u=t[e],i=u[0];if(u[1]!==n[i]||!(i in n))return!1}return!0}},h.times=function(n,t,r){var e=Array(Math.max(0,n));t=g(t,r,1);for(var u=0;n>u;u++)e[u]=t(u);return e},h.random=function(n,t){return null==t&&(t=n,n=0),n+Math.floor(Math.random()*(t-n+1))},h.now=Date.now||function(){return(new Date).getTime()};var _={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","`":"&#x60;"},w=h.invert(_),j=function(n){var t=function(t){return n[t]},r="(?:"+h.keys(n).join("|")+")",e=RegExp(r),u=RegExp(r,"g");return function(n){return n=null==n?"":""+n,e.test(n)?n.replace(u,t):n}};h.escape=j(_),h.unescape=j(w),h.result=function(n,t){if(null==n)return void 0;var r=n[t];return h.isFunction(r)?n[t]():r};var x=0;h.uniqueId=function(n){var t=++x+"";return n?n+t:t},h.templateSettings={evaluate:/<%([\s\S]+?)%>/g,interpolate:/<%=([\s\S]+?)%>/g,escape:/<%-([\s\S]+?)%>/g};var A=/(.)^/,k={"'":"'","\\":"\\","\r":"r","\n":"n","\u2028":"u2028","\u2029":"u2029"},O=/\\|'|\r|\n|\u2028|\u2029/g,F=function(n){return"\\"+k[n]};h.template=function(n,t,r){!t&&r&&(t=r),t=h.defaults({},t,h.templateSettings);var e=RegExp([(t.escape||A).source,(t.interpolate||A).source,(t.evaluate||A).source].join("|")+"|$","g"),u=0,i="__p+='";n.replace(e,function(t,r,e,a,o){return i+=n.slice(u,o).replace(O,F),u=o+t.length,r?i+="'+\n((__t=("+r+"))==null?'':_.escape(__t))+\n'":e?i+="'+\n((__t=("+e+"))==null?'':__t)+\n'":a&&(i+="';\n"+a+"\n__p+='"),t}),i+="';\n",t.variable||(i="with(obj||{}){\n"+i+"}\n"),i="var __t,__p='',__j=Array.prototype.join,"+"print=function(){__p+=__j.call(arguments,'');};\n"+i+"return __p;\n";try{var a=new Function(t.variable||"obj","_",i)}catch(o){throw o.source=i,o}var l=function(n){return a.call(this,n,h)},c=t.variable||"obj";return l.source="function("+c+"){\n"+i+"}",l},h.chain=function(n){var t=h(n);return t._chain=!0,t};var E=function(n){return this._chain?h(n).chain():n};h.mixin=function(n){h.each(h.functions(n),function(t){var r=h[t]=n[t];h.prototype[t]=function(){var n=[this._wrapped];return i.apply(n,arguments),E.call(this,r.apply(h,n))}})},h.mixin(h),h.each(["pop","push","reverse","shift","sort","splice","unshift"],function(n){var t=r[n];h.prototype[n]=function(){var r=this._wrapped;return t.apply(r,arguments),"shift"!==n&&"splice"!==n||0!==r.length||delete r[0],E.call(this,r)}}),h.each(["concat","join","slice"],function(n){var t=r[n];h.prototype[n]=function(){return E.call(this,t.apply(this._wrapped,arguments))}}),h.prototype.value=function(){return this._wrapped},"function"==typeof define&&define.amd&&define("underscore",[],function(){return h})}).call(this);


/* 	Checked Polyfill
    Author: Ryan DeBeasi (352 Media Group)
    Description: Provides a .checked class that works like the :checked pseudo class on radio buttons and checkboxes but is available in older browsers such as IE7/8. */

(function ($) {
	$.fn.checkedPolyfill = function (options) {
		function checkValue ($elem) {
			var $label = $('label[for="' + $elem.attr('id') + '"]');
			// TODO: also find labels wrapped around the input
			if ($elem.prop('checked')) {
				$elem.addClass('checked');
				$label.addClass('checked');
			} else {
				$elem.removeClass('checked');
				$label.removeClass('checked');
			}
			// We modify the label as well as the input because authors may want to style the labels based on the state of the chebkox, and IE7 and IE8 don't fully support sibling selectors.
			// For more info: http://www.quirksmode.org/css/contents.html
			return $elem;
		}

		return this.each(function () {
			var $self = $(this);
			if ($self.prop('type') === 'radio') {
				$('input[name="' + $self.prop('name') + '"]').change(function() {
					checkValue($self);
				});
			} else if ($self.prop('type') === 'checkbox') {
				$self.change(function() {
					checkValue($self);
				});
			}
			checkValue($self); // Check value when plugin is first called, in case a value has already been set.
		});

	};
})(jQuery);


/*
 * jQuery Highlight plugin
 *
 * Based on highlight v3 by Johann Burkard
 * http://johannburkard.de/blog/programming/javascript/highlight-javascript-text-higlighting-jquery-plugin.html
 *
 * Code a little bit refactored and cleaned (in my humble opinion).
 * Most important changes:
 *  - has an option to highlight only entire words (wordsOnly - false by default),
 *  - has an option to be case sensitive (caseSensitive - false by default)
 *  - highlight element tag and class names can be specified in options
 *
 * Usage:
 *   // wrap every occurrance of text 'lorem' in content
 *   // with <span class='highlight'> (default options)
 *   $('#content').highlight('lorem');
 *
 *   // search for and highlight more terms at once
 *   // so you can save some time on traversing DOM
 *   $('#content').highlight(['lorem', 'ipsum']);
 *   $('#content').highlight('lorem ipsum');
 *
 *   // search only for entire word 'lorem'
 *   $('#content').highlight('lorem', { wordsOnly: true });
 *
 *   // don't ignore case during search of term 'lorem'
 *   $('#content').highlight('lorem', { caseSensitive: true });
 *
 *   // wrap every occurrance of term 'ipsum' in content
 *   // with <em class='important'>
 *   $('#content').highlight('ipsum', { element: 'em', className: 'important' });
 *
 *   // remove default highlight
 *   $('#content').unhighlight();
 *
 *   // remove custom highlight
 *   $('#content').unhighlight({ element: 'em', className: 'important' });
 *
 *
 * Copyright (c) 2009 Bartek Szopka
 *
 * Licensed under MIT license.
 *
 */

jQuery.extend({
    highlight: function (node, re, nodeName, className) {
        if (node.nodeType === 3) {
            var match = node.data.match(re);
            if (match) {
                var highlight = document.createElement(nodeName || 'span');
                highlight.className = className || 'highlight';
                var wordNode = node.splitText(match.index);
                wordNode.splitText(match[0].length);
                var wordClone = wordNode.cloneNode(true);
                highlight.appendChild(wordClone);
                wordNode.parentNode.replaceChild(highlight, wordNode);
                return 1; //skip added node in parent
            }
        } else if ((node.nodeType === 1 && node.childNodes) && // only element nodes that have children
                !/(script|style)/i.test(node.tagName) && // ignore script and style nodes
                !(node.tagName === nodeName.toUpperCase() && node.className === className)) { // skip if already highlighted
            for (var i = 0; i < node.childNodes.length; i++) {
                i += jQuery.highlight(node.childNodes[i], re, nodeName, className);
            }
        }
        return 0;
    }
});

jQuery.fn.unhighlight = function (options) {
    var settings = { className: 'highlight', element: 'span' };
    jQuery.extend(settings, options);

    return this.find(settings.element + "." + settings.className).each(function () {
        var parent = this.parentNode;
        parent.replaceChild(this.firstChild, this);
        parent.normalize();
    }).end();
};

jQuery.fn.highlight = function (words, options) {
    var settings = { className: 'highlight', element: 'span', caseSensitive: false, wordsOnly: false };
    jQuery.extend(settings, options);

    if (words.constructor === String) {
        words = [words];
    }
    words = jQuery.grep(words, function(word, i){
      return word != '';
    });
    words = jQuery.map(words, function(word, i) {
      return word.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
    });
    if (words.length == 0) { return this; };

    var flag = settings.caseSensitive ? "" : "i";
    var pattern = "(" + words.join("|") + ")";
    if (settings.wordsOnly) {
        pattern = "\\b" + pattern + "\\b";
    }
    var re = new RegExp(pattern, flag);

    return this.each(function () {
        jQuery.highlight(this, re, settings.element, settings.className);
    });
};

/* tiny scrollbar
*
* http://baijs.nl/tinyscrollbar
*/
(function(a){a.tiny=a.tiny||{};a.tiny.scrollbar={options:{axis:"y",wheel:40,scroll:true,lockscroll:true,size:"auto",sizethumb:"auto",invertscroll:false}};a.fn.tinyscrollbar=function(d){var c=a.extend({},a.tiny.scrollbar.options,d);this.each(function(){a(this).data("tsb",new b(a(this),c))});return this};a.fn.tinyscrollbar_update=function(c){return a(this).data("tsb").update(c)};function b(q,g){var k=this,t=q,j={obj:a(".viewport",q)},h={obj:a(".overview",q)},d={obj:a(".scrollbar",q)},m={obj:a(".track",d.obj)},p={obj:a(".thumb",d.obj)},l=g.axis==="x",n=l?"left":"top",v=l?"Width":"Height",r=0,y={start:0,now:0},o={},e="ontouchstart" in document.documentElement;function c(){k.update();s();return k}this.update=function(z){j[g.axis]=j.obj[0]["offset"+v];h[g.axis]=h.obj[0]["scroll"+v];h.ratio=j[g.axis]/h[g.axis];d.obj.toggleClass("disable",h.ratio>=1);m[g.axis]=g.size==="auto"?j[g.axis]:g.size;p[g.axis]=Math.min(m[g.axis],Math.max(0,(g.sizethumb==="auto"?(m[g.axis]*h.ratio):g.sizethumb)));d.ratio=g.sizethumb==="auto"?(h[g.axis]/m[g.axis]):(h[g.axis]-j[g.axis])/(m[g.axis]-p[g.axis]);r=(z==="relative"&&h.ratio<=1)?Math.min((h[g.axis]-j[g.axis]),Math.max(0,r)):0;r=(z==="bottom"&&h.ratio<=1)?(h[g.axis]-j[g.axis]):isNaN(parseInt(z,10))?r:parseInt(z,10);w()};function w(){var z=v.toLowerCase();p.obj.css(n,r/d.ratio);h.obj.css(n,-r);o.start=p.obj.offset()[n];d.obj.css(z,m[g.axis]);m.obj.css(z,m[g.axis]);p.obj.css(z,p[g.axis])}function s(){if(!e){p.obj.bind("mousedown",i);m.obj.bind("mouseup",u)}else{j.obj[0].ontouchstart=function(z){if(1===z.touches.length){i(z.touches[0]);z.stopPropagation()}}}if(g.scroll&&window.addEventListener){t[0].addEventListener("DOMMouseScroll",x,false);t[0].addEventListener("mousewheel",x,false);t[0].addEventListener("MozMousePixelScroll",function(z){z.preventDefault()},false)}else{if(g.scroll){t[0].onmousewheel=x}}}function i(A){a("body").addClass("noSelect");var z=parseInt(p.obj.css(n),10);o.start=l?A.pageX:A.pageY;y.start=z=="auto"?0:z;if(!e){a(document).bind("mousemove",u);a(document).bind("mouseup",f);p.obj.bind("mouseup",f)}else{document.ontouchmove=function(B){B.preventDefault();u(B.touches[0])};document.ontouchend=f}}function x(B){if(h.ratio<1){var A=B||window.event,z=A.wheelDelta?A.wheelDelta/120:-A.detail/3;r-=z*g.wheel;r=Math.min((h[g.axis]-j[g.axis]),Math.max(0,r));p.obj.css(n,r/d.ratio);h.obj.css(n,-r);if(g.lockscroll||(r!==(h[g.axis]-j[g.axis])&&r!==0)){A=a.event.fix(A);A.preventDefault()}}}function u(z){if(h.ratio<1){if(g.invertscroll&&e){y.now=Math.min((m[g.axis]-p[g.axis]),Math.max(0,(y.start+(o.start-(l?z.pageX:z.pageY)))))}else{y.now=Math.min((m[g.axis]-p[g.axis]),Math.max(0,(y.start+((l?z.pageX:z.pageY)-o.start))))}r=y.now*d.ratio;h.obj.css(n,-r);p.obj.css(n,y.now)}}function f(){a("body").removeClass("noSelect");a(document).unbind("mousemove",u);a(document).unbind("mouseup",f);p.obj.unbind("mouseup",f);document.ontouchmove=document.ontouchend=null}return c()}}(jQuery));


/*
    listJS 1.1.1
    2014-02-03
    http://listjs.com/
*/
!function(){function a(b,c,d){var e=a.resolve(b);if(null==e){d=d||b,c=c||"root";var f=new Error('Failed to require "'+d+'" from "'+c+'"');throw f.path=d,f.parent=c,f.require=!0,f}var g=a.modules[e];if(!g._resolving&&!g.exports){var h={};h.exports={},h.client=h.component=!0,g._resolving=!0,g.call(this,h.exports,a.relative(e),h),delete g._resolving,g.exports=h.exports}return g.exports}a.modules={},a.aliases={},a.resolve=function(b){"/"===b.charAt(0)&&(b=b.slice(1));for(var c=[b,b+".js",b+".json",b+"/index.js",b+"/index.json"],d=0;d<c.length;d++){var b=c[d];if(a.modules.hasOwnProperty(b))return b;if(a.aliases.hasOwnProperty(b))return a.aliases[b]}},a.normalize=function(a,b){var c=[];if("."!=b.charAt(0))return b;a=a.split("/"),b=b.split("/");for(var d=0;d<b.length;++d)".."==b[d]?a.pop():"."!=b[d]&&""!=b[d]&&c.push(b[d]);return a.concat(c).join("/")},a.register=function(b,c){a.modules[b]=c},a.alias=function(b,c){if(!a.modules.hasOwnProperty(b))throw new Error('Failed to alias "'+b+'", it does not exist');a.aliases[c]=b},a.relative=function(b){function c(a,b){for(var c=a.length;c--;)if(a[c]===b)return c;return-1}function d(c){var e=d.resolve(c);return a(e,b,c)}var e=a.normalize(b,"..");return d.resolve=function(d){var f=d.charAt(0);if("/"==f)return d.slice(1);if("."==f)return a.normalize(e,d);var g=b.split("/"),h=c(g,"deps")+1;return h||(h=0),d=g.slice(0,h+1).join("/")+"/deps/"+d},d.exists=function(b){return a.modules.hasOwnProperty(d.resolve(b))},d},a.register("component-classes/index.js",function(a,b,c){function d(a){if(!a)throw new Error("A DOM element reference is required");this.el=a,this.list=a.classList}var e=b("indexof"),f=/\s+/,g=Object.prototype.toString;c.exports=function(a){return new d(a)},d.prototype.add=function(a){if(this.list)return this.list.add(a),this;var b=this.array(),c=e(b,a);return~c||b.push(a),this.el.className=b.join(" "),this},d.prototype.remove=function(a){if("[object RegExp]"==g.call(a))return this.removeMatching(a);if(this.list)return this.list.remove(a),this;var b=this.array(),c=e(b,a);return~c&&b.splice(c,1),this.el.className=b.join(" "),this},d.prototype.removeMatching=function(a){for(var b=this.array(),c=0;c<b.length;c++)a.test(b[c])&&this.remove(b[c]);return this},d.prototype.toggle=function(a,b){return this.list?("undefined"!=typeof b?b!==this.list.toggle(a,b)&&this.list.toggle(a):this.list.toggle(a),this):("undefined"!=typeof b?b?this.add(a):this.remove(a):this.has(a)?this.remove(a):this.add(a),this)},d.prototype.array=function(){var a=this.el.className.replace(/^\s+|\s+$/g,""),b=a.split(f);return""===b[0]&&b.shift(),b},d.prototype.has=d.prototype.contains=function(a){return this.list?this.list.contains(a):!!~e(this.array(),a)}}),a.register("segmentio-extend/index.js",function(a,b,c){c.exports=function(a){for(var b,c=Array.prototype.slice.call(arguments,1),d=0;b=c[d];d++)if(b)for(var e in b)a[e]=b[e];return a}}),a.register("component-indexof/index.js",function(a,b,c){c.exports=function(a,b){if(a.indexOf)return a.indexOf(b);for(var c=0;c<a.length;++c)if(a[c]===b)return c;return-1}}),a.register("component-event/index.js",function(a){var b=window.addEventListener?"addEventListener":"attachEvent",c=window.removeEventListener?"removeEventListener":"detachEvent",d="addEventListener"!==b?"on":"";a.bind=function(a,c,e,f){return a[b](d+c,e,f||!1),e},a.unbind=function(a,b,e,f){return a[c](d+b,e,f||!1),e}}),a.register("timoxley-to-array/index.js",function(a,b,c){function d(a){return"[object Array]"===Object.prototype.toString.call(a)}c.exports=function(a){if("undefined"==typeof a)return[];if(null===a)return[null];if(a===window)return[window];if("string"==typeof a)return[a];if(d(a))return a;if("number"!=typeof a.length)return[a];if("function"==typeof a&&a instanceof Function)return[a];for(var b=[],c=0;c<a.length;c++)(Object.prototype.hasOwnProperty.call(a,c)||c in a)&&b.push(a[c]);return b.length?b:[]}}),a.register("javve-events/index.js",function(a,b){var c=b("event"),d=b("to-array");a.bind=function(a,b,e,f){a=d(a);for(var g=0;g<a.length;g++)c.bind(a[g],b,e,f)},a.unbind=function(a,b,e,f){a=d(a);for(var g=0;g<a.length;g++)c.unbind(a[g],b,e,f)}}),a.register("javve-get-by-class/index.js",function(a,b,c){c.exports=function(){return document.getElementsByClassName?function(a,b,c){return c?a.getElementsByClassName(b)[0]:a.getElementsByClassName(b)}:document.querySelector?function(a,b,c){return b="."+b,c?a.querySelector(b):a.querySelectorAll(b)}:function(a,b,c){var d=[],e="*";null==a&&(a=document);for(var f=a.getElementsByTagName(e),g=f.length,h=new RegExp("(^|\\s)"+b+"(\\s|$)"),i=0,j=0;g>i;i++)if(h.test(f[i].className)){if(c)return f[i];d[j]=f[i],j++}return d}}()}),a.register("javve-get-attribute/index.js",function(a,b,c){c.exports=function(a,b){var c=a.getAttribute&&a.getAttribute(b)||null;if(!c)for(var d=a.attributes,e=d.length,f=0;e>f;f++)void 0!==b[f]&&b[f].nodeName===b&&(c=b[f].nodeValue);return c}}),a.register("javve-natural-sort/index.js",function(a,b,c){c.exports=function(a,b,c){var d,e,f=/(^-?[0-9]+(\.?[0-9]*)[df]?e?[0-9]?$|^0x[0-9a-f]+$|[0-9]+)/gi,g=/(^[ ]*|[ ]*$)/g,h=/(^([\w ]+,?[\w ]+)?[\w ]+,?[\w ]+\d+:\d+(:\d+)?[\w ]?|^\d{1,4}[\/\-]\d{1,4}[\/\-]\d{1,4}|^\w+, \w+ \d+, \d{4})/,i=/^0x[0-9a-f]+$/i,j=/^0/,c=c||{},k=function(a){return c.insensitive&&(""+a).toLowerCase()||""+a},l=k(a).replace(g,"")||"",m=k(b).replace(g,"")||"",n=l.replace(f,"\x00$1\x00").replace(/\0$/,"").replace(/^\0/,"").split("\x00"),o=m.replace(f,"\x00$1\x00").replace(/\0$/,"").replace(/^\0/,"").split("\x00"),p=parseInt(l.match(i))||1!=n.length&&l.match(h)&&Date.parse(l),q=parseInt(m.match(i))||p&&m.match(h)&&Date.parse(m)||null,r=c.desc?-1:1;if(q){if(q>p)return-1*r;if(p>q)return 1*r}for(var s=0,t=Math.max(n.length,o.length);t>s;s++){if(d=!(n[s]||"").match(j)&&parseFloat(n[s])||n[s]||0,e=!(o[s]||"").match(j)&&parseFloat(o[s])||o[s]||0,isNaN(d)!==isNaN(e))return isNaN(d)?1:-1;if(typeof d!=typeof e&&(d+="",e+=""),e>d)return-1*r;if(d>e)return 1*r}return 0}}),a.register("javve-to-string/index.js",function(a,b,c){c.exports=function(a){return a=void 0===a?"":a,a=null===a?"":a,a=a.toString()}}),a.register("component-type/index.js",function(a,b,c){var d=Object.prototype.toString;c.exports=function(a){switch(d.call(a)){case"[object Date]":return"date";case"[object RegExp]":return"regexp";case"[object Arguments]":return"arguments";case"[object Array]":return"array";case"[object Error]":return"error"}return null===a?"null":void 0===a?"undefined":a!==a?"nan":a&&1===a.nodeType?"element":typeof a.valueOf()}}),a.register("list.js/index.js",function(a,b,c){!function(a,d){"use strict";var e=a.document,f=b("get-by-class"),g=b("extend"),h=b("indexof"),i=function(a,c,i){var j,k=this,l=b("./src/item")(k),m=b("./src/add-async")(k),n=b("./src/parse")(k);j={start:function(){k.listClass="list",k.searchClass="search",k.sortClass="sort",k.page=200,k.i=1,k.items=[],k.visibleItems=[],k.matchingItems=[],k.searched=!1,k.filtered=!1,k.handlers={updated:[]},k.plugins={},k.helpers={getByClass:f,extend:g,indexOf:h},g(k,c),k.listContainer="string"==typeof a?e.getElementById(a):a,k.listContainer&&(k.list=f(k.listContainer,k.listClass,!0),k.templater=b("./src/templater")(k),k.search=b("./src/search")(k),k.filter=b("./src/filter")(k),k.sort=b("./src/sort")(k),this.items(),k.update(),this.plugins())},items:function(){n(k.list),i!==d&&k.add(i)},plugins:function(){for(var a=0;a<k.plugins.length;a++){var b=k.plugins[a];k[b.name]=b,b.init(k)}}},this.add=function(a,b){if(b)return m(a,b),void 0;var c=[],e=!1;a[0]===d&&(a=[a]);for(var f=0,g=a.length;g>f;f++){var h=null;a[f]instanceof l?(h=a[f],h.reload()):(e=k.items.length>k.page?!0:!1,h=new l(a[f],d,e)),k.items.push(h),c.push(h)}return k.update(),c},this.show=function(a,b){return this.i=a,this.page=b,k.update(),k},this.remove=function(a,b,c){for(var d=0,e=0,f=k.items.length;f>e;e++)k.items[e].values()[a]==b&&(k.templater.remove(k.items[e],c),k.items.splice(e,1),f--,e--,d++);return k.update(),d},this.get=function(a,b){for(var c=[],d=0,e=k.items.length;e>d;d++){var f=k.items[d];f.values()[a]==b&&c.push(f)}return c},this.size=function(){return k.items.length},this.clear=function(){return k.templater.clear(),k.items=[],k},this.on=function(a,b){return k.handlers[a].push(b),k},this.off=function(a,b){var c=k.handlers[a],d=h(c,b);return d>-1&&c.splice(d,1),k},this.trigger=function(a){for(var b=k.handlers[a].length;b--;)k.handlers[a][b](k);return k},this.reset={filter:function(){for(var a=k.items,b=a.length;b--;)a[b].filtered=!1;return k},search:function(){for(var a=k.items,b=a.length;b--;)a[b].found=!1;return k}},this.update=function(){var a=k.items,b=a.length;k.visibleItems=[],k.matchingItems=[],k.templater.clear();for(var c=0;b>c;c++)a[c].matching()&&k.matchingItems.length+1>=k.i&&k.visibleItems.length<k.page?(a[c].show(),k.visibleItems.push(a[c]),k.matchingItems.push(a[c])):a[c].matching()?(k.matchingItems.push(a[c]),a[c].hide()):a[c].hide();return k.trigger("updated"),k},j.start()};c.exports=i}(window)}),a.register("list.js/src/search.js",function(a,b,c){var d=b("events"),e=b("get-by-class"),f=b("to-string");c.exports=function(a){var b,c,g,h,i={resetList:function(){a.i=1,a.templater.clear(),h=void 0},setOptions:function(a){2==a.length&&a[1]instanceof Array?c=a[1]:2==a.length&&"function"==typeof a[1]?h=a[1]:3==a.length&&(c=a[1],h=a[2])},setColumns:function(){c=void 0===c?i.toArray(a.items[0].values()):c},setSearchString:function(a){a=f(a).toLowerCase(),a=a.replace(/[-[\]{}()*+?.,\\^$|#]/g,"\\$&"),g=a},toArray:function(a){var b=[];for(var c in a)b.push(c);return b}},j={list:function(){for(var b=0,c=a.items.length;c>b;b++)j.item(a.items[b])},item:function(a){a.found=!1;for(var b=0,d=c.length;d>b;b++)if(j.values(a.values(),c[b]))return a.found=!0,void 0},values:function(a,c){return a.hasOwnProperty(c)&&(b=f(a[c]).toLowerCase(),""!==g&&b.search(g)>-1)?!0:!1},reset:function(){a.reset.search(),a.searched=!1}},k=function(b){return a.trigger("searchStart"),i.resetList(),i.setSearchString(b),i.setOptions(arguments),i.setColumns(),""===g?j.reset():(a.searched=!0,h?h(g,c):j.list()),a.update(),a.trigger("searchComplete"),a.visibleItems};return a.handlers.searchStart=a.handlers.searchStart||[],a.handlers.searchComplete=a.handlers.searchComplete||[],d.bind(e(a.listContainer,a.searchClass),"keyup",function(b){var c=b.target||b.srcElement,d=""===c.value&&!a.searched;d||k(c.value)}),d.bind(e(a.listContainer,a.searchClass),"input",function(a){var b=a.target||a.srcElement;""===b.value&&k("")}),a.helpers.toString=f,k}}),a.register("list.js/src/sort.js",function(a,b,c){var d=b("natural-sort"),e=b("classes"),f=b("events"),g=b("get-by-class"),h=b("get-attribute");c.exports=function(a){a.sortFunction=a.sortFunction||function(a,b,c){return c.desc="desc"==c.order?!0:!1,d(a.values()[c.valueName],b.values()[c.valueName],c)};var b={els:void 0,clear:function(){for(var a=0,c=b.els.length;c>a;a++)e(b.els[a]).remove("asc"),e(b.els[a]).remove("desc")},getOrder:function(a){var b=h(a,"data-order");return"asc"==b||"desc"==b?b:e(a).has("desc")?"asc":e(a).has("asc")?"desc":"asc"},getInSensitive:function(a,b){var c=h(a,"data-insensitive");b.insensitive="true"===c?!0:!1},setOrder:function(a){for(var c=0,d=b.els.length;d>c;c++){var f=b.els[c];if(h(f,"data-sort")===a.valueName){var g=h(f,"data-order");"asc"==g||"desc"==g?g==a.order&&e(f).add(a.order):e(f).add(a.order)}}}},c=function(){a.trigger("sortStart"),options={};var c=arguments[0].currentTarget||arguments[0].srcElement||void 0;c?(options.valueName=h(c,"data-sort"),b.getInSensitive(c,options),options.order=b.getOrder(c)):(options=arguments[1]||options,options.valueName=arguments[0],options.order=options.order||"asc",options.insensitive="undefined"==typeof options.insensitive?!0:options.insensitive),b.clear(),b.setOrder(options),options.sortFunction=options.sortFunction||a.sortFunction,a.items.sort(function(a,b){return options.sortFunction(a,b,options)}),a.update(),a.trigger("sortComplete")};return a.handlers.sortStart=a.handlers.sortStart||[],a.handlers.sortComplete=a.handlers.sortComplete||[],b.els=g(a.listContainer,a.sortClass),f.bind(b.els,"click",c),a.on("searchStart",b.clear),a.on("filterStart",b.clear),a.helpers.classes=e,a.helpers.naturalSort=d,a.helpers.events=f,a.helpers.getAttribute=h,c}}),a.register("list.js/src/item.js",function(a,b,c){c.exports=function(a){return function(b,c,d){var e=this;this._values={},this.found=!1,this.filtered=!1;var f=function(b,c,d){if(void 0===c)d?e.values(b,d):e.values(b);else{e.elm=c;var f=a.templater.get(e,b);e.values(f)}};this.values=function(b,c){if(void 0===b)return e._values;for(var d in b)e._values[d]=b[d];c!==!0&&a.templater.set(e,e.values())},this.show=function(){a.templater.show(e)},this.hide=function(){a.templater.hide(e)},this.matching=function(){return a.filtered&&a.searched&&e.found&&e.filtered||a.filtered&&!a.searched&&e.filtered||!a.filtered&&a.searched&&e.found||!a.filtered&&!a.searched},this.visible=function(){return e.elm.parentNode==a.list?!0:!1},f(b,c,d)}}}),a.register("list.js/src/templater.js",function(a,b,c){var d=b("get-by-class"),e=function(a){function b(b){if(void 0===b){for(var c=a.list.childNodes,d=0,e=c.length;e>d;d++)if(void 0===c[d].data)return c[d];return null}if(-1!==b.indexOf("<")){var f=document.createElement("div");return f.innerHTML=b,f.firstChild}return document.getElementById(a.item)}var c=b(a.item),e=this;this.get=function(a,b){e.create(a);for(var c={},f=0,g=b.length;g>f;f++){var h=d(a.elm,b[f],!0);c[b[f]]=h?h.innerHTML:""}return c},this.set=function(a,b){if(!e.create(a))for(var c in b)if(b.hasOwnProperty(c)){var f=d(a.elm,c,!0);f&&("IMG"===f.tagName&&""!==b[c]?f.src=b[c]:f.innerHTML=b[c])}},this.create=function(a){if(void 0!==a.elm)return!1;var b=c.cloneNode(!0);return b.removeAttribute("id"),a.elm=b,e.set(a,a.values()),!0},this.remove=function(b){a.list.removeChild(b.elm)},this.show=function(b){e.create(b),a.list.appendChild(b.elm)},this.hide=function(b){void 0!==b.elm&&b.elm.parentNode===a.list&&a.list.removeChild(b.elm)},this.clear=function(){if(a.list.hasChildNodes())for(;a.list.childNodes.length>=1;)a.list.removeChild(a.list.firstChild)}};c.exports=function(a){return new e(a)}}),a.register("list.js/src/filter.js",function(a,b,c){c.exports=function(a){return a.handlers.filterStart=a.handlers.filterStart||[],a.handlers.filterComplete=a.handlers.filterComplete||[],function(b){if(a.trigger("filterStart"),a.i=1,a.reset.filter(),void 0===b)a.filtered=!1;else{a.filtered=!0;for(var c=a.items,d=0,e=c.length;e>d;d++){var f=c[d];f.filtered=b(f)?!0:!1}}return a.update(),a.trigger("filterComplete"),a.visibleItems}}}),a.register("list.js/src/add-async.js",function(a,b,c){c.exports=function(a){return function(b,c,d){var e=b.splice(0,100);d=d||[],d=d.concat(a.add(e)),b.length>0?setTimeout(function(){addAsync(b,c,d)},10):(a.update(),c(d))}}}),a.register("list.js/src/parse.js",function(a,b,c){c.exports=function(a){var c=b("./item")(a),d=function(a){for(var b=a.childNodes,c=[],d=0,e=b.length;e>d;d++)void 0===b[d].data&&c.push(b[d]);return c},e=function(b,d){for(var e=0,f=b.length;f>e;e++)a.items.push(new c(d,b[e]))},f=function(b,c){var d=b.splice(0,100);e(d,c),b.length>0?setTimeout(function(){init.items.indexAsync(b,c)},10):a.update()};return function(){var b=d(a.list),c=a.valueNames;a.indexAsync?f(b,c):e(b,c)}}}),a.alias("component-classes/index.js","list.js/deps/classes/index.js"),a.alias("component-classes/index.js","classes/index.js"),a.alias("component-indexof/index.js","component-classes/deps/indexof/index.js"),a.alias("segmentio-extend/index.js","list.js/deps/extend/index.js"),a.alias("segmentio-extend/index.js","extend/index.js"),a.alias("component-indexof/index.js","list.js/deps/indexof/index.js"),a.alias("component-indexof/index.js","indexof/index.js"),a.alias("javve-events/index.js","list.js/deps/events/index.js"),a.alias("javve-events/index.js","events/index.js"),a.alias("component-event/index.js","javve-events/deps/event/index.js"),a.alias("timoxley-to-array/index.js","javve-events/deps/to-array/index.js"),a.alias("javve-get-by-class/index.js","list.js/deps/get-by-class/index.js"),a.alias("javve-get-by-class/index.js","get-by-class/index.js"),a.alias("javve-get-attribute/index.js","list.js/deps/get-attribute/index.js"),a.alias("javve-get-attribute/index.js","get-attribute/index.js"),a.alias("javve-natural-sort/index.js","list.js/deps/natural-sort/index.js"),a.alias("javve-natural-sort/index.js","natural-sort/index.js"),a.alias("javve-to-string/index.js","list.js/deps/to-string/index.js"),a.alias("javve-to-string/index.js","list.js/deps/to-string/index.js"),a.alias("javve-to-string/index.js","to-string/index.js"),a.alias("javve-to-string/index.js","javve-to-string/index.js"),a.alias("component-type/index.js","list.js/deps/type/index.js"),a.alias("component-type/index.js","type/index.js"),"object"==typeof exports?module.exports=a("list.js"):"function"==typeof define&&define.amd?define(function(){return a("list.js")}):this.List=a("list.js")}();


// -----------------------------------
// Slidebars
// Version 0.10.2
// http://plugins.adchsm.me/slidebars/
//
// Written by Adam Smith
// http://www.adchsm.me/
//
// Released under MIT License
// http://plugins.adchsm.me/slidebars/license.txt
//
// ---------------------
// Index of Slidebars.js
//
// 001 - Default Settings
// 002 - Feature Detection
// 003 - User Agents
// 004 - Setup
// 005 - Animation
// 006 - Operations
// 007 - API
// 008 - User Input

;(function($) {

	$.slidebars = function(options) {

		// ----------------------
		// 001 - Default Settings

		var settings = $.extend({
			siteClose: true, // true or false - Enable closing of Slidebars by clicking on #sb-site.
			scrollLock: false, // true or false - Prevent scrolling of site when a Slidebar is open.
			disableOver: false, // integer or false - Hide Slidebars over a specific width.
			hideControlClasses: false // true or false - Hide controls at same width as disableOver.
		}, options);

		// -----------------------
		// 002 - Feature Detection

		var test = document.createElement('div').style, // Create element to test on.
		supportTransition = false, // Variable for testing transitions.
		supportTransform = false; // variable for testing transforms.

		// Test for CSS Transitions
		if (test.MozTransition === '' || test.WebkitTransition === '' || test.OTransition === '' || test.transition === '') supportTransition = true;

		// Test for CSS Transforms
		if (test.MozTransform === '' || test.WebkitTransform === '' || test.OTransform === '' || test.transform === '') supportTransform = true;

		// -----------------
		// 003 - User Agents

		var ua = navigator.userAgent, // Get user agent string.
		android = false, // Variable for storing android version.
		iOS = false; // Variable for storing iOS version.
		
		if (/Android/.test(ua)) { // Detect Android in user agent string.
			android = ua.substr(ua.indexOf('Android')+8, 3); // Set version of Android.
		} else if (/(iPhone|iPod|iPad)/.test(ua)) { // Detect iOS in user agent string.
			iOS = ua.substr(ua.indexOf('OS ')+3, 3).replace('_', '.'); // Set version of iOS.
		}
		
		if (android && android < 3 || iOS && iOS < 5) $('html').addClass('sb-static'); // Add helper class for older versions of Android & iOS.

		// -----------
		// 004 - Setup

		// Site container
		var $site = $('#sb-site, .sb-site-container'); // Cache the selector.

		// Left Slidebar	
		if ($('.sb-left').length) { // Check if the left Slidebar exists.
			var $left = $('.sb-left'), // Cache the selector.
			leftActive = false; // Used to check whether the left Slidebar is open or closed.
		}

		// Right Slidebar
		if ($('.sb-right').length) { // Check if the right Slidebar exists.
			var $right = $('.sb-right'), // Cache the selector.
			rightActive = false; // Used to check whether the right Slidebar is open or closed.
		}
				
		var init = false, // Initialisation variable.
		windowWidth = $(window).width(), // Get width of window.
		$controls = $('.sb-toggle-left, .sb-toggle-right, .sb-open-left, .sb-open-right, .sb-close'), // Cache the control classes.
		$slide = $('.sb-slide'); // Cache users elements to animate.
		
		// Initailise Slidebars
		function initialise() {
			if (!settings.disableOver || (typeof settings.disableOver === 'number' && settings.disableOver >= windowWidth)) { // False or larger than window size. 
				init = true; // true enabled Slidebars to open.
				$('html').addClass('sb-init'); // Add helper class.
				if (settings.hideControlClasses) $controls.removeClass('sb-hide'); // Remove class just incase Slidebars was originally disabled.
				css(); // Set required inline styles.
			} else if (typeof settings.disableOver === 'number' && settings.disableOver < windowWidth) { // Less than window size.
				init = false; // false stop Slidebars from opening.
				$('html').removeClass('sb-init'); // Remove helper class.
				if (settings.hideControlClasses) $controls.addClass('sb-hide'); // Hide controls
				$site.css('minHeight', ''); // Remove minimum height.
				if (leftActive || rightActive) close(); // Close Slidebars if open.
			}
		}
		initialise();
		
		// Inline CSS
		function css() {
			// Set minimum height.
			$site.css('minHeight', ''); // Reset minimum height.
			$site.css('minHeight', $('html').height() + 'px'); // Set minimum height of the site to the minimum height of the html.
			
			// Custom Slidebar widths.
			if ($left && $left.hasClass('sb-width-custom')) $left.css('width', $left.attr('data-sb-width')); // Set user custom width.
			if ($right && $right.hasClass('sb-width-custom')) $right.css('width', $right.attr('data-sb-width')); // Set user custom width.
			
			// Set off-canvas margins for Slidebars with push and overlay animations.
			if ($left && ($left.hasClass('sb-style-push') || $left.hasClass('sb-style-overlay'))) $left.css('marginLeft', '-' + $left.css('width'));
			if ($right && ($right.hasClass('sb-style-push') || $right.hasClass('sb-style-overlay'))) $right.css('marginRight', '-' + $right.css('width'));
			
			// Site scroll locking.
			if (settings.scrollLock) $('html').addClass('sb-scroll-lock');
		}
		
		// Resize Functions
		$(window).resize(function() {
			var resizedWindowWidth = $(window).width(); // Get resized window width.
			if (windowWidth !== resizedWindowWidth) { // Slidebars is running and window was actually resized.
				windowWidth = resizedWindowWidth; // Set the new window width.
				initialise(); // Call initalise to see if Slidebars should still be running.
				if (leftActive) open('left'); // If left Slidebar is open, calling open will ensure it is the correct size.
				if (rightActive) open('right'); // If right Slidebar is open, calling open will ensure it is the correct size.
			}
		});
		// I may include a height check along side a width check here in future.

		// ---------------
		// 005 - Animation

		var animation; // Animation type.

		// Set animation type.
		if (supportTransition && supportTransform) { // Browser supports css transitions and transforms.
			animation = 'translate'; // Translate for browsers that support it.
			if (android && android < 4.4) animation = 'side'; // Android supports both, but can't translate any fixed positions, so use left instead.
		} else {
			animation = 'jQuery'; // Browsers that don't support css transitions and transitions.
		}

		// Animate mixin.
		function animate(object, amount, side) {
			// Choose selectors depending on animation style.
			var selector;
			
			if (object.hasClass('sb-style-push')) {
				selector = $site.add(object).add($slide); // Push - Animate site, Slidebar and user elements.
			} else if (object.hasClass('sb-style-overlay')) {
				selector = object; // Overlay - Animate Slidebar only.
			} else {
				selector = $site.add($slide); // Reveal - Animate site and user elements.
			}
			
			// Apply animation
			if (animation === 'translate') {
				selector.css('transform', 'translate(' + amount + ')'); // Apply the animation.

			} else if (animation === 'side') {		
				if (amount[0] === '-') amount = amount.substr(1); // Remove the '-' from the passed amount for side animations.
				if (amount !== '0px') selector.css(side, '0px'); // Add a 0 value so css transition works.
				setTimeout(function() { // Set a timeout to allow the 0 value to be applied above.
					selector.css(side, amount); // Apply the animation.
				}, 1);

			} else if (animation === 'jQuery') {
				if (amount[0] === '-') amount = amount.substr(1); // Remove the '-' from the passed amount for jQuery animations.
				var properties = {};
				properties[side] = amount;
				selector.stop().animate(properties, 400); // Stop any current jQuery animation before starting another.
			}
			
			// If closed, remove the inline styling on completion of the animation.
			setTimeout(function() {
				if (amount === '0px') {
					selector.removeAttr('style');
					css();
				}
			}, 400);
		}

		// ----------------
		// 006 - Operations

		// Open a Slidebar
		function open(side) {
			// Check to see if opposite Slidebar is open.
			if (side === 'left' && $left && rightActive || side === 'right' && $right && leftActive) { // It's open, close it, then continue.
				close();
				setTimeout(proceed, 400);
			} else { // Its not open, continue.
				proceed();
			}

			// Open
			function proceed() {
				if (init && side === 'left' && $left) { // Slidebars is initiated, left is in use and called to open.
					$('html').addClass('sb-active sb-active-left'); // Add active classes.
					$left.addClass('sb-active');
					animate($left, $left.css('width'), 'left'); // Animation
					setTimeout(function() { leftActive = true; }, 400); // Set active variables.
				} else if (init && side === 'right' && $right) { // Slidebars is initiated, right is in use and called to open.
					$('html').addClass('sb-active sb-active-right'); // Add active classes.
					$right.addClass('sb-active');
					animate($right, '-' + $right.css('width'), 'right'); // Animation
					setTimeout(function() { rightActive = true; }, 400); // Set active variables.
				}
			}
		}
			
		// Close either Slidebar
		function close(link) {
			if (leftActive || rightActive) { // If a Slidebar is open.
				if (leftActive) {
					animate($left, '0px', 'left'); // Animation
					leftActive = false;
				}
				if (rightActive) {
					animate($right, '0px', 'right'); // Animation
					rightActive = false;
				}
			
				//setTimeout(function() { // Wait for closing animation to finish. //adapted for jSCAF
					$('html').removeClass('sb-active sb-active-left sb-active-right'); // Remove active classes.
					if ($left) $left.removeClass('sb-active');
					if ($right) $right.removeClass('sb-active');
					if (typeof link !== 'undefined') window.location = link; // If a link has been passed to the function, go to it.
				//}, 400); // adapted for jSCAF
			}
		}
		
		// Toggle either Slidebar
		function toggle(side) {
			if (side === 'left' && $left) { // If left Slidebar is called and in use.
				if (!leftActive) {
					open('left'); // Slidebar is closed, open it.
				} else {
					close(); // Slidebar is open, close it.
				}
			}
			if (side === 'right' && $right) { // If right Slidebar is called and in use.
				if (!rightActive) {
					open('right'); // Slidebar is closed, open it.
				} else {
					close(); // Slidebar is open, close it.
				}
			}
		}

		// ---------
		// 007 - API
		
		this.slidebars = {
			open: open, // Maps user variable name to the open method.
			close: close, // Maps user variable name to the close method.
			toggle: toggle, // Maps user variable name to the toggle method.
			init: function() { // Returns true or false whether Slidebars are running or not.
				return init; // Returns true or false whether Slidebars are running.
			},
			active: function(side) { // Returns true or false whether Slidebar is open or closed.
				if (side === 'left' && $left) return leftActive;
				if (side === 'right' && $right) return rightActive;
			},
			destroy: function(side) { // Removes the Slidebar from the DOM.
				if (side === 'left' && $left) {
					if (leftActive) close(); // Close if its open.
					setTimeout(function() {
						$left.remove(); // Remove it.
						$left = false; // Set variable to false so it cannot be opened again.
					}, 400);
				}
				if (side === 'right' && $right) {
					if (rightActive) close(); // Close if its open.
					setTimeout(function() {
						$right.remove(); // Remove it.
						$right = false; // Set variable to false so it cannot be opened again.
					}, 400);
				}
			}
		};

		// ----------------
		// 008 - User Input
		
		function eventHandler(event, selector) {
			event.stopPropagation(); // Stop event bubbling.
			event.preventDefault(); // Prevent default behaviour.
			if (event.type === 'touchend') selector.off('click'); // If event type was touch, turn off clicks to prevent phantom clicks.
		}
		
		// Toggle left Slidebar
		$('.sb-toggle-left').on('touchend click', function(event) {
			eventHandler(event, $(this)); // Handle the event.
			toggle('left'); // Toggle the left Slidbar.
		});
		
		// Toggle right Slidebar
		$('.sb-toggle-right').on('touchend click', function(event) {
			eventHandler(event, $(this)); // Handle the event.
			toggle('right'); // Toggle the right Slidbar.
		});
		
		// Open left Slidebar
		$('.sb-open-left').on('touchend click', function(event) {
			eventHandler(event, $(this)); // Handle the event.
			open('left'); // Open the left Slidebar.
		});
		
		// Open right Slidebar
		$('.sb-open-right').on('touchend click', function(event) {
			eventHandler(event, $(this)); // Handle the event.
			open('right'); // Open the right Slidebar.
		});
		
		// Close Slidebar
		$('.sb-close').on('touchend click', function(event) {
			if ( $(this).is('a') || $(this).children().is('a') ) { // Is a link or contains a link.
				if ( event.type === 'click' ) { // Make sure the user wanted to follow the link.
					event.preventDefault(); // Stop default behaviour.
					var href = ( $(this).is('a') ? $(this).attr('href') : $(this).find('a').attr('href') ); // Get the href.
					close( href ); // Close Slidebar and pass link.
				}
			} else { // Just a normal control class.
				eventHandler(event, $(this)); // Handle the event.
				close(); // Close Slidebar.
			}
		});
		
		// Close Slidebar via site
		$site.on('touchend click', function(event) {
			if (settings.siteClose && (leftActive || rightActive)) { // If settings permit closing by site and left or right Slidebar is open.
				eventHandler(event, $(this)); // Handle the event.
				close(); // Close it.
			}
		});
		
	}; // End Slidebars function.

}) (jQuery);
/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,localStorage,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF AJAX MODULE                      == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __AJAX: {

            initAjaxEvents: function() {

                $._log('AJAX.initAjaxEvents');

                //AJAX EVENTS
                //-----------
                $._$document.ajaxStart(function () {
                    $._log('AJAX.ajaxStart');
                    $.__AJAX.ajaxCallRunning = true;
                    if ($._isBlockerPage) {
                        $._blockUI();
                    }
                    jQuery.noticeRemoveAll($('.notice-item-wrapper'));
                });

                $._$document.ajaxStop(function () {
                    $._log('AJAX.ajaxStop');

                    $.__AJAX.ajaxCallRunning = false;

                    if ($._settings.isInitDisplayOnAjaxStop) {
                        $._initDisplay();
                    }
                    if ($._isBlockerPage) {
                        $._unblockUI();
                    }
                    $._settings.isInitDisplayOnAjaxStop = true;
                });

                $._$document.ajaxError(function (e, x) {
                    $._log('AJAX.ajaxError');
                    $.__AJAX.ajaxCallRunning = false;

                    if ($.__AJAX.handleRedirect(x)) {
                        return;
                    }

                    var message = '';
                    var statusErrorMap = {
                        '400': "Server understood the request but request content was invalid.",
                        '401': "Unauthorised access.",
                        '403': "Forbidden resource can't be accessed",
                        '404': "Requested action url not found",
                        '500': "Internal Server Error",
                        '503': "Service Unavailable"
                    };

                    if (x.status !== undefined && x.status !== null && x.status !== 200) {
                        if ((x.status === 0 || x.status === 303 || x.status === 12017) && x.responseText === '' && x.statusText === 'error') {
                            message = 'EXCEPTION:EcasRedirectFailed';
                        } else {
                            message = x.status + ' [ ' + statusErrorMap[x.status] + ' ]';
                        }
                    } else if (x.status === 200 && x.statusText === 'parsererror') {
                        message = "Error.Parsing JSON Request failed.";
                    } else if (e === 'timeout') {
                        message = "Request Time out";
                    } else if (e === 'abort') {
                        message = "Request was aborted by the server";
                    } else if (x.status === 200 && x.statusText === 'OK') { //for preventing ajax error on JSON returning empty array
                        message = null;
                    } else {
                        message = "Unknown Error";
                    }

                    $.__AJAX.ajaxErrorMessage = message;
                    $.__AJAX.ajaxErrorResponse = x;

                    $._forceLog('AJAX.ajaxError : ' + $.__AJAX.ajaxErrorMessage);
                });

            },

            loadInnerFragment: function () {
                $._log('AJAX.loadInnerFragment');

                $.__AJAX.ajax({
                    id: $._settings.innerFragmentName,
                    call: $._settings.innerFragmentLoadActionName,
                    isBlockerActive: false,
                    fnPostCall: function() {
                        if ($._settings.koActive) {
                            $.__initKnockoutJS();
                        }
                        $.__initPostCommon();
                    }
                });
            },

            handleRedirect: function (x) {
                if (x && x.getResponseHeader('X-JSCAF-REDIRECT-TO')) {

                    window.location.href = x.getResponseHeader('X-JSCAF-REDIRECT-TO');
                    return true; // prevent further handling of ajax result
                }

                return false;
            },


            // INTERFACE TO jQuery AJAX LOW-LEVEL FUNCTION
            // -------------------------------------------

            isPrototypeMode: false,

            ajaxXhr: null,
            ajaxErrorMessage: null,
            ajaxErrorResponse: null,
            ajaxCallRunning: false,
            ajaxJsonArray: null,
            ajaxData: null,

            ajaxSettings: {},

            ajax: function (opt) {

                //get defaults settings and subclass by options if provided

                var settings = $.extend({

                    callType: 'POST',
                    isDataTypeHtml: true,
                    isDataTypeJson: false,
                    isDataTypeXml: false,
                    pageForm: $._getPageForm(),
                    pageUrl: $._getPageUrl(opt.callOverride),
                    id: null,
                    fragmentIdToRefresh: null,
                    isFragmentRefreshed: true,
                    call: null,
                    callOverride: null,
                    dispatchValue: null,
                    action: null,
                    paramValue: null,
                    isBlockerActive: true,
                    fnPreSuccess: null,
                    fnPreCall: null,
                    fnBeforeSend: $._settings.fnDefaultAjaxBeforeSend,
                    fnPostCall: null,
                    isDisplayAjaxCallError: true,
                    isAsyncCall: true,
                    isTemplateUsed: false,
                    parentTemplateIdToRefresh: null,
                    templateId: null,
                    isJsonArrayRefreshed: false,
                    isJsonArrayPosted: false,
                    jsonArrayPosted: null,
                    cache: true,
                    successNotificationMessage: null,
                    fnServerValidationSuccess: null,
                    fnServerValidationError: null,
                    isDialogRefreshed: false

                }, opt);


                if ($.__AJAX.ajaxCallRunning && !$._settings.isPrototypeMode) {
                    return;
                }

                $._log('AJAX.__ajax');

                if (settings.pageUrl === null && !$._settings.isPrototypeMode) {
                    $._log('====>WARNING : pageUrl cannot be NULL !');
                    return;
                }

                //storing the settings used for later usage
                $.__AJAX.ajaxSettings = settings;


                //use new id declaration, fragmentIdToRefresh replacement
                var fragmentIdToRefresh = null;
                if (settings.id !== null) {
                    fragmentIdToRefresh = settings.id;
                } else {
                    fragmentIdToRefresh = settings.fragmentIdToRefresh;
                }

                var dispatchValue = null;
                if (settings.call !== null) {
                    dispatchValue = settings.call;
                } else {
                    if (settings.callOverride !== null) {
                        dispatchValue = settings.callOverride;
                    } else {
                        dispatchValue = settings.dispatchValue;
                    }
                }


                //check first if it's a prototype ajax call (locally loaded file)
                //setting up special ajax call for prototype action
                if (settings.pageUrl === 'PROTOTYPE' || $._settings.isPrototypeMode) {

                    //setting the blocker as inactive as local calls are faster,
                    //settings.isBlockerActive = false;

                    //setting the cache to false for local page prototyping ajax call
                    settings.cache = false;

                    if (dispatchValue === $._settings.innerFragmentLoadActionName) {
                        //inner load fragment html file name can be provided in the page app settings
                        if ($._settings.prototypeInnerFragmentPageUrl !== null) {
                            settings.pageUrl = $._settings.prototypeInnerFragmentPageUrl;
                        }
                    } else {
                        settings.pageUrl = dispatchValue;
                    }

                    //need to change the url to prevent browser cache of locally called files which normally needs a refresh cache
                    settings.pageUrl += '?v=' + moment().unix();

                    //setting the prototype mode flag for further usages
                    $.__AJAX.isPrototypeMode = true;


                //normal ajax execution, finding the url
                } else {
                    if (dispatchValue !== null) {
                        var $dispatchValue = $($._getById('dispatchValue'));
                        if ($dispatchValue.length) {
                            //option #1 dispatch value is set on every form in a special hidden field
                            $dispatchValue.val(dispatchValue);
                        } else {
                            if ($._settings.isFormParamDispatchValueUsed) {
                                $._setParamValue('dispatchValue', dispatchValue);
                            } else {
                                if (settings.pageUrl !== null) {
                                    if (settings.paramValue !== null) {
                                        //option #2 action if set on the form (example : form action="/person"), the dispatchValue will be the method of the mapping for example : "/add", to match the complete request mapping : "/person/add"
                                        if (settings.pageUrl.indexOf(dispatchValue) < 0) {
                                            settings.pageUrl = settings.pageUrl.replace('.do','') + '/' + dispatchValue + '/' + settings.paramValue + '.do';
                                        } else {
                                            settings.pageUrl = '/' + dispatchValue + '/' + settings.paramValue + '.do';
                                        }

                                    } else {
                                        //option #3 action if set on the form (example : form action="/person"), the dispatchValue will be the method of the mapping for example : "/add", to match the complete request mapping : "/person/add"
                                        if (settings.pageUrl.indexOf(dispatchValue) < 0) {
                                            settings.pageUrl = settings.pageUrl.replace('.do','') + '/' + dispatchValue + '.do';
                                        } else {
                                            settings.pageUrl = '/' + dispatchValue + '.do';
                                        }
                                    }
                                } else {
                                    //option #4 in case no form action="/anAction" is specified, the full request mapping is set in the dispatchValue
                                    settings.pageUrl = $._getContextPath() + dispatchValue;
                                }
                            }
                        }
                    }
                }

                if (settings.action !== null) {
                    var $action = $($._getById('action'));
                    if ($action.length) {
                        $action.val(settings.action);
                    } else {
                        if ($._settings.isFormParamActionUsed) {
                            $._setParamValue('action', settings.action);
                        }
                    }
                    $action = null;
                }

                //setup the ajax default blocker : force it to be displayed when it's null (by default)
                if (settings.isBlockerActive === null) {
                    $._isBlockerPage = true;
                } else {
                    $._isBlockerPage = settings.isBlockerActive;
                }

                //checking data type
                //elseif is not used here, because the default is always isDataTypeHtml = true if not explicitly set to false
                var dataType;
                if (settings.isDataTypeHtml) {
                    dataType = 'html';
                }
                if (settings.isDataTypeJson) {
                    dataType = 'json';
                    settings.cache = false;
                }
                if (settings.isDataTypeXml) {
                    dataType = 'xml';
                    settings.cache = false;
                }

                //checking if a template is used
                if (settings.isTemplateUsed) {
                    if (!settings.isDataTypeJson || settings.templateId === null) {
                        $._log('ERROR : when using template, isDataTypeJson must be TRUE AND templateId must not be NULL');
                        return;
                    }
                }


                //reset error message and error response
                $.__AJAX.ajaxErrorMessage = null;
                $.__AJAX.ajaxErrorResponse = null;

                //reset refreshed json array
                $.__AJAX.ajaxJsonArray = null;

                //reset ajax data
                $.__AJAX.ajaxData = null;

                if (settings.fnPreCall !== null) {
                    settings.fnPreCall();
                }

                if (settings.isDialogRefreshed) {
                    $._log('AJAX.Dialog injection');
                    $._injectDialog();
                }

                //put a special param for AJAX calls to be known server-side
                if ($._settings.isFormDispatchCallTypeUsed) {
                    $._setParamValue('dispatchCallType', 'AJAX');
                }

                //checking page parameters and do injection
                $._injectPageParams();

                //checking jsonArray transferred
                var transferredData = null;
                var contentType = null;
                if (settings.isJsonArrayPosted) {
                    if ($._settings.isFormParamDispatchValueUsed) {
                        transferredData = 'dispatchValue=' + dispatchValue +
                            '&paramArray=' + JSON.stringify(settings.jsonArrayPosted);
                    } else {
                        transferredData = JSON.stringify(settings.jsonArrayPosted);
                    }                  
                    contentType = 'application/json';
                } else {
                    if (settings.pageForm !== null) {
                        transferredData = $($._getById(settings.pageForm)).serialize();
                    }
                    contentType = 'application/x-www-form-urlencoded; charset=UTF-8'; //jQuery default as of the doc
                }

                //log important settings
                $._log('AJAX.ajax.settings.callType=' + settings.callType);
                $._log('AJAX.ajax.settings.pageUrl=' + settings.pageUrl);
                $._log('AJAX.ajax.settings.pageForm=' + settings.pageForm);
                $._log('AJAX.ajax.dataType=' + dataType);
                $._log('AJAX.ajax.fragmentIdToRefresh=' + fragmentIdToRefresh);
                $._log('AJAX.ajax.transferredData=' + transferredData);

                //making the low-level ajax call
                $.__AJAX.ajaxXhr = $.ajax({
                    type: settings.callType,
                    cache: settings.cache,
                    url: settings.pageUrl,
                    data: transferredData,
                    dataType: dataType,
                    contentType: contentType,
                    async: settings.isAsyncCall,
                    beforeSend: settings.fnBeforeSend,
                    success: function (data, textStatus, x) {
                        $._log('AJAX.ajax.SUCCESS');

                        if ($.__AJAX.handleRedirect(x)) {
                            return;
                        }

                        if (settings.fnPreSuccess !== null) {
                            settings.fnPreSuccess();
                        }

                        if (settings.isDialogRefreshed) {
                            $._log('AJAX.re-appending refreshed dialog content:' + fragmentIdToRefresh + ' to dialog container:' + $.__currentDialogId);
                            $('#'+fragmentIdToRefresh).appendTo('#'+$.__currentDialogId);
                        }

                        //HTML/text injection
                        if (settings.isDataTypeHtml) {
                            if ($._isIE9() && $._settings.isCleanIE9AjaxHtmlTags) {
                                data = data.replace(/>\s+(?=<\/?(t|c)[hardfob])/gm, '>');
                            }


                            if ($._settings.isPrototypeMode) {

                                var cOutCnt, cOutPosition,i, cOutString, trailingQuotesOffset, valueObject, valueDefArray, pageFormObject;

                                cOutCnt = data.split('<c:out').length-1;

                                for (i=0; i<cOutCnt; i++) {

                                    cOutPosition = data.indexOf('<c:out');

                                    cOutString = data.substr(cOutPosition, data.substr(cOutPosition).indexOf('/>')+2);
                                    trailingQuotesOffset = 2;
                                    if (data.indexOf('"<c:out') !== -1) {
                                        trailingQuotesOffset = 2;
                                    }
                                    valueObject = cOutString.substr(cOutString.indexOf('${')+2, cOutString.indexOf('}') - cOutString.indexOf('${')-trailingQuotesOffset);
                                    valueDefArray = valueObject.split('.');
                                    pageFormObject = JSON.parse(localStorage.getItem(valueDefArray[0]));

                                    data = data.replace(cOutString, pageFormObject[valueDefArray[1]]);
                                }

                                cOutCnt = cOutPosition = i = cOutString = trailingQuotesOffset = valueObject = valueDefArray = pageFormObject = null;

                            }

                            if (settings.isFragmentRefreshed) {
                                $._addClass($._html,'hidden');
                                $($._getById(fragmentIdToRefresh)).html(data);
                                $._removeClass($._html,'hidden');
                            }

                            //JSON injection native or using a template
                        } else {
                            if (settings.isTemplateUsed) {

                                var refreshedObject,
                                    $template = $($._getById(settings.templateId));

                                if (settings.parentTemplateIdToRefresh !== null) {
                                    //if explicitly provided
                                    refreshedObject = $($._getById(settings.parentTemplateIdToRefresh));
                                } else {
                                    //by default, the container of the template is always the previous element in the DOM
                                    refreshedObject = $template.prev();
                                }

                                refreshedObject.html($template.render(data));

                                refreshedObject = $template = null;
                            }

                            //refreshing also the jsonArray object if needed
                            if (settings.isJsonArrayRefreshed) {
                                $.__AJAX.ajaxJsonArray = data;
                                $._ajaxJsonArray = data;
                            } else {
                                $.__AJAX.ajaxData = data;
                            }
                        }
                    },


                    complete: function () {
                        $._log('AJAX.ajax.COMPLETE');


                        //detecting if error has been detected during ajax call catched with .ajaxError global event
                        if ($.__AJAX.ajaxErrorMessage !== null) {

                            var errorMessageNotification = null,
                                isNotifyError = false;

                            if (settings.fnPreSuccess !== null) {
                                settings.fnPreSuccess();
                            }

                            //just a safety to be sure the overlay block is totally destroyed.
                            $._unblockUI();
                            $('.blockUI').remove();

                            //Reset the call stack ajax calls flag when an error occurs to prevent other ajax calls
                            //in the queue to be executed after
                            if ($.__isCallStackSyncRunning) {
                                $.__isCallStackSyncRunning = false;
                            }

                            //prototype mode is detected before the ajax call for local HTML calls, in that case, only simple notification are displayed
                            if ($.__AJAX.isPrototypeMode) {

                                isNotifyError = true;

                                if ( $.__AJAX.ajaxErrorResponse.status === 404) {
                                    errorMessageNotification = '<u>' + $.__AJAX.ajaxSettings.pageUrl + '</u> NOT FOUND';
                                    if ($.__modules.BROWSER && $._isChrome()) {
                                        errorMessageNotification += '<br>launch CHROME with command line parameter <span class="text-color-red big">--allow-file-access-from-files</span>';
                                    } else {
                                        errorMessageNotification += '<br>IF using CHROME, launch it with command line parameter <span class="text-color-red big">--allow-file-access-from-files</span>';
                                    }
                                }

                            } else {

                                if (settings.isDisplayAjaxCallError && $._settings.isAjaxErrorDisplay) {
                                    if ($._settings.isAjaxErrorNotificationDisplayOnly) {
                                        isNotifyError = true;
                                    } else {
                                        if ($._settings.fnDisplayErrorDialogOverride === null) {
                                            if ($.__modules.UI_DIALOG) {
                                                $.__UI_DIALOG.displayErrorDialog($.__AJAX.ajaxErrorResponse.responseText, $.__AJAX.ajaxErrorMessage, false);
                                            } else {
                                                isNotifyError = true;
                                            }
                                        } else {
                                            //in case the error is handled by the application itself,
                                            //then jSCAF postCommon must be called before
                                            $.__initPostCommon(true);

                                            //calling the override error display function
                                            $._settings.fnDisplayErrorDialogOverride($.__AJAX.ajaxErrorResponse.responseText);
                                        }
                                    }
                                }
                            }

                            //if no error dialog is display and no override occurs, then the normal notification is executed
                            if (isNotifyError) {
                                if (errorMessageNotification === null) {
                                    errorMessageNotification = 'ERROR <br>' + $.__AJAX.ajaxErrorMessage;
                                }
                                $._notifyError(errorMessageNotification);

                                //the init post common must be called if case a notification is displayed,
                                //the default if an error dialog is used using the displayErrorDialog function handle this call
                                $.__initPostCommon(true);
                            }


                        //callback if the fnPostCall has been provided
                        } else {

                            $.__AJAX.ajaxCallRunning = false;

                            if (settings.fnPostCall !== null) {
                                $._execFn(settings.fnPostCall,true);
                            }

                            //display the success notification if a message has been provided
                            if (settings.successNotificationMessage !== null) {
                                $._notifySuccess(settings.successNotificationMessage);
                            }


                            //Specific functions calls to handle server-side SpringMVC bean validation on the refreshed fragment
                            if (settings.fnServerValidationSuccess !== null && settings.fnServerValidationError !== null) {

                                if ($('#' + fragmentIdToRefresh).find('.error').length) {
                                    settings.fnServerValidationError();
                                } else {
                                    settings.fnServerValidationSuccess();
                                }

                            }
                        }

                        //remove paramsContainer and empty parameters array
                        $($._getById('paramsContainer')).remove();

                        //reset timeoutDialog counter
                        if (!$._isEmpty($._settings.timeoutDialogProperties)) {
                            $._settings.timeoutDialogProperties.currentInterval = 0;
                        }

                        $.__pageParams = null;

                    }


                });

                // return the json array if the call is a JSON call (ajaxJsonGet, ajaxJsonPost or ajaxJsonRefreshTemplate)
                if (settings.isJsonArrayRefreshed) {
                    return $.__AJAX.ajaxJsonArray;
                }
            },

        },




        //SHORTHANDS
        //----------
        _AR: function (opt, fnPostCall) {
            $._ajaxRefresh(opt, fnPostCall);
        },

        _AC: function (opt, fnPostCall) {
            $._ajaxCall(opt, fnPostCall);
        },

        _ARO: function (opt, fnPostCall) {
            $._ajaxRefreshOnly(opt, fnPostCall);
        },

        _AJG: function(opt) {
            return $._ajaxJsonGet(opt);
        },

        _AJP: function(opt) {
            return $._ajaxJsonPost(opt);
        },

        _AJRT: function(opt) {
            return $._ajaxJsonRefreshTemplate(opt);
        },




        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonArray
         * @description todo
         * @example todo
         */
        _ajaxJsonArray: null,




        // SYNCHRONOUS CALL STACK IMPLEMENTATION
        // -------------------------------------

        __callStackSyncIdx: 0,
        __callStackSyncFunctionsStack: [],
        __isCallStackSyncRunning: false,


        _callStackSyncNext: function () {
            $._log('AJAX.callStackSyncNext ==> stack function # ' + $.__callStackSyncIdx);

            if ($.__callStackSyncIdx === $.__callStackSyncFunctionsStack.length - 1) {
                $._log('AJAX.callStackSyncNext ==> END of stack reached');
                $.__isCallStackSyncRunning = false;
            }
            var action = $.__callStackSyncFunctionsStack[ $.__callStackSyncIdx++ ];
            $._execFn(action);
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _callStackSync
         * @description todo
         * @example todo
         */
        _callStackSync: function (functionsStack) {
            $._log('AJAX.callStackSync');

            if (!$.__isCallStackSyncRunning) {
                //important to avoid multiple call to initDisplay at each ajax function call
                $._settings.isInitDisplayOnAjaxStop = false;

                //to prevent multiple queues of running at the same time
                $.__isCallStackSyncRunning = true;
                $.__callStackSyncIdx = 0;
                $.__callStackSyncFunctionsStack = functionsStack;

                $._callStackSyncNext();
            }
        },



        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxRefreshOnly
         * @description todo
         * @example todo
         */
        _ajaxRefreshOnly: function (opt, fnPostCall) {
            $._log('AJAX.ajaxRefreshOnly : ' + opt.id);

            var settings = $._extend({
                id: null,
                call: 'refreshFragment',
                isBlockerActive: false,
                fnPostCall: fnPostCall
            },opt);

            $.__AJAX.ajax({
                id: settings.id,
                call: settings.call,
                action: settings.id,
                isBlockerActive: settings.isBlockerActive,
                fnPreCall: settings.fnPreCall,
                fnPostCall: settings.fnPostCall
            });
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxRefresh
         * @description todo
         * @example todo
         */
        _ajaxRefresh: function (opt, fnPostCall) {

            if (fnPostCall === undefined) {
                fnPostCall = null;
            }

            var settings = $.extend({
                callType: 'POST',
                isDataTypeHtml: true,
                isDataTypeJson: false,
                id: null,
                fragmentIdToRefresh: null,
                isFragmentRefreshed: true,
                pageUrl: null,
                pageForm: null,
                dispatchValue: null,
                paramValue: null,
                call: null,
                callOverride: null,
                action: null,
                isBlockerActive: true,
                fnPreSuccess: null,
                fnBeforeSend: $._settings.fnDefaultAjaxBeforeSend,
                fnPreCall: null,
                fnPostCall: fnPostCall,
                isTemplateUsed: false,
                parentTemplateIdToRefresh: null,
                templateId: null,
                isJsonArrayRefreshed: false,
                isJsonSerialized: false,
                isAsyncCall: true,
                fnServerValidationSuccess: null,
                fnServerValidationError: null,
                isDialogRefreshed: false
            }, opt);

            $._log('AJAX.ajaxRefresh : fragment=[' + settings.fragmentIdToRefresh + '|' + settings.id + '] - dispatchValue=[' + settings.dispatchValue + '|' + settings.call + '] - action=[' + settings.action + ']');

            if (settings.fragmentIdToRefresh === null && settings.id === null && settings.isFragmentRefreshed && !settings.isTemplateUsed && !settings.isJsonArrayRefreshed) {
                $._log('====>WARNING : At least a fragment id to be refreshed must be provided if template is not used and if not a direct json injection');
                return;
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            $.__AJAX.ajax({
                callType: settings.callType,
                isDataTypeHtml: settings.isDataTypeHtml,
                isDataTypeJson: settings.isDataTypeJson,
                fragmentIdToRefresh: settings.fragmentIdToRefresh,
                id: settings.id,
                isFragmentRefreshed: settings.isFragmentRefreshed,
                pageForm: settings.pageForm,
                pageUrl: settings.pageUrl,
                dispatchValue: settings.dispatchValue,
                paramValue: settings.paramValue,
                call: settings.call,
                callOverride: settings.callOverride,
                action: settings.action,
                isBlockerActive: settings.isBlockerActive,
                fnPostCall: settings.fnPostCall,
                fnBeforeSend: settings.fnBeforeSend,
                fnPreCall: settings.fnPreCall,
                fnPreSuccess: settings.fnPreSuccess,
                isTemplateUsed: settings.isTemplateUsed,
                parentTemplateIdToRefresh: settings.parentTemplateIdToRefresh,
                templateId: settings.templateId,
                isJsonArrayRefreshed: settings.isJsonArrayRefreshed,
                isAsyncCall: settings.isAsyncCall,
                fnServerValidationSuccess: settings.fnServerValidationSuccess,
                fnServerValidationError: settings.fnServerValidationError,
                isDialogRefreshed: settings.isDialogRefreshed
            });
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxCall
         * @description todo
         * @example todo
         */
        _ajaxCall: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                call: null,
                callOverride: null,
                dispatchValue: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null
            }, opt);

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            $.__AJAX.ajax({
                isFragmentRefreshed: false,
                pageForm: settings.pageForm,
                pageUrl: settings.pageUrl,
                call: settings.call,
                callOverride: settings.callOverride,
                dispatchValue: settings.dispatchValue,
                action: settings.action,
                paramValue: settings.paramValue,
                isBlockerActive: settings.isBlockerActive,
                fnPostCall: settings.fnPostCall,
                fnPreCall: settings.fnPreCall,
                fnPreSuccess: settings.fnPreSuccess
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonPost
         * @description todo
         * @example todo
         */
        _ajaxJsonPost: function (opt) {

            var settings = $.extend({
                isFragmentRefreshed: true,
                fragmentIdToRefresh: null,
                id: null,
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: true,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null,
                jsonArrayPosted: null,
                isJsonInHtml: false
            }, opt);

            if (settings.jsonArrayPosted === null) {
                $._log('AJAX._ajaxJsonPost : ERROR : jsonArrayPosted cannot be null');
                return;
            }
            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }


            var isDataTypeJson, isDataTypeHtml, isJsonArrayRefreshed;

            if (settings.fragmentIdToRefresh !== null || settings.id !== null || settings.isJsonInHtml) {
                isDataTypeHtml = true;
                isDataTypeJson = false;
                isJsonArrayRefreshed = false;
            } else {
                isDataTypeJson = true,
                isDataTypeHtml = false,
                isJsonArrayRefreshed = true;
                $._settings.isInitDisplayOnAjaxStop = false;
            }


            return $.__AJAX.ajax({
                        isFragmentRefreshed: settings.isFragmentRefreshed,
                        fragmentIdToRefresh: settings.fragmentIdToRefresh,
                        id: settings.id,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isJsonArrayPosted: true,
                        jsonArrayPosted: settings.jsonArrayPosted,
                        isDataTypeJson: isDataTypeJson,
                        isDataTypeHtml: isDataTypeHtml,
                        isJsonArrayRefreshed: isJsonArrayRefreshed
                    });

        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonGet
         * @description todo
         * @example todo
         */
        _ajaxJsonGet: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                isAsyncCall:false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null
            }, opt);

            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            //$._settings.isInitDisplayOnAjaxStop = false;

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            return $.__AJAX.ajax({
                        callType: 'GET',
                        isFragmentRefreshed: false,
                        fragmentIdToRefresh: null,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isAsyncCall: settings.isAsyncCall,
                        isDataTypeJson: true,
                        isDataTypeHtml: false,
                        isJsonArrayRefreshed: true
                    });

        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _checkNotifyJsonMessages
         * @description todo
         * @example todo
         */
        _checkNotifyJsonMessages: function(jsonArrayMessages, fnSuccess) {

            if ( jsonArrayMessages.length !== 0 ) {
                _.each(_.pluck(jsonArrayMessages,'messageText'), function(messageText) {
                    $._notifyError( messageText);
                });
            } else {
                $._execFn(fnSuccess);
            }

        },



        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonRefreshTemplate
         * @description todo
         * @example todo
         */
        _ajaxJsonRefreshTemplate: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                isAsyncCall:false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null,
                templateId: null
            }, opt);

            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            if (settings.templateId === null) {
                $._log('ERROR : a template id must be provided');
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            return $.__AJAX.ajax({
                        callType: 'GET',
                        isFragmentRefreshed: false,
                        fragmentIdToRefresh: null,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isAsyncCall: settings.isAsyncCall,
                        isDataTypeJson: true,
                        isDataTypeHtml: false,
                        isJsonArrayRefreshed: true,
                        templateId: settings.templateId,
                        isTemplateUsed: true
                    });

        },



        /* ======================================================= */
        /* ===            INLINE EDITION FUNCTIONS              == */
        /* ======================================================= */

        __inlineIsEdit: false,
        __inlineEditMode: null,
        __$inlineEditCallerRow: null,
        __$inlineEditCallerTable: null,

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineAddNew
         * @description todo
         * @example todo
         */
        _inlineAddNew: function (opt) {
            var settings = $.extend({
                $caller: null,
                fixedFragmentId: 'add_new_fragment',
                dispatchValue: null,
                fnPreCall: null,
                isAutoScrollToId: true,
                editModeParamName: 'editMode'
            }, opt);

            if ($.__inlineIsEdit) {
                return;
            }
            if (settings.$caller === null || settings.dispatchValue === null) {
                return;
            }


            if (settings.fixedFragmentId === 'add_new_fragment') {
                settings.$caller.closest('.box').find('.content').prepend('<div id="add_new_fragment" class="hidden"></div>');
            }

            $.__inlineIsEdit = true;
            $.__inlineEditMode = 'INSERT';

            $.__AJAX.ajax({
                id: settings.fixedFragmentId,
                call: settings.dispatchValue,
                isBlockerActive: false,
                fnPreCall: function () {
                    $._setParamValue(settings.editModeParamName, $.__inlineEditMode);
                    if (settings.fnPreCall !== null && typeof settings.fnPreCall === 'function') {
                        settings.fnPreCall();
                    }
                },
                fnPostCall: function () {
                    var $addNewFragment = $('#' + settings.fixedFragmentId),
                        $title = $addNewFragment.find('.inner-box').find('h1');
                    $title.text($title.text().replace('$EDIT_TITLE$', "Add new"));
                    $addNewFragment.removeClass('hidden');
                    if (settings.isAutoScrollToId) {
                        $._scrollToId(settings.fixedFragmentId);
                    }
                    $('#action_add_new').hide();
                }
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineEdit
         * @description todo
         * @example todo
         */
        _inlineEdit: function (opt) {

            var settings = $.extend({
                $caller: null,
                fixedFragmentId: 'edit_fragment',
                dispatchValue: null,
                call: null,
                paramValue: null,
                fnPreCall: null,
                isAutoScrollToId: true,
                editBoxTitle: 'EDIT',
                editModeParamName: 'editMode',
                actionSaveId: 'action_edit_inline_save',
                actionSaveTitle: 'SAVE',
                actionCancelId: 'action_edit_inline_cancel',
                actionCancelTitle: 'cancel'
            }, opt);

            if ($.__inlineIsEdit) {
                return;
            }

            $.__inlineIsEdit = true;
            $.__inlineEditMode = 'EDIT';
            $.__$inlineEditCallerRow = settings.$caller.closest('tr');
            $.__$inlineEditCallerTable = settings.$caller.closest('table');

            if (settings.fixedFragmentId === 'edit_fragment') {
                $.__$inlineEditCallerRow.after('<tr id="edit_container" class="sub-row hidden"><td id="edit_fragment" colspan="' + settings.$caller.closest('table').find('th').length +'"></td></tr>');
            }

            $.__AJAX.ajax({
                id: settings.fixedFragmentId,
                dispatchValue: settings.dispatchValue,
                call: settings.call,
                paramValue: settings.paramValue,
                isBlockerActive: false,
                fnPreCall: function () {
                    $._setParamValue(settings.editModeParamName, $.__inlineEditMode);
                    if (settings.fnPreCall !== null && typeof settings.fnPreCall === 'function') {
                        settings.fnPreCall();
                    }
                },
                fnPostCall: function () {
                    var $fixedFragment = $('#' + settings.fixedFragmentId);
                    var $innerBox = $fixedFragment.find('.inner-box');
                    if (!$innerBox.length) {
                        $innerBox = $fixedFragment.children().wrap('<div class="inner-box blue clear"></div>');
                        $fixedFragment.children().prepend('<h1>' + settings.editBoxTitle + '</h1>');
                    }
                    $fixedFragment.append('<a id="' + settings.actionCancelId + '" class="button-link cr-pointer fr"><span class="button alternate gray_button no-icon"><span><span><em>' + settings.actionCancelTitle + '</em></span></span></span></a>');
                    $fixedFragment.append('<a id="' + settings.actionSaveId + '" class="button-link cr-pointer fr"><span class="button alternate blue_button"><span><span><b class="icon-button icon-bw-save"></b><em>' + settings.actionSaveTitle + '</em></span></span></span></a>');

                    $.__$inlineEditCallerTable.find('td.action a').addClass('hidden');
                    $('#edit_container').removeClass('hidden');
                    if (settings.isAutoScrollToId) {
                        $._scrollToId(settings.fixedFragmentId);
                    }
                }
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineRemoveEditFragment
         * @description todo
         * @example todo
         */
        _inlineRemoveEditFragment: function () {
            if ($.__inlineEditMode === 'INSERT') {
                $('#add_new_fragment').remove();
            } else {
                $('#edit_container').remove();
            }

            $.__$inlineEditCallerTable.find('td.action a').removeClass('hidden');
            $('#action_add_new').show();

            $.__inlineIsEdit = false;
            $.__inlineEditMode = null;
            $.__$inlineEditCallerRow = null;
            $.__$inlineEditCallerTable = null;

        }




    });
}(jQuery));











/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF BROWSER MODULE                   == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __BROWSER: {


            isCheckBrowserSupported: function () {
                var browserVersion,
                    isSupported = true;

                if ($._settings.isBrowserDetectionEnabled) {

                    browserVersion = $.__BROWSER.getBrowserVersion();

                    if ($._isIE()) {
                        if (browserVersion < 8) {
                            isSupported = false;
                        }
                    }
                    if ($._isFirefox()) {
                        if (browserVersion < 5) {
                            isSupported = false;
                        }
                    }
                    if ($._isChrome()) {
                        if (browserVersion < 4) {
                            isSupported = false;
                        }
                    }
                    if ($._isOpera()) {
                        if (browserVersion < 9) {
                            isSupported = false;
                        }
                    }
                    if ($._isSafari()) {
                        if (browserVersion < 3) {
                            isSupported = false;
                        }
                    }

                }

                if (!isSupported) {
                    $._$main.addClass('hidden');
                    $._$header.addClass('hidden');
                    $._$footer.addClass('hidden');
                    if (!$('#unsupported_browser').length) {
                        var unsupportedContent = '';
                        unsupportedContent += '<div id="unsupported_browser" style="text-align:center;font-family:Arial,sans-serif;font-size:20px;font-weight:bold;"><br>';
                        unsupportedContent += 'Unsupported browser<br>';
                        unsupportedContent += '<span style="font-size:14px;font-weight: normal;">Your browser version is not supported</span><br><br>';
                        unsupportedContent += '</div>';
                        $._$body.html(unsupportedContent);
                    }
                }

                browserVersion = null;

                return isSupported;

            },


            checkBrowserWarning: function () {

                if (!$('#header-warning').length) {
                    $('#header').after('<div id="header-warning"></div>');
                }

                var browserVersion = $.__BROWSER.getBrowserVersion(),
                    isWarning = false,
                    $headerWarning = $('#header-warning'),
                    $browserWarning = $('#browser-warning');

                if ($._isIE()) {
                    if (browserVersion < 8) {
                        isWarning = true;
                    }
                }
                if ($._isFirefox()) {
                    if (browserVersion < 10) {
                        isWarning = true;
                    }
                }
                if ($._isChrome()) {
                    if (browserVersion < 10) {
                        isWarning = true;
                    }
                }
                if ($._isOpera()) {
                    isWarning = true;
                }
                if ($._isSafari()) {
                    if (browserVersion < 4) {
                        isWarning = true;
                    }
                }

                if (isWarning) {
                    if (!$browserWarning.length) {
                        $headerWarning.show().append('<li id="browser-warning">' + $._getData('jscaf_common_browser_warning') + '</li>');
                    }
                } else {
                    $browserWarning.remove();
                }

                if (!$headerWarning.children().length) {
                    $headerWarning.remove();
                }

                browserVersion = isWarning = $browserWarning = $headerWarning = null;

            },

            getBrowserName: function () {
                if ($._isIE()) {
                    return 'Internet Explorer';
                }
                if ($._isFirefox()) {
                    return 'Firefox';
                }
                if ($._isSafari()) {
                    return 'Safari';
                }
                if ($._isChrome()) {
                    return 'Chrome';
                }
                if ($._isOpera()) {
                    return 'Opera';
                }
                return 'Other';
            },

            getBrowserShortName: function () {
                if ($._isIE()) {
                    return 'ie';
                }
                if ($._isFirefox()) {
                    return 'ff';
                }
                if ($._isSafari()) {
                    return 'saf';
                }
                if ($._isChrome()) {
                    return 'chr';
                }
                if ($._isOpera()) {
                    return 'op';
                }
                return 'Other';
            },

            getBrowserVersion: function () {
                if ($._isIE()) {
                    var defaultVersion = parseInt(navigator.userAgent.substr(navigator.userAgent.indexOf('MSIE') + 5, 3), 10);
                    if (defaultVersion === 7) {
                        if (navigator.userAgent.indexOf('Trident/4.0') > 0) {
                            return 8;
                        }
                        else if (navigator.userAgent.indexOf('Trident/5.0') > 0) {
                            return 9;
                        }
                        else {
                            return 7;
                        }
                    } else {
                        return defaultVersion;
                    }
                }
                if ($._isFirefox()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Firefox') + 8, 7), 10);
                }
                if ($._isSafari()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Safari') + 7, 10), 10);
                }
                if ($._isChrome()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Chrome') + 7, 8), 10);
                }
                if ($._isOpera()) {
                    return  parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Opera') + 6, 4), 10);
                }

                return null;
            },


            getPageZoom: function () {

                var returnValue = 'N/A';

                if ($._isIE()) {
                    var version = $.__BROWSER.getBrowserVersion(), b;

                    if (version === 7) {
                        b = document.body.getBoundingClientRect();
                        returnValue = ((b.right - b.left) / document.body.clientWidth).toFixed(2);
                    } else if (version === 8) {
                        returnValue = (screen.deviceXDPI / screen.systemXDPI).toFixed(2);
                        if (isNaN(returnValue)) {
                            //for IE7 detected as IE8, force IE7 detection
                            b = document.body.getBoundingClientRect();
                            returnValue = ((b.right - b.left) / document.body.clientWidth).toFixed(2);
                        }
                    }
                }

                return returnValue;

            },


            checkPageZoomWarning: function () {
                var $pageZoomWarning = $('#pagezoom-warning');

                if (!$('#header-warning').length) {
                    $('#header').after('<div id="header-warning"></div>');
                }

                var $headerWarning = $('#header-warning'),
                    pageZoom = $.__BROWSER.getPageZoom();

                if (pageZoom !== 'N/A' && $.trim(pageZoom) !== '1.00') {
                    if (!$pageZoomWarning.length) {
                        $headerWarning.append('<li id="pagezoom-warning">' + $._getData('jscaf_common_page_zoom_warning') + '</li>');
                    }
                } else {
                    $pageZoomWarning.remove();
                }

                if (!$headerWarning.children().length) {
                    $headerWarning.remove();
                }

                $headerWarning = pageZoom = null;
            }


    

        },


        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isTouchDevice
         * @description todo
         * @example todo
         */
        _isTouchDevice: function () {
            try {
                document.createEvent("TouchEvent");
                return true;
            } catch (e) {
                return false;
            }
        },        

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE
         * @description todo
         * @example todo
         */
        _isIE: function () {
            return (navigator.userAgent.toUpperCase().indexOf('MSIE') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE7
         * @description todo
         * @example todo
         */
        _isIE7: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 7 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE8
         * @description todo
         * @example todo
         */
        _isIE8: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 8 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE9
         * @description todo
         * @example todo
         */
        _isIE9: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 9 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isFirefox
         * @description todo
         * @example todo
         */
        _isFirefox: function () {
            return (navigator.userAgent.toUpperCase().indexOf('FIREFOX') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isSafari
         * @description todo
         * @example todo
         */
        _isSafari: function () {
            return (navigator.userAgent.toUpperCase().indexOf('SAFARI') > 0 &&
                navigator.userAgent.toUpperCase().indexOf('CHROME') < 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isChrome
         * @description todo
         * @example todo
         */
        _isChrome: function () {
            return (navigator.userAgent.toUpperCase().indexOf('CHROME') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isOpera
         * @description todo
         * @example todo
         */
        _isOpera: function () {
            return (navigator.userAgent.toUpperCase().indexOf('OPERA') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isOther
         * @description todo
         * @example todo
         */
        _isOther: function () {
            return (!$._isIE() && !$._isFirefox() && !$._isSafari() && !$._isChrome() && !$._isOpera());
        },




        //old support of browser.msie and browser.mozilla functions dropped in jQuery 1.9
        browser: {
            msie: function () {
                return $._isIE();
            },
            mozilla: function () {
                return $._isFirefox();
            }
        }        


    });
}(jQuery));
















/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/


/* ======================================================= */
/* ===                 JSCAF CORE                       == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({

        __coreModuleIdentifier: function() {
            return true;
        },


        //loaded during main document.ready
        __modules: {},

        //jSCAF SETTINGS
        //--------------

        /**
         * @see CORE.JS
         * @class .
         * @memberOf $
         * @name _settings
         * @description todo
         * @example todo
         */

        _settings: {

            appName: 'APP',
            appVersion: null,
            appBuildDate: null,
            appDevelopmentBuild: true,
            contextPath: null,
            language: 'en',
            appBuildTimestamp: '',

            isLoggingEnabled: true, //TODO : DEPRECATE ! => replaced by delog ANT target at build time, to avoid calling the $._L / $._log helpers on PROD env.
            onlineHelpRootPath: '/help',

            dateFilterPrefix: null,
            dateFilterFormatName: null,
            isFormParamDispatchValueUsed: false,
            isFormParamActionUsed: false,

            fnDefaultAjaxBeforeSend: function (jqXHR, settings) {},
            fnDisplayErrorDialogOverride: null,
            isBrowserDetectionEnabled: true,
            isBrowserWarningEnabled: true,
            isPageGenerationTimeDisplayed: true,
            isPageZoomWarningEnabled: true,
            isFieldValidationUpdateDisplay: false,
            ecasRedirectFailedUrl: '/pages/outOfSession.html',
            isCleanIE9AjaxHtmlTags: false,
            isFormDispatchCallTypeUsed: false,
            isDisplaySettingsUsed: true,
            prototypeInnerFragmentPageUrl: null,
            hasPageInnerFragment: false,
            innerFragmentLoadActionName: 'load',
            innerFragmentName: 'innerFragment',
            isInitDisplayOnAjaxStop: false,
            isAjaxErrorDisplay: true,
            isAjaxErrorNotificationDisplayOnly: false,
            fnPageInitDisplayCallback: 'p.initDisplay',
            fnPageBindEventsCallback: 'p.bindEvents',
            isI18nActive: false,
            i18nResourcesLocation: null,
            i18nResourcesBundleName: null,
            isResponsiveTopBar: true,
            isPageDefaultInit: true,
            fnPageInitOverrideCallback: null,
            pageFormNameOverride: null,
            pageActionUrlOverride: null,
            isOnBeforeUnloadActive: false,
            timeoutDialogProperties: {},
            isLightInitialisationActive: false,
            fnInitPreCall: null,
            fnInitPostCall: null,
            isSinglePageApp: false,
            isFlatThemeActive: false,
            isPrototypeMode: false,
            isDisableEnterKeyOnForm: true,
            jscafRootUrl: null,
            isMinimalScreenWidthDetectionActive: true,
            isMinimalScreenWidthDetectionActiveForIE78Only: false,
            isKeepTopNavigationFixed: false,

            JScomponents: null,
            appComponents: null,

            isBootstrapActive: false,
            isBootstrapPrimaryActive: false,
            isSlidebarsActive: false,

            isBlockOnLoadActive: true,


            koActive: false,
            koModelJsonGet: null,
            koViewModel: null

        },


        //GLOBAL CACHED ELEMENTS
        //----------------------
        _$main: null,
        _main: null,
        _$mainContent: null,
        _$form: null,
        _$html: null,
        _html: null,
        _$body: null,
        _body:null,
        _$header: null,
        _$footer: null,
        _$document: null,
        _$slidebars: null,


        //LOCAL JSCAF VARIABLES
        //---------------------
        __pageStartTime: new Date(),
        __isInitPhase: true,


        //COMPONENTS DECLARATION
        //----------------------
        __cmp: $.__cmpDefs,

        __cmpDefs: {

            //bundled components

            JSapproveToggle: false,
            JSautocomplete: false,
            JSbuttonSet: false,
            JSfieldNumber: false,
            JSdatepicker: false,
            JStooltip: false,
            JSdialog: false,
            JSslider: false,
            JSradio: false,
            JScheckbox: false,
            JSstars: false,
            JStopNav: false,
            JStopBar: false,
            JStableSorter: false,
            JStableSorterServer: false,
            JSlistCounterSmall: false,
            JScounterBox: false,
            JSpagination: false,
            JStableFilter: false,
            JSfileInput: false,
            JShelp: false,
            JStoggleButton: false,
            JSselect2: false,
            JSsortable: false,
            JSmaxLength: false,
            JSmaskedInput: false,
            JSbsPopover: false,
            JSradioList: false,
            JSopenDialogContent: false,
            JSopenPdfDialog: false,
            JSopenVideoDialog: false,
            JSaccordionBox: false,
            JSsummernote: false,
            JSlistsTransfer: false,
            JSviewDocument: false,

            //plugins

            JSinlineEdit: false,
            JSwizard: false
        },



        /* ======================================================= */
        /* ===             PRE-INIT FUNCTIONS @doc.ready        == */
        /* ======================================================= */

        __initCachedElements: function () {
            $._log('CORE.initCachedElements');

            $._$main = $('#main');
            $._$mainContent = $('#main-content');

            //checking if the main and main-content are existing, if not create them and wrap the body content over
            if (!$._$main.length) {
                $._$body.children().wrapAll('<div id="main"></div>');
                $._$main = $('#main');
            }
            if (!$._$mainContent.length) {
                $._$main.children().wrapAll('<div id="main-content"></div>');
                $._$mainContent = $('#main-content');
            }


            //$._$form = $('form');
            $._$form = $._$mainContent.find('form');
            $._$html = $('html');
            $._$body = $('body');
            $._$document = $(document);
            $._$header = $('#header');
            $._$footer = $('#footer');

            $._html = $._$html[0];
            $._body = $._$body[0];
            $._main = $._$main[0];

        },


        __globMessagesArray: $.__globMessagesArray || [],

        __initGlobalMessages: function () {
            $._log('CORE.initGlobalMessages');




        },


        //loads an translate key value pairs on the page
        //an associated json file must be located right besides the page controller with the same root name
        //if page controller = page.js, the json file will be : page_en.json and page_fr.json by default
        //a complete resourceBundle can be provided using $._settings.i18nResourcesLocation + $._settings.i18nResourcesBundleName

        __i18nData: null,

        __initI18n: function () {

            var url;

            if ( $._settings.i18nResourcesLocation === null ) {
                var pageScriptFileName = $('#page_script').attr('src');
                pageScriptFileName = pageScriptFileName.substr(0, pageScriptFileName.indexOf('.'));
                url = pageScriptFileName + '_' + $._settings.language + '.json';
            } else {
                url = $._getContextPath() + $._settings.i18nResourcesLocation + '/' + $._settings.i18nResourcesBundleName + '_' + $._settings.language + '.json';
            }

            $._isBlockerPage = false;

            $.ajax({
                dataType: "json",
                url: url,
                cache: true,
                success: function (data) {
                    $.__i18nData = data;
                }
            });

            $._isBlockerPage = true;

        },

        // called on every initDisplay if active for the current page
        __refreshI18n: function() {
            if ( $.__i18nData !== null ) {
                $('.i18n').each(function () {
                    var $this = $(this),
                        attributes = $this.data();
                    $this.text($.__i18nData[attributes.key]);
                });
            }
        },


        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _initDisplay
         * @type function
         * @default empty
         * @description todo
         * @example todo
         */
        _initDisplay: function (fn) {
            $._log('CORE.initDisplay');

            //init components elements defined in $document.ready of the page script
            if ($.__modules.COMPONENTS) {
                $.__COMPONENTS.initDisplay();
            }


            //calling application initDisplay function
            if ($._settings.fnAppInitDisplay !== null && !$._settings.isSinglePageApp) {

                if (typeof window[$._settings.appName].initDisplay === 'function') {
                    $._execFn(window[$._settings.appName].initDisplay);
                }

            }


            //calling page initDisplay
            if ($.__isFunctionExists($._settings.fnPageInitDisplayCallback) && !$._settings.isSinglePageApp) {

                //check if the page initDisplay function does not contain a call to the _initDisplay (to avoid infinite loop)

                var objLiteral = null,
                    fnString = $._settings.fnPageInitDisplayCallback,
                    sep = fnString.indexOf('.'),
                    initDisplayPos;

                fn = null;

                if (sep > 0) {
                    objLiteral = fnString.substring(0, sep);
                    fn = fnString.substring(sep + 1);

                    initDisplayPos = window[objLiteral][fn].toString().indexOf('$._initDisplay');

                } else {
                    fn = fnString;

                    initDisplayPos = window[fn].toString().indexOf('$._initDisplay');
                }


                if (initDisplayPos < 0) {
                    $._execFn($._settings.fnPageInitDisplayCallback, true);
                } else {
                    throw new Error('ERROR !!!! initDisplay CANNOT BE CALLED WITHIN your PAGE initDisplay function');
                }


                objLiteral = fn = fnString = sep = initDisplayPos = null;

            }


            //by default every time the ajaxStop global events is triggered, the _initDisplay() is called,
            //this can be set to false on the fnPreCall option of the ajax call (using $._ajax function) to avoid
            //this automatic initDisplay and if you want to handle this by yourself.
            $._settings.isInitDisplayOnAjaxStop = true;

            //fill client-height when not done at first init (because of the page was full filled)
            $.__fillClientHeight();

            //activate Syze plugin on page init
            if ($.__modules.DISPLAY) {
                $.__DISPLAY.syze();
            }

            //refresh i18n if active for the current page
            if ( $._settings.isI18nActive ) {
                $.__refreshI18n();
            }

            //init slidebars plugin if activated
            if ( $._settings.isSlidebarsActive ) {
                $._slidebars = new $.slidebars();
                if ( $._settings.isSlidebarsOpen ) {
                    $._slidebars.slidebars.open('left');
                }
            }

            //calling callback function if provided
            $._execFn(fn, true);

        },


        __fillClientHeight: function () {
            if (!$._settings.isBootstrapPrimaryActive) { //don't execute this if bootstrap is the primary lib

                if ($._body.offsetHeight < screen.height) {

                    var height;
                    if ($._hasClass($._html,'ismobile')) {
                       height = (window.innerHeight || document.documentElement.clientHeight) - $('#header').height();
                    } else {
                       height = (window.innerHeight || document.documentElement.clientHeight) - $('#header').height() - $('#footer').height();
                    }

                    if ( $._settings.isFlatThemeActive ) {
                        height -= 50;
                    }  else {
                        if (!$('#header').length) {
                            height -= 30;
                        } else {
                            height -= 140;
                        }
                    }

                    $._log('CORE.fillClientHeight => height:' + height);

                    //fill the client height to adjust same height on smaller height pages
                    if ($._$main[0] !== undefined && $._$mainContent[0] !== undefined) {
                        $._$main[0].style.minHeight = height + 'px';
                        $._$mainContent[0].style.minHeight = height + 'px';
                    }

                    if ($.__modules.SPA) {
                        var pageContainer = $._getById('spa-page-container');
                        if (pageContainer !== null) {
                            pageContainer.style.minHeight = height + 20 + 'px';
                        }
                    }

                    height = null;
                }
            }
        },





        /* ============================================================================================== */
        /* ===             BROWSER DETECTION in case BROWSER module is not declared                    == */
        /* ============================================================================================== */

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE
         * @description todo
         * @example todo
         */
        _isIE: function() {
            return $._hasClass($._html,'ie');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE7
         * @description todo
         * @example todo
         */
        _isIE7: function() {
            return $._hasClass($._html,'ie7');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE8
         * @description todo
         * @example todo
         */
        _isIE8: function() {
            return $._hasClass($._html,'ie8');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE9
         * @description todo
         * @example todo
         */
        _isIE9: function() {
            return $._hasClass($._html,'ie9');
        },






        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getContextPath
         * @description todo
         * @example todo
         */
        _getContextPath: function () {
            var returnContextPath = null;

            try {
                if ($._settings.contextPath === null) {
                    returnContextPath = '..';
                } else {
                    returnContextPath = $._settings.contextPath;
                }
            } catch (err) {
                returnContextPath = '..';
            }

            if (returnContextPath === null) {
                returnContextPath = '..';
            }
            return returnContextPath;
        },




        // BLOCKER DURING REQUEST
        // ----------------------

        _isBlockerPage: true,
        _blockUIMessageInfo: null,


        _getBlockUIMessage: function () {

            if ($._blockUIMessageInfo !== null) {
                return  '<div class="blocker-wrapper">' +
                    '<br><div class="loader"></div>' +
                    '<br>' +
                    '<span class="blocker">' +
                    $._getData('jscaf_common_wait') + '<br>' +
                    '<span class="blocker">' +
                    $._blockUIMessageInfo +
                    '</span><br>' +
                    '</span><br><br>' +
                    '</div>';
            } else {
                return  '<div class="blocker-wrapper">' +
                    '<br><div class="loader"></div>' +
                    '<br>' +
                    '<span class="blocker">' +
                    $._getData('jscaf_common_wait') +
                    '</span><br><br>' +
                    '</div>';
            }

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _blockUI
         * @description todo
         * @example todo
         */
        _blockUI: function (postCall) {
            $._log('CORE.blockUI');

            $.blockUI({
                message: $._getBlockUIMessage(),
                overlayCSS: { backgroundColor: '#7c7c7d' },
                baseZ: 55000, // overlay popup
                fadeIn: 0,
                fadeOut: 0
            });

            // Remove UI message otherwise it will be reused for all blocker without messages.
            $._blockUIMessageInfo = null;

            $._execFn(postCall, true);
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _unblockUI
         * @description todo
         * @example todo
         */
        _unblockUI: function (postCall) {
            $._log('CORE.unblockUI');

            document.body.style.cursor = "default";
            $.unblockUI();

            $._execFn(postCall, true);

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _elementBlock
         * @description todo
         * @example todo
         */
        _elementBlock: function (o,isShowMessage) {

            if (isShowMessage === undefined) {
                isShowMessage = true;
            }

            if (isShowMessage) {
                o.block({
                    message: $._getBlockUIMessage(),
                    overlayCSS: { backgroundColor: '#7c7c7d' },
                    baseZ: 55000 // overlay popup
                });
            } else {
                o.block({
                    message: null,
                    overlayCSS: { backgroundColor: '#7c7c7d' },
                    baseZ: 55000 // overlay popup
                });
            }

            // Remove UI message otherwise it will be reused for all blocker without messages.
            $._blockUIMessageInfo = null;
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _elementUnblock
         * @description todo
         * @example todo
         */
        _elementUnblock: function (o) {
            o.unblock();
        },



        /* ======================================================= */
        /* ===             COMMON PAGE FUNCTIONS                == */
        /* ======================================================= */


        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getPageForm
         * @description todo
         * @example todo
         */
        _getPageForm: function () {
            if ($._settings.pageFormNameOverride === null) {
                var f = $($._$form[0]).attr('id');

                if (f === undefined) {
                    return null;
                } else {
                    return f;
                }

            } else {
                return $._settings.pageFormNameOverride;
            }
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getPageUrl
         * @description todo
         * @example todo
         */
        _getPageUrl: function (formActionOverride) {
            $._log('CORE.getPageUrl');

            var pageUrl = null,
                formId = $._getPageForm();

            if ($._settings.pageActionUrlOverride === null) {
                if (formActionOverride === null || formActionOverride === undefined) {
                    if (formId !== null) {
                        pageUrl = $._getAttr($._getById(formId),'action').replace(',DanaInfo=www.cc.cec+', '');
                    } else {
                        if ($._$form[0] !== undefined) {
                            pageUrl = $._getAttr($._$form[0],'action').replace(',DanaInfo=www.cc.cec+', '');
                        }
                    }
                } else {
                    pageUrl = formActionOverride;
                }

                if (pageUrl !== undefined && pageUrl !== null) {
                    if ($('#dstb-id').length /*|| typeof(DSHost) != 'undefined'*/) {
                        pageUrl = pageUrl.replace('https://myremote.ec.europa.eu', '')
                            .replace(',DanaInfo=www.cc.cec+', '')
                            .replace(',DanaInfo=www.cc.cec,dom=1,CT=sxml+', '');
                    }
                } else {
                    return null;
                }

            } else {
                pageUrl = $._getContextPath() + $._settings.pageActionUrlOverride;
            }


            if (pageUrl.indexOf($._getContextPath()) < 0 && pageUrl !== 'PROTOTYPE') {
                if (pageUrl.indexOf('http://')>=0) {
                    pageUrl = $._getContextPath() + pageUrl.substr(7).substr(pageUrl.substr(7).indexOf('/'));
                } else {
                    if (pageUrl.indexOf('https://')>=0) {
                        pageUrl = $._getContextPath() + pageUrl.substr(8).substr(pageUrl.substr(8).indexOf('/'));
                    } else {
                        pageUrl = $._getContextPath() + pageUrl;
                    }
                }
            }


            return pageUrl;
        },




        //todo : TO BE DEPRECATED and replaced by _submitForm2 only (temporary)
        _submitForm: function (dispatchValue, action, isBlocker, fnPostCall, formId, isFormInIframe) {
            $._submitForm2({
                dispatchValue: dispatchValue,
                action: action,
                isBlocker: isBlocker,
                fnPostCall: fnPostCall,
                formId: formId,
                isFormInIframe: isFormInIframe
            });
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _submitForm2
         * @description todo
         * @example todo
         */

        //todo: TO BE RENAMED TO submitForm when the older one will be removed
        _submitForm2: function (opt) {

            var settings = $.extend({
                call: null,
                dispatchValue: null,
                action: null,
                isBlocker: true,
                fnPostCall: null,
                formId: null,
                isFormInIframe: false,
                isFormInObject: false,
                isIframeInDialog: false,
                validateElementId: null
            }, opt);

            var dispatchValue = null;
            var $dispatchValue;

            if (settings.call !== null) {
                dispatchValue = settings.call;
            } else {
                dispatchValue = settings.dispatchValue;
            }

            $._log('CORE.submitForm : ' + $._getPageForm() + ' - dispatchValue:' + dispatchValue + ' - action:' + settings.action);

            if (settings.isBlocker) {
                if (settings.isIframeInDialog) {
                    $._elementBlock($('.ui-dialog'));
                } else {
                    $._blockUI();
                }
            }

            if (dispatchValue !== null) {

                if (settings.formId !== null) {

                    if (!settings.isFormInIframe && !settings.isFormInObject) {

                        if ($('iframe').length) {
                            settings.isFormInIframe = true;
                        }

                        if ($('object').length) {
                            settings.isFormInObject = true;
                        }
                    }

                    if (settings.isFormInIframe) {
                        $dispatchValue = $('iframe').contents().find('#' + settings.formId).find('#dispatchValue');
                    } else {
                        if (settings.isFormInObject) {
                            $dispatchValue = $($('object').get(0).contentDocument).find('#dispatchValue');
                        } else {
                            $dispatchValue = $('#' + settings.formId).find('#dispatchValue');
                        }
                    }


                } else {
                    $dispatchValue = $('#dispatchValue');
                }

                if ($dispatchValue.length) {
                    $dispatchValue.val(dispatchValue);
                } else {
                    if ($._settings.isFormParamDispatchValueUsed) {
                        $._setParamValue('dispatchValue', dispatchValue, false, settings.formId, settings.isFormInIframe);
                    }
                }
            }
            if (settings.action !== null) {
                var $action;

                if (settings.formId !== null) {
                    if (settings.isFormInIframe) {
                        $action = $('iframe').contents().find('#' + settings.formId).find('#action');
                    } else {
                        if (settings.isFormInObject) {
                            $dispatchValue = $($('object').get(0).contentDocument).find('#action');
                        } else {
                            $action = $('#' + settings.formId).find('#action');
                        }
                    }
                } else {
                    $action = $('#action');
                }


                if ($action.length) {
                    $action.val(settings.action);
                } else {
                    if ($._settings.isFormParamActionUsed) {
                        $._setParamValue('action', settings.action, false, settings.formId, settings.isFormInIframe);
                    }
                }
            }

            //set the submit call type explicitly to be known server-side
            if ($._settings.isFormDispatchCallTypeUsed) {
                $._setParamValue('dispatchCallType', 'SUBMIT', false, settings.formId, settings.isFormInIframe);
            }

            $._injectPageParams({
                formId: settings.formId,
                isFormInIframe: settings.isFormInIframe,
                isFormInObject: settings.isFormInObject
            });

            if (settings.formId !== null) {

                if (settings.isFormInIframe) {
                    $('iframe').contents().find('#' + settings.formId).submit();
                } else {
                    if (settings.isFormInObject) {
                        $($('object').get(0).contentDocument).find('#' + settings.formId).submit();
                    } else {
                        $('#' + settings.formId).submit();
                    }
                }
            } else {
                $._$form.submit();
            }


            if (settings.fnPostCall !== null) {
                var isValid = true;

                if (settings.validateElementId !== null) {
                    if (settings.isFormInIframe) {
                        isValid = $('iframe').contents().find('#' + settings.validateElementId).length === 0;
                    } else if (settings.isFormInObject) {
                        isValid = $($('object').get(0).contentDocument).find('#' + settings.validateElementId).length === 0;
                    }
                }

                if (isValid) {
                    setTimeout(settings.fnPostCall, 1000);
                } else {
                    $._unblockUI();
                }
            }
        },















        // COMPONENTS
        // ==========

        _pageComponents: [],

        _registerComponent: function (cmp) {
            $._pageComponents.push(cmp);
        },

        _pageComponentsInitDisplay: function () {
            $._log('CORE.pageComponentsInitDisplay');

            //executing initDisplay of each page components if exist
            if ($._pageComponents.length) {
                var c;
                for (c = 0; c < $._pageComponents.length; c++) {
                    if (typeof $._pageComponents[c].initDisplay === 'function') {
                        $._pageComponents[c].initDisplay();
                    }
                }
            }
        },

        _pageComponentsBindEvents: function () {
            $._log('CORE.pageComponentsBindEvents');

            //load the sub-components of the page if there are any
            if ($._pageComponents.length) {
                var c;
                for (c = 0; c < $._pageComponents.length; c++) {
                    if (typeof $._pageComponents[c].bindEvents === 'function') {
                        $._pageComponents[c].bindEvents();
                    }
                }
            }
        },



        /* ======================================================= */
        /* ===            SHORTHANDS TO PLUGINS                 == */
        /* ======================================================= */

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _notifyError
         * @description todo
         * @example todo
         */
        _notifyError: function(text) {

            jQuery.noticeAdd({type:'error',text:text});

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _notifySuccess
         * @description todo
         * @example todo
         */
        _notifySuccess: function(text) {

            jQuery.noticeAdd({type:'success',text:text});

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _removeNotifications
         * @description todo
         * @example todo
         */
        _removeNotifications: function() {
            jQuery.noticeRemoveAll($('.notice-item-wrapper'));
        },




        /* ======================================================= */
        /* ===            MINIMAL SHORTHANDS                    == */
        /* ======================================================= */

        _L: function (text, isForced) {
            $._log(text, isForced);
        },
        _I: function (opt) {
            $._init(opt);
        },
        _ID: function() {
            $._initDisplay();
        },


        _GD: function (key) {
            $._getData(key);
        },
        _SD: function (key, value) {
            $._setData(key, value);
        },

        _SF: function (opt) {
            $._submitForm2(opt);
        },

        _AE: function (eventType, selector, fn) {
            $._addEvent(eventType, selector, fn);
        },
        _AEB: function (eventType, selector, fn) {
            $._addEventButtonClick(selector, fn);
        },
        _AEE: function (selector, fn) {
            $._addEventOnEnter(selector, fn);
        },
        _ADE: function (eventType, selector, fn) {
            $._addDocumentEvent(eventType, selector, fn);
        },
        _ADEE: function (selector, fn) {
            $._addDocumentEventOnEnter(selector, fn);
        },
        _SPV: function (name, value, isDate, formId, isFormInIframe) {
            $._setParamValue(name, value, isDate, formId, isFormInIframe);
        },
        _EF: function (aFunction, isSetTimeout) {
            $._execFn(aFunction, isSetTimeout);
        }

    });
}(jQuery));





/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/


/* ======================================================= */
/* ===                 JSCAF UTILS                      == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({


        __utilsModuleIdentifier: function() {
            return true;
        },

 

        /* ======================================================= */
        /* ===             LOGGING FUNCTIONS                    == */
        /* ======================================================= */


        _logStack: [],
        _logStackIdx: 0,
        _logTimerStart: 0,
        _logTimerText: '',

        _forceLog: function (text) {
            $._log(text, true);
        },

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _log
         * @description todo
         * @example todo
         */

        /*
         * @description
         * general logging function, display a timestamp into the console and stores logs history into a logs-array : _logStack[]
         * @class .
         * @name _log
         * @memberOf $
         * @param text {String} the text to be logged <br><i>default : undefined</i>
         * @param isForced {boolean} if the log should bypass the general log switched : _isLoggingEnabled <br><i>default : undefined</i>
         */
        _log: function (text, isForced) {

            var logText = moment().format('DD/MM/YYYY HH:mm:ss...SSS') + ' => ' + text;

            //insert log text in stack
            $._logStack[$._logStackIdx] = text;
            $._logStackIdx++;

            if (typeof console !== "undefined") {
                console.log(logText);
            }

        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _logStart
         * @description todo
         * @example todo
         */
        _logStart: function(text) {
            $._logTimerStart = new Date().getTime();
            $._log(text + ' => START');
            $._logTimerText = text;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _logEnd
         * @description todo
         * @example todo
         */
        _logEnd: function() {
            var duration = new Date().getTime() - $._logTimerStart;
            $._log($._logTimerText + ' => END  ---------> ' + duration + 'ms');
        },





        /* ======================================================= */
        /* ===             STRING FUNCTIONS                     == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _pad
         * @description todo
         * @example todo
         */

        /*
         * @description
         * add 0 leading character length to a string, works like the lpad oracle function for formating number
         * @class .
         * @name _pad
         * @memberOf $
         * @param n {String} the input string <br><i>default : undefined</i>
         * @param len {Number} the length of the leading zeroes string <br><i>default : undefined</i>
         */
        _pad: function (n, len) {
            var s = n.toString();
            if (s.length < len) {
                s = ('0000000000' + s).slice(-len);
            }
            return s;
        },

        _escapeRegExp: function (string) {
            return string.replace(/([.*+?\^=!:${}()|\[\]\/\\])/g, "\\$1");
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _replaceAll
         * @description todo
         * @example todo
         */
        _replaceAll: function(string, find, replace) {
            return string.replace(new RegExp($._escapeRegExp(find), 'g'), replace);
        },


        /* ======================================================= */
        /* ===             DATE FUNCTIONS                       == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getDate
         * @description todo
         * @example todo
         */

        /*
         * @description
         * String TO date converter, use the moment.js library for date manipulation
         * @class .
         * @name _getDate
         * @memberOf $
         * @param s {String} the input date string, accept 3 foramts : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:mm <br><i>default : undefined</i>
         */
        _getDate: function (s) {
            var m = $._getMoment(s);

            if (m === null) {
                return m;
            }

            return m.toDate();

        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getMoment
         * @description todo
         * @example todo
         */


        /*
         * @description
         * String TO moment converter, use the moment.js library for date manipulation
         * @class .
         * @name _getMoment
         * @memberOf $
         * @param s {String} the input date string, accept 3 foramts : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:mm <br><i>default : undefined</i>
         */
        _getMoment://string format is FR format : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:MI
            function (s) {
                if (s === undefined || s === null) {
                    return null;
                }
                if (s.length === 8) { // format = DD/MM/YY
                    return moment(s, 'DD/MM/YY');
                } else {
                    if (s.length === 10) { // format = DD/MM/YYYY
                        return moment(s, 'DD/MM/YYYY');
                    } else {  // format = DD/MM/YYYY HH:MI
                        return moment(s, 'DD/MM/YYYY HH:mm');
                    }
                }
            },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getDateRange
         * @description todo
         * @example todo
         */

        /*
         * @description
         * returns a dates array filled by dates between a start date and a end date
         * @class .
         * @name _getDateRange
         * @memberOf $
         * @param startDate {Date} the start date of the range <br><i>default : undefined</i>
         * @param endDate {Date} the end date of the range <br><i>default : undefined</i>
         */
        _getDateRange: function (startDate, endDate) {
            if (startDate === undefined || endDate === undefined) {
                return [];
            }

            var dateArray = [];
            var currentDate = new Date(startDate);
            var t;
            while (currentDate <= endDate) {
                t = new Date(currentDate);
                dateArray.push(t);
                currentDate.setDate(currentDate.getDate() + 1);
            }
            return dateArray;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isDateEqual
         * @description todo
         * @example todo
         */


        /*
         * @description
         * checks if two dates are equal
         * @class .
         * @name _isDateEqual
         * @memberOf $
         * @param d1 {Date} the first date to be compared <br><i>default : undefined</i>
         * @param d2 {Date} the second date to be compared <br><i>default : undefined</i>
         */
        _isDateEqual: function (d1, d2, isCheckDateOnly) {
            if (d1 === undefined || d2 === undefined) {
                return false;
            } else {
                if (isCheckDateOnly) {
                    return (moment(d1).format('DD/MM/YYYY') === moment(d2).format('DD/MM/YYYY'));
                } else {
                    return ((d1.getTime() - d2.getTime()) === 0);
                }
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isPeriodValid
         * @description todo
         * @example todo
         */

        /*
         * @description
         * checks if a period given the start and end dates is valid, start date <= end date
         * @class .
         * @name _isPeriodValid
         * @memberOf $
         * @param dateFrom {Date} the start date of the period to be checked <br><i>default : undefined</i>
         * @param dateTo {Date} the end date of the period to be checked <br><i>default : undefined</i>
         */
        _isPeriodValid: function (dateFrom, dateTo) {
            if (dateFrom === undefined || dateTo === undefined) {
                return false;
            } else {
                return (dateFrom.getTime() <= dateTo.getTime());
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isPeriodValidNotEqual
         * @description todo
         * @example todo
         */

        /*
         * @description
         * checks if a period given the start and end dates is valid and not equal, start date < end date
         * @class .
         * @name _isPeriodValidNotEqual
         * @memberOf $
         * @param dateFrom {Date} the start date of the period to be checked <br><i>default : undefined</i>
         * @param dateTo {Date} the end date of the period to be checked <br><i>default : undefined</i>
         */
        _isPeriodValidNotEqual: function (dateFrom, dateTo) {
            if (dateFrom === undefined || dateTo === undefined) {
                return false;
            } else {
                return (dateFrom.getTime() < dateTo.getTime());
            }
        },

        _getGenerationTime: function () {
            var t = new Date();
            return (t.getTime() - $.__pageStartTime.getTime()) / 1000 + 's';
        },




        /* ======================================================= */
        /* ===             MISC FUNCTIONS                       == */
        /* ======================================================= */



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToId
         * @description todo
         * @example todo
         */
         _scrollToId: function (id, offset, isHighlighted) {
            var $el = $("#" + id);

            return $._scrollToElement($("#" + id),offset,isHighlighted);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToElement
         * @description todo
         * @example todo
         */
        _scrollToElement: function ($el, offset, isHighlighted) {

            if (!$el.length) {
                return [];
            }

            if (offset === undefined) {
                offset = 0;
            }

            if (isHighlighted === undefined) {
                isHighlighted = false;
            }            

            $('html,body').animate({scrollTop: $el.offset().top - offset});

            if (isHighlighted) {
                $el.effect("highlight", {color: "#ff0000"}, 1500);
            }

            return $el;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToTop
         * @description todo
         * @example todo
         */
        _scrollToTop: function() {
            $('html,body').animate({scrollTop: 0});
        },


        _enableButtons: function () {
            var $buttons = $('span.button');
            if ($buttons.length) {
                $buttons.each(function () {
                    $(this).removeClass('disabled');
                });
            }
        },

        _disableButtons: function () {
            var $buttons = $('span.button');
            if ($buttons.length) {
                $buttons.each(
                    function () {
                        
                        var $this = $(this);

                        if (!$this.hasClass('enabled')) {
                            $this.addClass('disabled');
                            $._$form.off('click','#' + this.id);
                        }
                    }
                );
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getData
         * @description todo
         * @example todo
         */
        _getData: function (key) {
            var i, globMessagesArrayLength = $.__globMessagesArray.length;
            
            for ( i=0; i<globMessagesArrayLength;i++) {
                if ($.__globMessagesArray[i].key === key) {
                    return $.__globMessagesArray[i].value;
                }
            }

            i = globMessagesArrayLength = null;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setData
         * @description todo
         * @example todo
         */
        _setData: function (key, value) {
            if ($._getData(key) === undefined) {
                $.__globMessagesArray.push({key:key,value:value});
            }
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _cancelBubble
         * @description todo
         * @example todo
         */
        _cancelBubble: function (event) {
            if ($._isIE()) {
                event.cancelBubble = true;
            } else {
                event.stopPropagation();
            }
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _defer
         * @description todo
         * @example todo
         */
        _defer: function (aFunction) {
            $._execFn(aFunction,true);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _execFn
         * @description todo
         * @example todo
         */
        _execFn: function (aFunction, isSetTimeout) {

            if (aFunction === undefined || aFunction === null) {
                return;
            }

            if (isSetTimeout === undefined) {
                isSetTimeout = false;
            }

            if (typeof aFunction === 'function') {

                if (isSetTimeout) {
                    setTimeout(function () {
                        aFunction();
                    }, 0);
                } else {
                    aFunction();
                }

            } else {

                if (aFunction.indexOf("()") > 0) {
                    aFunction = aFunction.substr(0, aFunction.indexOf("()"));
                }

                var objLiteral = null,
                    fn = null,
                    sep = aFunction.indexOf('.');

                if (sep > 0) {
                    objLiteral = aFunction.substring(0, sep);
                    fn = aFunction.substring(sep + 1);
                } else {
                    fn = aFunction;
                }

                if (objLiteral === null) {
                    if (typeof window[fn] === 'function') {
                        if (isSetTimeout) {
                            setTimeout(function () {
                                window[fn]();
                            }, 0);
                        } else {
                            window[fn]();
                        }
                    }
                } else {
                    if (typeof window[objLiteral][fn] === 'function') {
                        if (isSetTimeout) {
                            setTimeout(function () {
                                window[objLiteral][fn]();
                            }, 0);
                        } else {
                            window[objLiteral][fn]();
                        }
                    }
                }
            }
        },







        /* ======================================================= */
        /* ===             COMMON PAGE FUNCTIONS                == */
        /* ======================================================= */

        __pageParams: null,


        _getParamValue: function (name) {
            if ($.__pageParams !== null) {
                var i, pageParamsLength = $.__pageParams.length;
                
                for (i = 0; i < pageParamsLength; i++) {
                    if ($.__pageParams[i].name === name) {
                        return $.__pageParams[i].value;
                    }
                }
                
                i = pageParamsLength = null;
            }
            return null;
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name __setParamValue
         * @description todo
         * @example todo
         */

        _setParamValue: function (name, value, isDate, formId, isFormInIframe) {
            $.__setParamValue({
                name: name,
                value: value,
                isDate: isDate,
                formId: formId,
                isFormInIframe: isFormInIframe
            });
        },


        __setParamValue: function (opt) {

            var settings = $.extend({
                name: null,
                value: null,
                isDate: false,
                formId: null,
                isFormInIframe: false
            }, opt);

            var isParamExist = false;

            if ($.__pageParams === null) {
                $.__pageParams = [];
            } else {
                var i, pageParamsLength = $.__pageParams.length;
                
                for (i = 0; i < pageParamsLength; i++) {
                    if ($.__pageParams[i].name === settings.name) {
                        $.__pageParams[i].value = settings.value;
                        $.__pageParams[i].isDate = settings.isDate;
                        isParamExist = true;
                        break;
                    }
                }

                i = pageParamsLength = null;
            }

            if (!isParamExist) {
                //stay compatible with normal input field definition and avoid double injection
                var $name;

                if (settings.formId !== null) {
                    if (settings.isFormInIframe) {
                        $name = $('iframe').contents().find('#' + settings.formId).find('#' + settings.name);
                    } else {
                        $name = $('#' + settings.formId).find('#' + settings.name);
                    }
                } else {
                    $name = $('#' + settings.name);
                }


                if ($name.length) {
                    $name.val(settings.value);
                } else {
                    $.__pageParams.push({name: settings.name, value: settings.value, isDate: settings.isDate});
                }
            }

            settings = isParamExist = null;
        },

        _injectPageParams: function (opt) {

            var settings = $.extend({
                formId: null,
                isFormInIframe: false,
                isFormInObject: false
            }, opt);


            if ($.__pageParams !== null) {

                var paramsContainer = '<div id="paramsContainer" class="hidden">',
                    paramName,
                    isDateExists = false,
                    p,
                    pageParamsLength = $.__pageParams.length;

                for (p = 0; p < pageParamsLength; p++) {
                    if ($.__pageParams[p].isDate && $._settings.dateFilterPrefix !== null) {
                        paramName = $._settings.dateFilterPrefix + $.__pageParams[p].name;
                        isDateExists = true;
                    } else {
                        paramName = $.__pageParams[p].name;
                    }
                    paramsContainer += '<input type="hidden" name="' + paramName + '" value="' + $.__pageParams[p].value + '" id="' + $.__pageParams[p].name + '">';
                }

                if (isDateExists && $._settings.dateFilterFormatName !== null) {
                    paramsContainer += '<input type="hidden" name="' + $._settings.dateFilterFormatName + '" value="dd/MM/yyyy hh:mm">';
                }

                paramsContainer += '</div>';

                if (settings.formId === null) {
                    $($._$form[$._$form.length - 1]).append(paramsContainer);
                } else {
                    if (settings.isFormInIframe) {
                        $('iframe').contents().find('#' + settings.formId).append(paramsContainer);
                    } else {
                        if (settings.isFormInObject) {
                            $($('object').get(0).contentDocument).find('#' + settings.formId).append(paramsContainer);
                        }  else {
                            $('#' + settings.formId).append(paramsContainer);
                        }
                    }
                }

                paramsContainer = paramName = isDateExists = p = pageParamsLength = null;

            }

            settings = null;

        },



        __isFunctionExists: function (fnString) {
            var objLiteral = null,
                fn = null,
                sep = fnString.indexOf('.');

            if (sep > 0) {
                objLiteral = fnString.substring(0, sep);
                fn = fnString.substring(sep + 1);
            } else {
                fn = fnString;
            }

            if (objLiteral === null) {
                if (window[fn] !== undefined) {
                    return (typeof window[fn] === 'function');    
                } else {
                    return false;
                }
            } else {
                if (window[objLiteral] !== undefined) {
                    if (window[objLiteral][fn] !== undefined) {
                        return (typeof window[objLiteral][fn] === 'function');
                    } else {
                        return false;
                    }    
                } else {
                    return false;
                }    
            }
        },



        __externalWindow: null,
        __externalWindowOpened: false,



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _navigateTo
         * @description todo
         * @example todo
         */
        _navigateTo: function (opt) {

            $._blockUI();

            var settings = $.extend({
                url: null,
                isOpenInExternalWindow: false,
                isCenterExternalWindow: true,
                isSameWindow: true,
                isKeepOnlyOneExternalWindow: true,
                externalWindowWidth: 800,
                externalWindowHeight: 600
            }, opt);

            var name;

            if (settings.isKeepOnlyOneExternalWindow) {
                name = "externalWindow";
            } else {
                name = "_blank";
            }

            if (settings.isOpenInExternalWindow) {
                $.__externalWindow = window.open(
                    $._getContextPath() + settings.url,
                    name,
                    "toolbar=no, location=no, directories=no, status=no, menubar=no, " +
                        "scrollbars=yes, resizable=yes, copyhistory=no, " +
                        "width=" + settings.externalWindowWidth + ",height=" + settings.externalWindowHeight
                );
                if (!$.__externalWindowOpened && settings.isCenterExternalWindow) {
                    $.__externalWindow.moveTo(screen.width / 2 - settings.externalWindowWidth / 2, screen.height / 2 - settings.externalWindowHeight / 2);
                }
                $.__externalWindowOpened = true;
            } else {
                if (settings.isSameWindow) {
                    window.location = $._getContextPath() + settings.url;
                } else {
                    window.open($._getContextPath() + settings.url, '_blank');
                }
            }

            $._unblockUI();
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _closeExternalWindow
         * @description todo
         * @example todo
         */
        _closeExternalWindow: function () {

            if ($.__externalWindow !== null) {
                $.__externalWindow.close();
            }

        },

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _form2json
         * @description todo
         * @example todo
         */
        _form2json: function ($form) {
            var o = {};
            var a = $form.serializeArray();
            $.each(a, function() {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        },


        /* ======================================================= */
        /* ===             EVENTS FUNCTIONS                     == */
        /* ======================================================= */

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEvent
         * @description todo
         * @example todo
         */
        //default form registered event creation
        _addEvent: function (eventType, selector, fn) {
            $._log('CORE.addEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            //listen the event
            $._$form.on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEventButtonClick
         * @description todo
         * @example todo
         */

        //default form registered event creation
        _addEventButtonClick: function (selector, fn) {
            $._log('CORE.addEventButtonClick : selector:[' + selector + ']');

            //listen the event
            $._$form.onButtonClick(
                'click',
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeEvent
         * @description todo
         * @example todo
         */

        //default form registered event removal
        _removeEvent: function (eventType, selector) {
            $._log('CORE.removeEvent : feventType:[' + eventType + '] selector:[' + selector + ']');

            $._$form.off(
                eventType,
                selector
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDocumentEvent
         * @description todo
         * @example todo
         */

        //default TOP registered event creation
        _addDocumentEvent: function (eventType, selector, fn) {
            $._log('CORE.addDocumentEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $(document).on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeDocumentEvent
         * @description todo
         * @example todo
         */
        //default TOP registered event removal
        _removeDocumentEvent: function (eventType, selector) {
            $._log('CORE.removeEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $(document).off(
                eventType,
                selector
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDialogEvent
         * @description todo
         * @example todo
         */

        //default TOP registered event creation
        _addDialogEvent: function (dialogId, eventType, selector, fn) {
            $._log('CORE.addDocumentEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $('#' + dialogId).on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEventOnEnter
         * @description todo
         * @example todo
         */

        //particular bound event on ENTER keydown on form element
        _addEventOnEnter: function (selector, fn) {
            $._log('CORE.addEventOnEnter : selector:[' + selector + ']');

            $._$form.on(
                'keypress',
                selector,
                function (event) {
                    var key;
                    if (window.event) {
                        key = window.event.keyCode; //IE
                    } else {
                        key = event.which;
                    }

                    if (key === 13) {
                        $._execFn(fn, true);
                        return false;
                    }

                    return true;
                }
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDocumentEventOnEnter
         * @description todo
         * @example todo
         */        
        //particular bound event on ENTER keydown on form element to be used in dialogs
        _addDocumentEventOnEnter: function (selector, fn) {
            $._log('CORE.addEventOnEnter : selector:[' + selector + ']');

            $(document).on(
                'keypress',
                selector,
                function (event) {
                    var key;
                    if (window.event) {
                        key = window.event.keyCode; //IE
                    } else {
                        key = event.which;
                    }

                    if (key === 13) {
                        $._execFn(fn, true);
                        return false;
                    }

                    return true;
                }
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addOnBeforeUnloadEvent
         * @description todo
         * @example todo
         */
        _addOnBeforeUnloadEvent: function () {
            window.onbeforeunload = function () {
                return $._getData('jscaf_common_onbeforeunload_message');
            };
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeOnBeforeUnloadEvent
         * @description todo
         * @example todo
         */
        _removeOnBeforeUnloadEvent: function () {
            window.onbeforeunload = function () {
            };
        },





        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getKey
         * @description todo
         * @example todo
         */
        _getKey: function(event) {
            var key;
            if (window.event) {
                key = window.event.keyCode; //IE
            } else {
                key = event.which;
            }
            return key;
        },







        /* ======================================================= */
        /* ===             MISC FUNCTIONS                       == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isEmpty
         * @description todo
         * @example todo
         */
        _isEmpty: function(map) {
           var key;

           for(key in map) {
              if (map.hasOwnProperty(key)) {
                 return false;
              }
           }
           return true;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _refreshJsonTemplate
         * @description todo
         * @example todo
         */
        _refreshJsonTemplate: function(templateId, jsonData, templateContainerId, fnPostCall) {

            var $template = $('#'+templateId), 
                $templateContainer = null;

            if (templateId === undefined || jsonData === undefined) {
                $._L('ERROR ===> templateId AND jsonData should be provided');
                return false;
            }

            if (!$template.length) {
                $._L('ERROR ===> template with id:[' + templateId +'] cannot be found ');
                return false;
            }


            //in case the container is undefined, the container is always the previous element to the template
            if (templateContainerId === undefined) {
                $templateContainer = $template.prev();
            } else {
                $templateContainer = $('#'+templateContainerId);
                if (!$templateContainer.length) {
                    $._L('ERROR ===> templateContainer with id:[' + templateContainerId +'] cannot be found ');
                    return false;
                } else {
                    $templateContainer = $('#'+templateContainerId);
                }
            }

            $templateContainer.html($template.render(jsonData));

            $template = $templateContainer = null;

            if (fnPostCall !== undefined) {
                fnPostCall();
            }

            return true;
        },






        /* ======================================================= */
        /* ===            REQUIRE.JS + COMPONENTS FUNCTIONS     == */
        /* ======================================================= */


        // REQUIRE.JS functions
        // ====================


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _require
         * @description todo
         * @example todo
         */
        //interface to requireJS.require
        _require: function (moduleName, fn) {
            var modulesArray = [],
                i, isAllModulesExist = true,
                modulesArrayLength;

            if ($.isArray(moduleName)) {
                modulesArray = moduleName;
            } else {
                modulesArray.push(moduleName);
            }

            modulesArrayLength = modulesArray.length;

            for (i = 0; i < modulesArrayLength; i++) {
                if (window[modulesArray[i]] === undefined) {
                    isAllModulesExist = false;

                    if (i === modulesArray.length - 1) {
                        require([modulesArray[i]], fn);
                    } else {
                        require([modulesArray[i]]);
                    }
                }
            }

            if (isAllModulesExist && fn !== undefined) {
                fn();
            }

            modulesArray = i = isAllModulesExist = modulesArrayLength = moduleName = fn = null;

        },


        /* ======================================================= */
        /* ===     HELPERS FUNCTIONS FOR jQUERY SUBSTITUTES     == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _hasClass
         * @description todo
         * @example todo
         */
        _hasClass: function (el, name) {
           return new RegExp('(\\s|^)'+name+'(\\s|$)').test(el.className);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addClass
         * @description todo
         * @example todo
         */
        _addClass: function(el, name) {
           if (!$._hasClass(el, name)) { el.className += (el.className ? ' ' : '') +name; }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeClass
         * @description todo
         * @example todo
         */
        _removeClass: function(el, name) {
           if ( el === null) {
              return;
           }
           if ($._hasClass(el, name)) {
              el.className=el.className.replace(new RegExp('(\\s|^)'+name+'(\\s|$)'),' ').replace(/^\s+|\s+$/g, '');
           }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _extend
         * @description todo
         * @example todo
         */
        _extend: function(a,b) {
            var key;
            for(key in b) {
                if(b.hasOwnProperty(key)) {
                    a[key] = b[key];
                }
            }
            return a;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getById
         * @description todo
         * @example todo
         */
        _getById: function(id) {
            return document.getElementById(id);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getAttr
         * @description todo
         * @example todo
         */
        _getAttr: function(el,attr) {
            var value = el.getAttribute(attr);
            if (value === null) {
                return undefined;
            }
            return value;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setAttr
         * @description todo
         * @example todo
         */
        _setAttr: function(el,attr,value) {
            el.setAttribute(attr,value);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getId
         * @description todo
         * @example todo
         */
        _getId: function(el) {
            return el.getAttribute('id');
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getCookie
         * @description todo
         * @example todo
         */
        _getCookie: function(c_name) {
            var i,x,y,ARRcookies=document.cookie.split(";");
            for (i=0;i<ARRcookies.length;i++) {
                x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
                y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
                x=x.replace(/^\s+|\s+$/g,"");
                if (x===c_name) {
                    return unescape(y);
                }
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setCookie
         * @description todo
         * @example todo
         */
        _setCookie: function(c_name,value,exdays) {
            var exdate=new Date();
            exdate.setDate(exdate.getDate() + exdays);
            var c_value=escape(value) + ((exdays===null) ? "" : "; expires="+exdate.toUTCString());
            document.cookie=c_name + "=" + c_value;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _remove
         * @description todo
         * @example todo
         */
        _remove: function($o) {
            var $parent = $o.parent();
            $o.remove();
            return $parent;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _putBack
         * @description todo
         * @example todo
         */
        _putBack: function($parent,$o) {
            $parent.append($o);
        }






    });
}(jQuery));





/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF COMPONENTS MODULE                == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({


        //DECLARATION
        //-----------
        __COMPONENTS: {

            define: function() {

                $._log('COMPONENTS.define');

                //in case a JScomponents object is provided, it takes the lead over default declaration,
                //and only the components defined in the JScomponents object will be initialised
                if ($._settings.JScomponents !== null) {

                    $.__cmp = $.extend($.__cmpDefs, $._settings.JScomponents);

                    //if the JScomponents is null, then all the components are initialised, except when jSCAF is run in LIGHT mode
                } else {

                    //in light version, every component used on a page should be explicitely declared
                    if ( !$._settings.isLightInitialisationActive ) {

                        var prop;

                        //in case the JScomponents is null, all components are loaded by default
                        for (prop in $.__cmpDefs) {
                            if ($.__cmpDefs.hasOwnProperty(prop)) {
                                $.__cmpDefs[prop] = true;
                            }
                        }

                        prop = null;
                    }

                    $.__cmp = $.__cmpDefs;
                }

            },


            bindEvents: function() {
                $._log('COMPONENTS.bindEvents');

                // global events
                // -------------

                //DISABLE ENTER KEY ON FORM AUTO-SUBMIT
                //-------------------------------------

                if ( $._settings.isDisableEnterKeyOnForm ) {

                    $._$document.on('keypress',
                        function (event) {
                            var key = $._getKey(event);

                            if (key === 13) { //enter
                                if ($('#' + event.target.id).hasClass('JS_enterkey_enabled')) {
                                    return true;
                                } else {
                                    return (event.target.nodeName === 'TEXTAREA');
                                }
                            }
                            return true;
                        }
                    );

                }


                //expandable events
                $._$document.on('click', '.JS_expandable', function () {
                    var $this = $(this),
                        $content = $this.siblings().filter('.content');


                    if ($content.length) {
                        var $expandButton = $this.find('.expand-button');
                        if ($expandButton.length) {
                            if ($content.hasClass('hidden')) {
                                $expandButton.removeClass('expanded').addClass('collapsed');
                            } else {
                                $expandButton.removeClass('collapsed').addClass('expanded');
                            }
                        }
                        $content.toggleClass('hidden');

                    } else {
                        $this.next().toggleClass('hidden');
                    }
                });

                $._$document
                    .on('click', '.JS_toggle-content, .toggle-trigger-content',
                    function () {
                        var $this = $(this);

                        $this.toggleClass("active");

                        if ($this.hasClass('JS_toggle-sub-row')) {
                            $this.closest('tr').next().toggleClass('hidden');
                        } else {
                            $('#content_' + $this.attr('id')).toggleClass('hidden');
                        }
                    }
                );


                if ($.__modules.VALIDATION) {
                    $.__VALIDATION.initValidationEvents();
                }



                //APPROVE TOGGLE BUTTON
                //---------------------
                if ($.__cmp.JSapproveToggle) {

                    $._$document
                        .on('click', 'span.JS_approve-toggle',
                        function () {
                            $._log('COMMON.JS_approve-toggle.CLICK');

                            var $this = $(this),
                                isDisabled = $this.hasClass('approve-toggle-disabled'),
                                $associatedContent = $('#' + $this.attr('id') + '_content'),
                                associatedPropertyId = $this.attr('associatedPropertyId'),
                                $associatedProperty = $('#' + associatedPropertyId),
                                associatedValueTrue = 'Y',
                                associatedValueFalse = 'N',
                                enableTableRowUpdate = $this.attr('enableTableRowUpdate');

                            if (isDisabled) {
                                $this.removeClass('approve-toggle-disabled').addClass('approve-toggle-enabled');
                            } else {
                                $this.removeClass('approve-toggle-enabled').addClass('approve-toggle-disabled');
                            }

                            if ($this.hasClass('JS_live-validation')) {
                                $._isFieldValid($this, null, isDisabled);
                            }

                            if ($this.attr('associatedValueTrue') === 'true') {
                                associatedValueTrue = 'true';
                                associatedValueFalse = 'false';
                            }

                            if ($associatedProperty.length) {
                                if (!isDisabled) {
                                    $associatedProperty.val(associatedValueFalse);
                                } else {
                                    $associatedProperty.val(associatedValueTrue);
                                }
                            }

                            if ($associatedContent.length) {
                                if (!isDisabled) {
                                    $associatedContent.addClass('hidden');
                                } else {
                                    $associatedContent.removeClass('hidden');
                                }
                            }

                            if (enableTableRowUpdate === undefined) {
                                enableTableRowUpdate = true;
                            }

                            if (enableTableRowUpdate) {

                                if (isDisabled) {
                                    $this.closest('tr').find('.note-error').removeClass('note-error').addClass('note-success').find('p').removeClass('text-color-red');
                                    $this.closest('tr').find("td:nth-child(2)").removeClass('bgcolor-red');
                                } else {
                                    $this.closest('tr').find('.note-success').removeClass('note-success').addClass('note-error').find('p').addClass('text-color-red');
                                    $this.closest('tr').find("td:nth-child(2)").addClass('bgcolor-red');
                                }

                            }

                        }
                    );
                }

                //BUTTON SET => hidden field val + associated content
                //---------------------------------------------------
                if ($.__cmp.JSbuttonSet) {

                    $._$document
                        .on('change', 'span.JS_buttonSet',
                        function () {
                            $._log('COMMON.JS_buttonSet.CHANGE');
                            var $this = $(this),
                                id = $this.attr('id'),
                                associatedPropertyId = $this.attr('associatedPropertyId'),
                                $associatedProperty = $('#' + associatedPropertyId),
                                checkedValue = $('[name="' + id + '"]:checked').attr('value'),
                                $associatedContent = $('#' + id + '_content');

                            if ($associatedProperty.length) {
                                $associatedProperty.val(checkedValue);
                            }

                            if ($associatedContent.length) {
                                var associatedContentDisplayValue = $this.attr('associatedContentDisplayValue');

                                if (associatedContentDisplayValue === '' || associatedContentDisplayValue === undefined) {
                                    if (checkedValue.toUpperCase() === 'TRUE' || checkedValue.toUpperCase() === 'Y') {
                                        $associatedContent.removeClass('hidden');
                                    } else {
                                        $associatedContent.addClass('hidden');
                                    }
                                } else {
                                    if (checkedValue === associatedContentDisplayValue) {
                                        $associatedContent.removeClass('hidden');
                                    } else {
                                        $associatedContent.addClass('hidden');
                                    }
                                }
                            } else {

                                var associatedContentDisplayBasedOnValue = $this.attr('associatedContentDisplayBasedOnValue');

                                if (associatedContentDisplayBasedOnValue !== '' && associatedContentDisplayBasedOnValue !== undefined) {
                                    if (associatedContentDisplayBasedOnValue.toUpperCase() === 'TRUE') {
                                        $('.' + id).addClass('hidden');
                                        $('#' + id + '_content_' + checkedValue).removeClass('hidden');
                                    }
                                }
                            }

                            //for blur for JS_live-validation trigger
                            if ($this.hasClass('JS_live-validation')) {
                                $._isFieldValid($this);
                            }
                        }
                    );
                }

                if ($.__cmp.JSfieldNumber) {

                    $._$document
                        .on('keypress', 'input.JS_field-number',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }

                            if (key > 31 && (key < 48 || key > 57)) {
                                return (key === 46 || key === 45 || key === 44); // decimal point & minus
                            }
                            return true;
                        }
                    );

                    $._$document
                        .on('blur', 'input.JS_field-number',
                        function () {
                            var $this = $(this),
                                value = $this.val();

                            if (value !== '') {
                                $this.val(parseFloat(value).toFixed(2));
                            }
                        }
                    );

                    $._$document
                        .on('keyup', 'input.JS_field-number',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }

                            //Convert "," (188 main keyboard, 110 numeric keyboard) by "."
                            if ( (key === 188) || (key === 110) ) {
                                var $this = $(this);
                                $this.val($this.val().replace(/[,]/g,"."));
                                return false;
                            }
                            return true;
                        }

                    );


                    $._$document
                        .on('keypress', 'input.JS_field-number0to9',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }
                            return !((key > 31 && key < 48) || key > 57);
                        }
                    );

                    //JS_field-number and JS_field-number0to9 auto select content
                    //-----------------------------------------------------------
                    $._$document
                        .on('focus', 'input.JS_field-number, input.JS_field-number0to9',
                        function () {
                            var t = $(this);

                            $._execFn(function () {
                                t.select();
                            }, true);
                        }
                    );
                }

                //JS_datepicker and JS_datetimepicker double click = today event
                //--------------------------------------------------------------
                if ($.__cmp.JSdatepicker) {

                    $._$document
                        .on('dblclick', 'input.JS_datepicker',
                        function () {
                            $(this).val(moment().format('DD/MM/YYYY'));
                        }
                    );


                    $._$document
                        .on('dblclick', 'input.JS_datetimepicker_d',
                        function () {
                            var d = moment(),
                                $this = $(this),
                                mainInputId = $this.attr('mainInputId'),
                                $d = $('#' + mainInputId + '_d'),
                                $h = $('#' + mainInputId + '_h'),
                                $m = $('#' + mainInputId + '_m');

                            $d.val(d.format('DD/MM/YYYY'));
                            $h.val(d.format('HH'));
                            $m.val(d.format('mm'));

                            $('#' + mainInputId).val(d.format('DD/MM/YYYY HH:mm'));

                            if ($this.hasClass('JS_dateonly')) {
                                $h.val('00');
                                $m.val('00');
                            }

                        }
                    );

                    $._$document
                        .on('blur', 'input.JS_datetimepicker_d',
                        function () {
                            $._log('COMMON.JS_datetimepicker_d.BLUR');
                            var $this = $(this),
                                mainInputId = $this.attr('mainInputId');

                            $('#'+mainInputId).val($this.val());
                            if ($('#'+mainInputId).val() === '__/__/____') {
                                $('#'+mainInputId).val('');
                            }
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + mainInputId));
                            }
                        }
                    );


                    $._$document
                        .on('blur', 'input.JS_datetimepicker_h',
                        function () {
                            $._log('COMMON.JS_datetimepicker_h.BLUR');
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + $(this).attr('mainInputId')));
                            }
                        }
                    );

                    $._$document
                        .on('keyup', 'input.JS_datetimepicker_h',
                        function (event) {
                            var $this = $(this);

                            if (!$this.hasClass('JS_no-minutes-autofocus')) {

                                var key;
                                if (window.event) {
                                    key = window.event.keyCode; //IE
                                } else {
                                    key = event.which;
                                }

                                //this prevent to go directly to the minutes when using tab on the date
                                if (key !== 9 && key !== 16) {
                                    //focus directly on minutes when the hours are filled
                                    if ($this.val().split("_").length - 1 === 0) {
                                        $('#' + $this.attr('mainInputId') + '_m').focus();
                                    }
                                }

                            }
                        }
                    );


                    $._$document
                        .on('blur', 'input.JS_datetimepicker_m',
                        function () {
                            $._log('COMMON.JS_datetimepicker_m.BLUR');
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + $(this).attr('mainInputId')));
                            }
                        }
                    );
                }

                //SLIDER KEEP FIXED ON SCROLL
                //---------------------------
                if ($.__cmp.JSslider) {

                    $(window).scroll(function () {
                        var $sliderTab = $('#slider_tab');
                        if ($sliderTab.length) {
                            $sliderTab.find('ul').css({'padding-top': $(window).height() / 4 + $(window).scrollTop() + 'px'});
                            $('#slider_content:not(.fixed)').css({'margin-top': $(window).scrollTop() + 'px'});
                        }
                    });
                }

                //JS_radio with content displayed on click
                //----------------------------------------
                if ($.__cmp.JSradio) {

                    $._$document.on('click', '.JS_radio', function () {
                        $('.JS_radio-content').addClass('hidden');
                        $('#' + this.id + '_content').removeClass('hidden');
                    });
                }

                //JS_checkbox with content displayed on click
                //----------------------------------------
                if ($.__cmp.JScheckbox) {

                    $._$document.on('click', '.JS_checkbox', function () {
                        if ($(this).is(':checked')) {
                            $('#' + this.id + '_content').removeClass('hidden');
                        } else {
                            $('#' + this.id + '_content').addClass('hidden');
                        }
                    });
                }

                //JS_autocomplete, prevent ajax blocker + initDisplay when remote ajax calls are done
                //-----------------------------------------------------------------------------------
                if ($.__cmp.JSautocomplete) {

                    $._$document.on('keydown', '.JS_autocomplete', function () {
                        $._isBlockerPage = false;
                        $._settings.isInitDisplayOnAjaxStop = false;
                    });

                    $._$document.on('keypress', '.JS_autocomplete', function () {
                        $._isBlockerPage = false;
                        $._settings.isInitDisplayOnAjaxStop = false;
                    });

                    $._$document.on('keyup', '.JS_autocomplete', function () {
                        var $this = $(this);
                        if ($this.val() === '' && $this.data().hasResultElement) {
                            var $result = $('#' + $this.attr('id') + '_result');
                            if ($result.get(0).nodeName === 'INPUT') {
                                $result.val('');
                            } else {
                                $result.html('');
                            }
                        }
                    });
                }

                //STAR RATING force blur of parent element for triggering live-validation
                //-----------------------------------------------------------------------
                if ($.__cmp.JSstars) {

                    $._$document.on('click',
                        '.ui-stars-star',
                        function () {
                            $(this).parent().blur();
                        });
                }

                //TOPNAV SELECT NAVIGATION FOR LOW-RESOLUTION DEVICES
                //---------------------------------------------------
                if ($.__cmp.JStopNav) {

                    $._$document.on('click',
                        '#topnav-menu',
                        function () {
                            $('#topnav-menu-content').toggle();
                        });
                    $._$document.on('click',
                        '#topnav-menu-content li',
                        function () {
                            var url = $(this).attr('linkUrl');
                            if (url !== 'undefined') {
                                window.location = url;
                            }
                        });
                }

                if ($.__cmp.JStopBar) {

                    $._$document.on('click',
                        '#topbar-menu',
                        function () {
                            $('#topbar-menu-content').slideToggle();
                        });
                    $._$document.on('click',
                        '#topbar-menu-content li',
                        function () {
                            var url = $(this).attr('linkUrl');
                            if (url !== 'undefined') {
                                window.location = url;
                            }
                        });
                }


                //HELP DIALOGS AND VIDEO CONTAINER
                //--------------------------------
                if ($.__cmp.JSopenDialogContent) {

                    $._$document
                        .on('click', '.JS_open-dialogContent',
                        function () {
                            var $this = $(this),
                                dialogContentId = this.id + '_dialogContent',
                                data = $.extend({
                                    contentId: dialogContentId,
                                    width:600,
                                    isEmptyOnClose:false,
                                    title: $this.attr('dialogTitle') //to ensure backward comp.
                                }, $this.data()),

                            //clone the dialogContent as it's always detroyed on save (since jSCAF 1.7)
                                $cloneDialogContent = $("#" + dialogContentId).clone();

                            $._openDialog({
                                dialogId: data.contentId,
                                isCloseOnEscape: true,
                                dialogWidth: data.width,
                                dialogTitle: data.title,
                                isEmptyOnClose:data.isEmptyOnClose,
                                isCenterAfterCreate: true,
                                fnDialogClose: function() {
                                    //put back the previously destroyed dialog content
                                    $this.after($cloneDialogContent);
                                }
                            });
                        }
                    );
                }



                if ($.__cmp.JSopenPdfDialog) {
                    $._$document
                        .on('click', '.JS_open-pdf-dialog',
                        function () {

                            var $this = $(this),
                                $data = $this.data(),
                                dialogId,
                                dialogTitle,
                                documentsArray = [],
                                i,
                                pdfFileNames,
                                pdfFileNamesPath,
                                hasHeaderAction = false,
                                headerActionUrl = null;

                            //creating the dialogId
                            dialogId = this.id + '_dialogContent';

                            //getting the fileNames array
                            if ($data.dialogPdfFilenames !== undefined) {
                                pdfFileNames = $data.dialogPdfFilenames;
                            } else {
                                pdfFileNames = $this.attr('pdfFileNames');
                            }

                            pdfFileNames = pdfFileNames.split(',');

                            //getting the path of the file
                            if ($data.dialogPdfFilenamesPath !== undefined) {
                                pdfFileNamesPath = $data.dialogPdfFilenamesPath;
                            } else {
                                pdfFileNamesPath = $._getContextPath() + $._settings.onlineHelpRootPath + "/pdf/";
                            }


                            //getting the title
                            if ($data.dialogTitle !== undefined) {
                                dialogTitle = $data.dialogTitle;
                            } else {
                                dialogTitle = $this.attr('dialogTitle');
                            }

                            //adding path to the files
                            for (i = 0; i < pdfFileNames.length; i++) {
                                documentsArray.push({
                                    documentFileName: pdfFileNames[i],
                                    documentUrl: pdfFileNamesPath + pdfFileNames[i]
                                });
                            }


                            if (pdfFileNames.length === 1 && $data.showDownloadLink) {
                                // Single document allow download
                                hasHeaderAction = true;
                                headerActionUrl = pdfFileNamesPath + pdfFileNames[0];
                            }

                            $._openIframeDialog({
                                dialogId: dialogId,
                                dialogTitle: dialogTitle,
                                documentsArray: documentsArray,
                                iFrameScrolling: 'no',
                                hasHeaderAction: hasHeaderAction,
                                headerActionUrl: headerActionUrl,
                                fnDialogClose: function() {
                                    $._closeDialog({dialogId: dialogId});
                                }
                            });

                        }
                    );

                }

                if ($.__cmp.JSopenVideoDialog) {

                    $._$document
                        .on('click', '.JS_open-video-dialog',
                        function () {
                            var $this = $(this);
                            var videoFileName = $._getContextPath() + $._settings.onlineHelpRootPath + "/videos/" + $this.attr('videoFileName') + '.m4v';

                            if ($._isIE7() || $._isIE8()) {

                                $._openIframeDialog({
                                    dialogId: 'testdialog',
                                    dialogTitle: 'HELP TUTORIAL : ' + $this.attr('videoFileName'),
                                    dialogWidth: 820,
                                    dialogHeight: 600,
                                    documentsArray: [
                                        {documentUrl: $._getContextPath() + $._settings.onlineHelpRootPath + '/videos/' + $this.attr('videoFileName') + '.html'}
                                    ],
                                    hasHeaderAction: true,
                                    headerActionUrl: videoFileName
                                });

                            } else {

                                var videoContent = '<video id="videoPlayer" class="video-js vjs-default-skin" controls autoplay preload="none" data-setup="{}">';
                                videoContent += '<source src="' + videoFileName + '" type="video/mp4" />';
                                videoContent += '</video>';

                                $._$body.append(videoContent);

                                $._openDialog({
                                    dialogId: 'videoPlayer',
                                    dialogTitle: 'VIDEO TUTORIAL : ' + $this.attr('videoFileName'),
                                    dialogWidth: 840,
                                    hasHeaderAction: true,
                                    headerActionUrl: videoFileName,
                                    fnAfterCreatePostCall: function () {
                                        $('#videoPlayer').css({'width': '800px'});
                                        $._centerDialog();
                                    },
                                    fnDialogClose: function () {
                                        $('#videoPlayer').remove();
                                    }
                                });

                            }
                        }
                    );
                }


                //TABLE SORTER on DEFAULT TABLE
                //-----------------------------
                if ($.__cmp.JStableSorter) {

                    $._$document
                        .on('sortEnd', 'table.default.tablesorter-table',
                        function () {
                            $.__COMPONENTS.initDefaultTable();
                        }
                    );

                }

                //TABLESORTER SIMULATION WITH SERVER-SIDE SORTING
                //------------------------------------------------
                if ($.__cmp.JStableSorterServer) {

                    $._$document
                        .on('click', 'table.tablesorter-table-server th',
                        function () {
                            var $this = $(this);
                            var tableId = $this.closest('table').attr('id');
                            var $table = $('#' + tableId);
                            //todo migrate those attributes into data- attributes
                            var $data = $this.data();
                            var $tableData = $table.data();
                            var sortedColumn = $this.attr('sortedColumn');
                            var sortDispatchValue = $table.attr('sortDispatchValue');
                            var sortAction = $table.attr('sortAction');
                            var associatedFragmentId = $table.attr('associatedFragmentId');
                            var fnPostCall = $table.attr('postCall');
                            var $sortOrder = $('#sortOrder');

                            if (sortedColumn === undefined) {
                                sortedColumn = $data.sortedColumn;
                            }
                            if (sortDispatchValue === undefined)  {
                                if ($tableData.sortDispatchValue !== undefined) {
                                    sortDispatchValue = $tableData.sortDispatchValue;
                                } else {
                                    sortDispatchValue = $tableData.sortCall;
                                }
                            }
                            if (sortAction === undefined) {
                                sortAction = $tableData.sortAction;
                            }
                            if (associatedFragmentId === undefined) {
                                associatedFragmentId = $tableData.associatedFragmentId;
                            }
                            if (fnPostCall === undefined) {
                                fnPostCall = $tableData.fnPostCall;
                            }


                            $._log('tablesorter-table-server th.CLICK => tableId=' + tableId + ' - sortedColumn=' + sortedColumn + ' - sortAction:' + sortAction + ' - associatedFragmentId=' + associatedFragmentId);

                            //displaying the header icon

                            if ($this.hasClass('sortable')) {
                                if ($this.hasClass('sortDown') || $this.hasClass('sortUp')) {
                                    if ($this.hasClass('sortDown')) {
                                        $sortOrder.val('ASC');
                                    }
                                    if ($this.hasClass('sortUp')) {
                                        $sortOrder.val('DESC');
                                    }
                                } else {
                                    $sortOrder.val('ASC');
                                }

                                //setting sortField
                                $('#sortedColumn').val(sortedColumn);


                                //refreshing the list according to the sorted options
                                if ($.__modules.AJAX) {
                                    $.__AJAX.ajax({
                                        id: associatedFragmentId,
                                        call: sortDispatchValue,
                                        action: sortAction,
                                        fnPostCall: function () {
                                            var $table = $('#'+tableId);

                                            $table.find('th.sortable').removeClass('sortDown').removeClass('sortUp');

                                            if ($('#sortOrder').val() === 'ASC') {
                                                $table.find('th.sortable[sortedColumn=' + sortedColumn + ']').addClass('sortUp');
                                                $table.find('th.sortable[data-sorted-column=' + sortedColumn + ']').addClass('sortUp');
                                            } else {
                                                $table.find('th.sortable[sortedColumn=' + sortedColumn + ']').addClass('sortDown');
                                                $table.find('th.sortable[data-sorted-column=' + sortedColumn + ']').addClass('sortDown');
                                            }

                                            if (fnPostCall !== undefined) {
                                                $._execFn(fnPostCall, false);
                                            }
                                        }
                                    });
                                }
                            }

                        });
                }


                // LIST COUNTER MENU
                // -----------------
                if ($.__cmp.JSlistCounterSmall) {

                    $._$document
                        .on('click', 'ul.list-counter-small li.parent',
                        function () {
                            var $this = $(this),
                                $ul = $this.parent(),
                                id = this.id;

                            if ($this.hasClass('empty') || $this.hasClass('selected')) {
                                return false;
                            }

                            $this.addClass('selected').siblings().removeClass('selected');

                            if ($ul.hasClass('JS_always-expanded')) {

                                $ul.find('li.child').removeClass('selected');

                            } else {

                                $('ul.list-counter-small span.child-wrapper').each(function () {
                                    var $this = $(this);

                                    if (this.id !== (id + '_children')) {

                                        if (!$this.hasClass('always-visible')) {
                                            $this.addClass('hidden');
                                        }

                                    }
                                });

                            }


                            var child = $('#' + id + '_children');

                            if (child.length) {
                                if (!child.find('li.child.selected').length) {
                                    if (child.hasClass('hidden')) {
                                        if (!$this.hasClass('unselectable')) {
                                            $this.addClass('selected');
                                        }
                                        child.removeClass('hidden');
                                    } else {
                                        if (!$ul.hasClass('JS_always-expanded')) {
                                            $this.removeClass('selected');
                                            child.addClass('hidden');
                                        }
                                    }
                                } else {
                                    child.find('li.child.selected').removeClass('selected');
                                    if (!$this.hasClass('unselectable')) {
                                        $this.addClass('selected');
                                    }
                                    child.removeClass('hidden');
                                }

                                if ($this.hasClass('unselectable')) {
                                    var $firstChild = child.find('li.child').eq(0);
                                    if ($this.hasClass('autoselect-first-child')) {
                                        $firstChild.addClass('selected').click();
                                    }
                                }
                            } else {
                                $this.addClass('selected');
                            }

                            if ($this.hasClass('selected') && $this.closest('ul').hasClass('with-arrow')) {
                                if (!$this.find('.arrow').length) {
                                    $this.append('<span class="arrow"></span>');
                                }
                            }

                            return true;
                        });

                    $._$document
                        .on('click', 'ul.list-counter-small li.child',
                        function () {
                            var $this = $(this),
                                $childWrapper = $this.closest('.child-wrapper'),
                                $ul = $this.closest('ul');

                            if ($this.hasClass('empty')) {
                                return;
                            }

                            if ($ul.hasClass('JS_always-expanded')) {
                                $ul.find('li.child').removeClass('selected');
                                $ul.find('li.parent').siblings().removeClass('selected');
                            }

                            $childWrapper.prev().removeClass('selected');
                            $childWrapper.find('li').removeClass('selected');
                            $this.addClass('selected');

                        });

                }


                //RADIO-LIST
                //----------
                if ($.__cmp.JSradioList) {

                    $._$document
                        .on('click', 'li.radio-list',
                        function () {
                            var $this = $(this);
                            $this.siblings().removeClass('selected');
                            $this.addClass('selected');
                        });

                }


                // COUNTER-BOX-BIG SELECTABLE
                // --------------------------
                if ($.__cmp.JScounterBox) {

                    $._$document
                        .on('click', '.counter-box-big-wrapper.selectable',
                        function () {
                            $('.counter-box-big-wrapper.selectable').removeClass('selected');
                            $(this).addClass('selected');
                        });
                }

                // PAGINATION EVENT
                // ----------------
                if ($.__cmp.JSpagination) {

                    $._$document
                        .on('click', '.action_pagination_goto_page',
                        function () {
                            var $this = $(this),
                                fragmentId = $this.closest('.pagination-wrapper').attr('id');

                            if ($.__modules.AJAX) {
                                $.__AJAX.ajax({
                                    id: fragmentId,
                                    call: 'pgGotoPage',
                                    action: fragmentId,
                                    fnPreCall: function() {
                                        $._setParamValue('pgSelectedPageIndex', $this.attr('pageIndex'));
                                    },
                                    fnPostCall: function () {
                                        $('html, body').animate({scrollTop: 0});
                                    }
                                });
                            }
                        });
                }

                if ($.__cmp.JStooltip) {

                    $._$document
                        .on('click', '.JS_tooltip',
                        function () {
                            $(this).poshytip('show').addClass('tooltip-active');
                        });


                    $._$document
                        .on('click', '.JS_tooltip-content-close',
                        function () {
                            var caller = $('#' + $(this).attr('data-linked-tooltip-id'));

                            //maintain backward comp.
                            if (!caller.length) {
                                caller = $('#' + $(this).attr('linkedtooltipid'));
                            }
                            if (caller.length) {
                                caller.poshytip('hide');
                                caller.removeClass('tooltip-active');
                            } else {
                                $('.tip-default').remove();
                                $('.tip-black').remove();
                                $('.tip-white').remove();
                            }
                        });
                }


                //TABLE FILTER EVENT
                //------------------
                if ($.__cmp.JStableFilter) {

                    $.expr[':'].containsIgnoreCase = function (n, i, m) {
                        return jQuery(n).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
                    };

                    $._$document.on('click', 'input.table-filter', function(e) {
                       e.stopPropagation();
                    });

                    $._$document.on('keyup',
                        'input.table-filter',
                        function () {
                            var tableIdToFilter =$(this).attr('tableIdToFilter'),
                                tableBody,
                                data = this.value,
                                jo, i, $jo;

                            if (tableIdToFilter==='*') {
                                tableBody = $('table').find('tbody');

                                if (data === '') {
                                    $('table').find('thead').show();
                                    $('.section-title-bullet').show();
                                    $('br').show();
                                } else {
                                    $('table').find('thead').hide();
                                    $('.section-title-bullet').hide();
                                    $('br').hide();
                                }

                            } else {
                                tableBody = $($('#' + tableIdToFilter).find('tbody')[0]);
                            }

                            tableBody.find('tr').hide().addClass('bg bgcolor-red');
                            jo = tableBody.find('tr');
                            for (i=0; i<jo.length; ++i) {
                                $jo = $(jo[i]);
                                if ($jo.find('*:containsIgnoreCase("'+data+'")').length) {
                                    $jo.show();
                                    if ($jo.hasClass('sub-row')) {
                                        $(jo[i-1]).show();
                                    } else {
                                        $jo = $(jo[i + 1]);
                                        if ($jo.hasClass('sub-row')) {
                                            $jo.show();
                                            ++i;
                                        }
                                    }
                                }
                            }
                        });


                    $._$document.on('click','.action_reset_table_filter',
                        function(e) {
                            var associatedFilterId= $(this).data().filterId;
                            $('#' + associatedFilterId).val('').keyup();
                            e.stopPropagation();
                        });


                }

                //INPUT FILE UPLOAD EVENT
                //-----------------------

                if ($.__cmp.JSfileInput) {

                    $._$document.on('change',
                        'input.file-input',
                        function () {
                            var value = $(this).val();
                            $(this).next().text(value.substr(value.lastIndexOf('\\') + 1, value.length));
                            $(this).next().addClass('value');
                        });
                }

                //HELP CONTENT TRIGGER
                //--------------------
                if ($.__cmp.JShelp) {

                    $._$document.on('focus',
                        'input.JS_help,select.JS_help',
                        function () {
                            var $this = $(this);

                            if ($._isInlineHelpActive) {
                                $this.poshytip({
                                    content: $('#' + $this.attr('id') + '_help').html(),
                                    showOn: 'focus',
                                    className: 'tip-black',
                                    offsetX: 10,
                                    allowTipHover: false,
                                    slide: false,
                                    alignTo: 'target',
                                    alignX: 'right',
                                    alignY: 'center'
                                });
                            } else {
                                $this.poshytip('destroy');
                            }
                        });
                }


                //TOGGLE BUTTON
                //-------------
                if ($.__cmp.JStoggleButton) {

                    $._$document.on('click',
                        'a.toggle-button',
                        function (e) {
                            var $this = $(this);
                            var $icon = $this.find('.icon');

                            $this.toggleClass('active');

                            if ($this.hasClass('active')) {
                                $icon.addClass('on').removeClass('off');
                            } else {
                                $icon.addClass('off').removeClass('on');
                            }

                            e.stopPropagation();
                        });
                }


                //RADIO BUTTONS : click on label = check the radio button
                //-------------------------------------------------------
                if ($.__cmp.JSradio) {
                    $._$document.on('click', '.radio-group > label', function () {
                        var $this = $(this),
                            data = $this.data();

                        if ( $this.prev().attr('disabled') !== 'disabled') {
                            $this.parent().find('input').prop('checked', false).removeClass('checked');
                            $this.prev().prop('checked', true).attr('checked', 'checked').addClass('checked');
                        }
                        if (data.associatedInputId !== undefined) {
                            $('#'+data.associatedInputId).val($this.find('input').val());
                        }
                    });
                }

                // ACCORDION BOX
                // -------------
                if ($.__cmp.JSaccordionBox) {
                    $._$document.on('click','.JS_accordion_box_auto_triggered',function() {
                        $._accordionPanelSwitch($(this).closest('.accordion-box').attr('id'));
                    });
                }


                // LISTS TRANSFER
                // --------------
                if ($.__cmp.JSlistsTransfer) {

                    $._$document.on('click','ul.JS_lists-transfer li', function(){
                        $(this).toggleClass('active');
                    });


                    $._$document.on('click','.JS_lists-transfer-actions button', function() {

                        var $this = $(this),
                            transferType = $this.data().transferType,
                            $lists = $('.JS_lists-transfer-actions').parent().find('ul.JS_lists-transfer'),
                            $listLeft = $($lists[0]),
                            $listRight = $($lists[1]);

                        if (transferType === 'LR_ALL') {
                            $listRight.append($listLeft.find('li'));
                        }
                        if (transferType === 'RL_ALL') {
                            $listLeft.append($listRight.find('li'));
                        }
                        if (transferType === 'LR_SELECTED') {
                            $listRight.append($listLeft.find('li.active').removeClass('active'));
                        }
                        if (transferType === 'RL_SELECTED') {
                            $listLeft.append($listRight.find('li.active').removeClass('active'));
                        }

                        return false;
                    });

                }


                // VIEW DOCUMENT
                // -------------
                if ($.__cmp.JSviewDocument) {

                    $._$document.on('click','.JS_view-document', function() {
                        var data = $(this).data();

                        $._openDocumentDialog({
                            documentId: data.id,
                            documentDescription: data.description,
                            documentFilename: data.filename,
                            documentUrl: data.url,
                            documentContentType: data.contentType
                        });

                    });

                }




                // initialising flat theme events
                if ($._settings.isFlatThemeActive) {

                    // Display/Hide Dashboard
                    // Handle dashboard height according  to window size

                    $._$document.on( 'click', '#toggle-tasks', function( event ) {
                        event.stopPropagation();
                        $( this ).toggleClass( 'open' );
                        $( '#header-tasks' ).toggle();
                        $( '.tasks-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
                    });

                    $._$document.on( 'click', '#toggle-dashboard', function( event ) {
                        event.stopPropagation();
                        $( this ).toggleClass( 'open' );
                        $( '#header-page-dashboard' ).toggle(function(){
                            $(this).find('.JS_focus').focus();
                        });
                        $.__COMPONENTS.iaResizeDashboard();
                    });

                    //Call the resize dashboard if the window is resized
                    $( window ).on( 'resize', $.__COMPONENTS.iaResizeDashboard );

                    // Hide Dashboard when the user clicks outside of it
                    $._$document.on( 'click touchstart', 'html', function( event ) {
                        var $headerPageDashboard = $( '#header-page-dashboard' );
                        if ( $headerPageDashboard.is( ':visible' ) ) {
                            $headerPageDashboard.hide();
                            $( '#toggle-dashboard' ).removeClass( 'open' );
                        }
                    });

                    $._$document.on( 'click touchstart', '#header-page-dashboard', function( event ) {
                        event.stopPropagation();
                    });

                    // Dashboard Filtering: Display results list on first character entered into the search box (hides tabbed interface)
                    var dashboardVisible = false;
                    $._$document.on( 'keyup', '#dashboard-filter-input', function() {
                        var filterInputContent = $( '#dashboard-filter-input' ).val();

                        if ( filterInputContent !== '' && dashboardVisible !== true ) {
                            dashboardVisible = true;
                            $( '#dashboard-tabs' ).hide();
                            $( '#dashboard-filter-results' ).show();
                        } else if ( filterInputContent === '' && dashboardVisible === true ) {
                            dashboardVisible = false;
                            $( '#dashboard-tabs' ).show();
                            $( '#dashboard-filter-results' ).hide();
                        }
                    });

                    // Dashboard Filtering: Display tabbed interface when the user cancels the search (hides results list)
                    $._$document.on( 'click', '#dashboard-filter-cancel', function() {
                        dashboardVisible = false;
                        $( '#dashboard-filter-input' ).val('');
                        $( '#dashboard-tabs' ).show();
                        $( '#dashboard-filter-results' ).hide();
                    });


                    // Dashboard Filtering: Filter content
                    $._$document.on( 'keyup', '#dashboard-filter-input', function() {
                        // Filter the items
                        var filter = $(this).val();

                        $( '#dashboard-filter-results .item' ).each( function() {
                            var $this = $(this);

                            if ( $this.text().search( new RegExp( filter, 'i' ) ) < 0 ) {
                                $this.fadeOut( { duration: 140 });
                            } else {
                                $this.show();
                            }
                        } );
                    });


                }



            },



            iaResizeDashboard: function() {
                $( '.dashboard-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
            },


            initDisplay: function() {

                $._log('COMPONENTS.initDisplay');

                if ($._settings.isPageZoomWarningEnabled && $.__modules.BROWSER) {
                    $.__BROWSER.checkPageZoomWarning();
                }

                //checking browser version (for warning - not blocking)
                if ($._settings.isBrowserWarningEnabled && $.__modules.BROWSER) {
                    $.__BROWSER.checkBrowserWarning();
                }

                //validation box field alignment
                $('.field > .validation.box').each(function () {
                    var $this = $(this);
                    $this.css({'margin-left': $this.parent().find('.field-label').width() + 8});
                    $this = null;
                });

                //dynamically center a block against its parent container
                $('.JS_center').each(function () {
                    var $this = $(this);
                    var marginLeft = (($this.closest('.content').width() - $this.width()) / 2) - 10;
                    if (marginLeft > 0) {
                        $this.css({'margin-left': marginLeft + 'px'});
                    }
                    $this = null;
                });

                //in case of a content slider is present, force it to be fixed positioned although floating
                if ($.__cmp.JSslider) {
                    var $sliderTab = $('#slider_tab');
                    if ($sliderTab.length) {
                        $('#slider_tab').find('ul').css({'padding-top': $(window).height() / 4 + $(window).scrollTop() + 'px'});
                        $('#slider_content:not(.fixed)').css({'margin-top': $(window).scrollTop() + 'px'});
                    }
                    $sliderTab = null;
                }

                //MENU WITH HOVERINTENT
                //---------------------
                if ($.__cmp.JStopNav) {

                    var $topnav = $('.top-nav');
                    if ($topnav.length) {
                        $topnav.find('.sub').css({'opacity': '0'});
                        $topnav.find('li').hoverIntent({
                            sensitivity: 1, // number = sensitivity threshold (must be 1 or higher)
                            interval: 0, // number = milliseconds for onMouseOver polling interval
                            over: megaHoverOver, // function = onMouseOver callback (REQUIRED)
                            timeout: 0, // number = milliseconds delay before onMouseOut
                            out: megaHoverOut // function = onMouseOut callback (REQUIRED)
                        });
                    }
                }



                //FIELD FOCUS on JS_focus class
                //-----------------------------
                $('input.JS_focus').focus();


                //DEFAULT TABLE
                //-------------
                if ($('table.default').length) {
                    $.__COMPONENTS.initDefaultTable();
                }


                //topbar contraction
                var $headerReadOnly = $('#header.read-only');
                if ($headerReadOnly.length) {
                    setTimeout(function () {
                        if ($headerReadOnly !== null) {

                            var $topBarLis = $headerReadOnly.find('.top-bar li:not(.fixed):not(.separator):not(.read-only-hidden)');
                            var $readOnlyBottomSwitch = $headerReadOnly.find('.read-only-bottom-switch');

                            if ($topBarLis.length && $readOnlyBottomSwitch.length) {
                                $topBarLis.css({'overflow': 'hidden'}).animate({width: 15}, 150);
                                $readOnlyBottomSwitch.css({'position': 'absolute'}).animate({'top': '30px', 'right': '0px'}, 150);
                            }

                            $topBarLis = $readOnlyBottomSwitch = null;
                        }
                    }, 750);
                }
                $headerReadOnly = null;

                var $topMessage = $('#top_message');
                if ($topMessage.length) {
                    //constraint the top-message to the body width, as it is position fixed
                    $('#top_message').css({'width': $._$body.width() + 'px'});
                }
                $topMessage = null;


                //EXPANDABLE BOX
                //--------------
                //show the expand icon on the box header : usage : <div class="box expandable">
                var $jsExpandable = $('.JS_expandable');

                $jsExpandable.each(function(){
                    var $this = $(this);

                    if ($this.hasClass('JS_show-icon')) {
                        if ($this.hasClass('header') && $this.parent().hasClass('box')) {
                            var $content = $this.siblings().filter('.content'),
                                $expandButton = $this.find('.expand-button'),
                                $headerRight = $this.find('.header-right');

                            if ($content.hasClass('hidden')) {
                                if ($expandButton.length) {
                                    $expandButton.removeClass('collapsed').addClass('expanded');
                                } else {
                                    if ($headerRight.length) {
                                        $headerRight.before('<span class="fr expand-button expanded"></span>');
                                    } else {
                                        $this.append('<span class="fr expand-button expanded"></span>');
                                    }
                                }
                            } else {
                                if ($expandButton.length) {
                                    $expandButton.removeClass('expanded').addClass('collapsed');
                                } else {
                                    if ($headerRight.length) {
                                        $headerRight.before('<span class="fr expand-button collapsed"></span>');
                                    } else {
                                        $this.append('<span class="fr expand-button collapsed"></span>');
                                    }
                                }
                            }
                            $content = null;
                            $expandButton = null;
                            $headerRight = null;
                        }
                    }
                });

                $jsExpandable = null;


                //PLACEHOLDER SUPPORT
                //-------------------
                if ($.placeholder !== undefined) {
                    $('input[placeholder], textarea[placeholder]').placeholder();
                }



                //JQUERY-UI DATEPICKER INIT
                //-------------------------
                if ($.__cmp.JSdatepicker) {

                    var datepickerOptions =
                    {
                        dateFormat: 'dd/mm/yy',
                        constrainInput: true,
                        changeMonth: true,
                        changeYear: true,
                        showOn: "button",
                        showAnim: '',
                        buttonImage: $._settings.jscafRootUrl + "/styles/images/common/datepicker-calendar.png",
                        buttonImageOnly: true,
                        buttonText: "",
                        showButtonPanel: true,
                        firstDay: 1,
                        isRTL: false,
                        minDate: null,
                        maxDate: null,
                        beforeShow: function (input) {
                            $(input).unbind('blur');
                        },
                        onSelect: function () {
                            $._log('COMMON.datepicker.ONSELECT');
                            var $this = $(this);
                            if ($this.hasClass('JS_datetimepicker_d')) {
                                var mainInputId = $('#' + $this.attr('mainInputId'));
                                if ($.__modules.VALIDATION) {
                                    $.__VALIDATION.validateDatetimepicker(mainInputId);
                                }
                            } else {
                                if ($.__modules.VALIDATION) {
                                    $._isFieldValid($this);
                                }
                                $this.change();
                            }
                        }
                    };

                    // datePicker localisation
                    var datePickerLocal;
                    if ($._settings.language === 'fr') {
                        datePickerLocal = {
                            monthNames: ['Janvier', 'F&eacute;vrier', 'Mars', 'Avril', 'Mai', 'Juin',
                                'Juillet', 'Ao&ucirc;t', 'Septembre', 'Octobre', 'Novembre', 'D&egrave;cembre'],
                            monthNamesShort: ['Jan', 'F&eacute;v', 'Mar', 'Avr', 'Mai', 'Jun',
                                'Jul', 'Ao&ucirc;', 'Sep', 'Oct', 'Nov', 'D&egrave;c'],
                            dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
                            dayNamesShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
                            dayNamesMin: ['Di', 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa'],
                            prevText: 'Mois pr&eacute;c&eacute;dent',
                            nextText: 'Mois suivant',
                            currentText: 'Aujourd\'hui',
                            closeText: 'Fermer'
                        };
                    } else {
                        datePickerLocal = {
                            monthNames: ['January', 'February', 'March', 'April', 'May', 'June',
                                'July', 'August', 'September', 'October', 'November', 'December'],
                            monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                            dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
                            dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                            dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
                            prevText: "Previous Month",
                            nextText: "Next Month",
                            currentText: 'today',
                            closeText: 'Done'
                        };
                    }
                    $.extend(datepickerOptions, datePickerLocal);


                    //Checking if a long date of years
                    var pickerRange = {yearRange: "c-1:c+2"};
                    var pickerRangeLong = {yearRange: "2007:c+5"};

                    $('input.JS_datepicker').each(function () {
                        var $this = $(this),
                            data = $this.data();

                        // YEAR RANGE

                        if (data.yearRange !== undefined) {
                            $.extend(datepickerOptions, {yearRange: data.yearRange});

                            //maintained for backward compatibility,
                        } else {
                            if ($this.hasClass('JS_dateLongRange')) {
                                $.extend(datepickerOptions, pickerRangeLong);
                            } else {
                                $.extend(datepickerOptions, pickerRange);
                            }
                        }

                        if (data.minDate !== undefined) {
                            $.extend(datepickerOptions, {minDate: data.minDate});
                        }

                        if (data.maxDate !== undefined) {
                            $.extend(datepickerOptions, {maxDate: data.maxDate});
                        }


                        // SHOW METHOD

                        if (data.showOn !== undefined) {
                            $.extend(datepickerOptions, {showOn: data.showOn});
                        }


                        // Enable input mask

                        $this.addClass('JS_maskedInput').attr('inputMask', '99/99/9999');

                        $this.datepicker(datepickerOptions);
                    });


                    //create the date picker
                    $("input.JS_datetimepicker").each(function () {
                        var $this = $(this);
                        var data = $this.data();
                        var id = $this.attr('id');
                        var id_d = id + '_d';
                        var id_h = id + '_h';
                        var id_m = id + '_m';

                        $this.addClass('hidden');

                        if ($this.parent().find('input').length === 1) {
                            $this.parent().append('<input id="' + id_d + '" mainInputId="' + id + '" type="text" class="JS_datepicker JS_datetimepicker_d JS_maskedInput field-value fl" inputMask="99/99/9999"/>');

                            if (data.yearRange !== undefined) {
                                $.extend(datepickerOptions, {yearRange: data.yearRange});

                                //maintained for backward compatibility
                            } else {
                                if ($this.hasClass('JS_dateLongRange')) {
                                    $.extend(datepickerOptions, pickerRangeLong);
                                } else {
                                    $.extend(datepickerOptions, pickerRange);
                                }
                            }

                            if (data.minDate !== undefined) {
                                $.extend(datepickerOptions, {minDate: data.minDate});
                            }

                            if (data.maxDate !== undefined) {
                                $.extend(datepickerOptions, {maxDate: data.maxDate});
                            }


                            // SHOW METHOD

                            if (data.showOn !== undefined) {
                                $.extend(datepickerOptions, {showOn: data.showOn});
                            }

                            $('#' + id_d).datepicker(datepickerOptions);
                            $this.parent().append('<input id="' + id_h + '" mainInputId="' + id + '" type="text" style="width:18px;margin-left:2px;" class="JS_datetimepicker_h JS_maskedInput field-value fl" inputMask="99"/>');
                            $this.parent().append('<input id="' + id_m + '" mainInputId="' + id + '" type="text" style="width:18px;margin-left:2px;" class="JS_datetimepicker_m JS_maskedInput field-value fl" inputMask="99"/>');
                            $this.closest('.field').addClass('datetimepicker');
                        }

                        var $d = $('#' + id_d);
                        var $h = $('#' + id_h);
                        var $m = $('#' + id_m);

                        if ($.trim($this.val()) !== '' && $.trim($this.val()) !== 'NaN/NaN/NaN NaN:NaN') {
                            var d = moment($this.val(), 'DD/MM/YYYY HH:mm');
                            $d.val(d.format('DD/MM/YYYY'));
                            $h.val(d.format('HH'));
                            $m.val(d.format('mm'));
                        }

                        if ($this.hasClass('JS_dateonly')) {
                            $h.val('00').addClass('hidden');
                            $m.val('00').addClass('hidden');
                        }

                        //copying additional classes from the input field to the generated fields
                        var i, classes = $this.attr('class').split(' ');
                        for (i = 1; i < classes.length; i++) {
                            if (classes[i] !== 'JS_datetimepicker' &&
                                classes[i] !== 'JS_field' &&
                                classes[i] !== 'JS_live-validation' &&
                                classes[i] !== 'field-value' &&
                                classes[i] !== 'hidden') {
                                $d.addClass(classes[i]);
                                $h.addClass(classes[i]);
                                $m.addClass(classes[i]);
                            }
                        }


                    });

                }

                //TRANSFORM RADIO BUTTONS INTO JQUERY-UI BUTTON SETS
                //--------------------------------------------------
                if ($.__cmp.JSbuttonSet) {

                    $('span.JS_buttonSet').buttonset();

                }

                //INIT poshytip tooltip
                //---------------------
                if ($.__cmp.JStooltip) {

                    //Poshytip

                    $('.JS_tooltip').each(function () {

                        var $this = $(this),
                            $content,
                            settings = $.extend({
                                type: 'default',
                                triggerType: 'hover',
                                content: 'title',
                                contentId: null,
                                align: 'bottom',
                                theme: 'tip-black',
                                fade: false,
                                slide: false,
                                width: null,
                                showClose: true,
                                closePosition: 'top',
                                fixedTopPosition: null,
                                hintTitle: null
                            }, $this.data()),

                        //default tip parameters for bottom alignment
                            tipParams = {
                                alignY: 'bottom',
                                alignX: 'center',
                                offsetX: 0,
                                showOn: 'hover'
                            };


                        //LEFT align
                        if (settings.align === 'left') {
                            tipParams.alignY = 'center';
                            tipParams.alignX = 'left';
                            tipParams.offsetX = 5;
                        }

                        //RIGHT align
                        if (settings.align === 'right') {
                            tipParams.alignY = 'center';
                            tipParams.alignX = 'right';
                            tipParams.offsetX = 5;
                        }

                        //TOP align
                        if (settings.align === 'top') {
                            tipParams.alignY = 'top';
                        }

                        //Trigger type : normal OR onClick
                        if (settings.triggerType === 'onClick') {
                            tipParams.showOn = 'none';
                        }

                        //Trigger type : focus for input
                        if (settings.triggerType === 'focus') {
                            tipParams.showOn = 'focus';
                            settings.slide = true;
                            settings.fade = true;
                        }

                        //checking and getting the content element
                        if (settings.content === 'title' && settings.type !== 'hint' && settings.triggerType !== 'onClick') {
                            settings.content = $this.attr('title');
                        } else if (settings.content === 'html' || settings.type === 'hint' || settings.triggerType === 'onClick') {
                            $content = $('#' + this.id + '_content');
                            if (!$content.length) {
                                $content = $('#' + settings.contentId);
                            }

                            //Prepend/append the close button dynamically if not present
                            if (settings.triggerType === 'onClick' && settings.showClose) {
                                if (!$content.find('.JS_tooltip-content-close').length) {
                                    if (settings.closePosition === 'top') {
                                        if (settings.type === 'hint') {
                                            $content.prepend('<a class="JS_tooltip-content-close text-color-light-grey fr small" data-linked-tooltip-id="' + $this.attr('id') + '" style="position:absolute; top:10px; right: 10px;">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a>');
                                        } else {
                                            $content.prepend('<a class="JS_tooltip-content-close text-color-white fr small" data-linked-tooltip-id="' + $this.attr('id') + '">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a><br><br>');
                                        }
                                        //$content.prepend('<i class="JS_tooltip-content-close icon icon-ft-cancel-circle size16 red fr" data-linked-tooltip-id="' + $this.attr('id') + '"><br>');
                                    } else if (settings.closePosition === 'bottom') {
                                        $content.append('<br><a class="JS_tooltip-content-close text-color-white fr small" data-linked-tooltip-id="' + $this.attr('id') + '">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a>');
                                    }
                                }
                            }

                            settings.content = $content.html();
                        }

                        //subclassing types
                        if (settings.type === 'hint') {
                            settings.theme = 'tip-hint';
                            settings.fade = true;
                            settings.slide = true;

                            if (settings.hintTitle === null) {
                                settings.hintTitle = 'INFO';
                            }

                            settings.content = '<div class="tooltip-hint">' +
                            '<h6>' + settings.hintTitle + '</h6>' +
                            settings.content +
                            '</div>';
                        }

                        if (settings.type === 'error' || settings.type === 'input-error') {
                            settings.theme = 'tip-error';
                            tipParams.alignX = 'right';
                            tipParams.alignY = 'center';
                            tipParams.offsetX = 5;
                            settings.fade = true;

                            if (settings.type === 'input-error') {
                                settings.triggerType = 'focus';
                            }
                        }


                        $this.poshytip({
                            content: settings.content,
                            showOn: tipParams.showOn,
                            className: settings.theme,
                            showTimeout: 100,
                            offsetY: 5,
                            allowTipHover: false,
                            fade: settings.fade,
                            slide: settings.slide,
                            alignTo: 'target',
                            alignX: tipParams.alignX,
                            alignY: tipParams.alignY,
                            offsetX: tipParams.offsetX
                        });

                        $('.tip-black, .tip-white, .tip-hint').css({'max-width': settings.width});
                        if (settings.fixedTopPosition !== null) {
                            $('.tip-white').addClass('fixed-top-position');
                        }
                    });

                    //remove some .tip craps added to the dom
                    $('.tip-default').remove();
                    $('.tip-error').remove();
                    $('.tip-hint').remove();
                    $('.tip-black').remove();
                    $('.tip-white').remove();

                }


                //MASKED INPUT
                //------------
                if ($.__cmp.JSmaskedInput) {

                    $('input.JS_maskedInput').each(function () {
                        var $this = $(this);
                        $this.mask($this.attr('inputMask'));
                    });
                }

                //FIELD JS_field-number0to9 formating
                //-----------------------------------
                if ($.__cmp.JSfieldNumber) {

                    $('input.JS_field-number0to9').each(function () {
                        var $this = $(this);
                        if ($this.val() !== '') {
                            $this.val(parseFloat($this.val()).toFixed(0));
                        }
                    });

                    //FIELD JS_field-number formating
                    //-----------------------------------
                    $('input.JS_field-number').each(function () {
                        var $this = $(this);
                        if ($this.val() !== '') {
                            $this.val(parseFloat($this.val()).toFixed(2));
                        }
                    });
                }


                // BOOTSTRAP COMPONENTS GENERATION
                // -------------------------------
                if ($.__cmp.JSbsPopover) {
                    $('a.JS_bs-popover').popover();
                }


                //MAX LENGTH
                //----------
                if ($.__cmp.JSmaxLength) {

                    // Max Length Input
                    $('input[maxlength],textarea[maxlength]').each(function () {
                        var $this = $(this),
                            data = $this.data(),
                            $parent = $this.parent(),
                            maxCharLength = parseInt($this.attr("maxlength"), 10);

                        if (maxCharLength >= 0 || maxCharLength <= 4000) {
                            var charLength = $this.val().length;

                            if (data.showcounter || data.showcounter === undefined) {
                                if (!$parent.find('span.max-length-count').length) {
                                    $parent.append('<span class="max-length-count">' + (maxCharLength - charLength) + '</span>');
                                } else {
                                    $parent.find('span.max-length-count').html(maxCharLength - charLength);
                                }
                            }
                            $this.on('keypress keyup', function (e) {

                                var charLength = $(this).val().length;
                                var newLinesCount = $(this).val().split('\n').length - 1;
                                $parent.find('span.max-length-count').html(maxCharLength - charLength - newLinesCount);
                                if ((charLength + newLinesCount) === maxCharLength) {
                                    $parent.find('span.max-length-count').addClass("max");
                                } else {
                                    $parent.find('span.max-length-count').removeClass("max");
                                }
                                if (charLength >= maxCharLength) {
                                    $this.val($this.val().substring(0,maxCharLength));
                                }
                            });
                        }
                    });

                }



                if ($.__cmp.JSlistCounterSmall) {

                    $('ul.list-counter-small.with-arrow li.selected').each(function () {
                        var $this = $(this);
                        if (!$this.find('.arrow').length) {
                            $this.append('<span class="arrow"></span>');
                        }
                    });

                    $('ul.list-counter-small li.with-children').each(function () {
                        var $this = $(this);
                        if (!$this.find('.icon-expand').length) {
                            $this.append('<span class="icon icon-expand"></span>');
                        }
                    });
                }

                // SORTABLE LIST
                // -------------
                if ($.__cmp.JSsortable) {

                    $('.JS_sortable').sortable({
                        deactivate: function () {
                            $._log('serialize = [' + $(this).sortable("serialize", { key: "sort" }) + ']  ' + 'toArray = [' + $(this).sortable("toArray") + ']');
                        }
                    });
                }


                //apply checkedPolyfill on radios input for IE7/8 compat
                if ($.__cmp.JSradio) {
                    $('.JS_radio').checkedPolyfill();
                }

                if ($.__cmp.JScheckbox) {
                    $('.JS_checkbox').checkedPolyfill();
                }

                //select2 init
                if ($.__cmp.JSselect2) {

                    try {
                        $('select.JS_select2').select2({ width: 'resolve' });
                    } catch (e) {
                    }
                }

                // AUTOCOMPLETE FIELD
                // ------------------
                if ($.__cmp.JSautocomplete) {

                    $('.JS_autocomplete').each(function () {
                        var $this = $(this);

                        var opt = $this.data();
                        var source;

                        if (opt.sourceType === 'local') {
                            source = p[opt.sourceLocalArray];
                        } else if (opt.sourceType === 'remote') {
                            source = $._getContextPath() + opt.sourceRemoteUrl;
                        }

                        $this.autocomplete({
                            source: source,
                            minLength: 2,
                            response: function (event, ui) {
                                if (!ui.item) {
                                    if ((opt.hasResultElement) && (opt.allowFreeText)) {
                                        //Nothing has been selected => remove id for hasResultElement when free text is allowed
                                        var $result = $('#' + $this.attr('id') + '_result');
                                        if ($result.get(0).nodeName === 'INPUT') {
                                            $result.val('');
                                        } else {
                                            $result.html('');
                                        }
                                    }
                                }
                            },
                            select: function (event, ui) {
                                $this.data('item', ui.item);
                                if (opt.hasResultElement) {
                                    var $result = $('#' + $this.attr('id') + '_result');
                                    if ($result.get(0).nodeName === 'INPUT') {
                                        if (ui.item) {
                                            $result.val(ui.item[opt.resultPropertyName]);
                                        } else {
                                            $result.val('');
                                        }
                                    } else {
                                        if (ui.item) {
                                            $result.html(ui.item[opt.resultPropertyName]);
                                        } else {
                                            $result.html('');
                                        }
                                    }
                                }
                                $this.change();
                            },
                            close: function () {
                                var item = $this.data('item');
                                if (item) {
                                    if (opt.replacePropertyName !== undefined) {
                                        $this.val(item[opt.replacePropertyName]);
                                    }
                                } else {
                                    $this.val('');
                                }
                                $._isBlockerPage = true;
                                $._settings.isInitDisplayOnAjaxStop = true;
                            }
                        });

                        if (opt.listItemHtml !== undefined && opt.listItemPropertyNames !== undefined) {

                            $this.data("ui-autocomplete")._renderItem = function (ul, item) {

                                var idx,
                                    listItem = opt.listItemHtml,
                                    listItemNames = opt.listItemPropertyNames.split(',');

                                for (idx = 0; idx < listItemNames.length; idx++) {
                                    listItem = listItem.replace('$' + (idx + 1), item[listItemNames[idx]]);
                                }

                                return $("<li>")
                                    .append(listItem)
                                    .appendTo(ul);
                            };
                        }

                    });
                }



                // SUMMERNOTE initialisation

                if ($.__cmp.JSsummernote) {

                    $('.JS_summernote').each(function () {
                        var $this = $(this),
                            opt = $this.data();

                        if (opt.height === undefined) {
                            opt.height = 200;
                        }

                        $this.summernote({
                            height: opt.height,
                            //see http://summernote.org/#/features for additional features but AVOID fontnames if you don't want to have a messy generated code!!
                            toolbar: [
                                ['style', ['style', 'bold', 'italic', 'underline', 'clear']],
                                ['fontsize', ['fontsize']],
                                ['color', ['color']],
                                ['para', ['ul', 'ol', 'paragraph']],
                                ['height', ['height']]
                            ]
                        });

                    });




                }






                //FIELD REQUIRED
                //--------------
                //add the required icon on the field required : usage <div class="field required">
                $('div.field.required').each(function () {

                    var $this = $(this);


                    //dynamic insert of the required icon if not already present inside the required field
                    if (!$this.find('span.required-toggle').length) {

                        if ($this.find('br').length) {
                            $this.find('br').before('<span class="required-toggle required-required"></span>');
                        } else {

                            /* todo test in every cases

                             var $input = $this.find('div.label').next();
                             if (!$input.length) {
                             $input = $this.find('div.field-label').next();
                             }

                             if ($input.length) {
                             $input.after('<span class="required-toggle required-required"></span>');
                             } else {
                             */

                            $this.append('<span class="required-toggle required-required"></span>');

                            //}
                        }

                    }

                    if ($.__modules.VALIDATION) {
                        //checking the required field on init
                        //if the field have been initialised with some valid values for changing the required-toggle
                        //if the field contains more than one component to be validated, step through the returned array if not empty
                        $this.find('.JS_live-validation').each(function () {
                            $.__liveValidation($(this));
                        });
                    }

                    $this = null;
                });



                if ($._settings.isFlatThemeActive) {

                    $('.js-serialaccordion').serialaccordion({
                        getTarget: function( $trigger, $wrapper ) {
                            return $wrapper.find( '.js-serialaccordion-content' );
                        }
                    });

                    $('.header-user-navigation [data-serialexpand]').serialexpand({
                        position: 'bottom right'
                    });

                    $('.header-page-navigation [data-serialexpand]').serialexpand({
                        position: 'bottom left'
                    });

                    $('[data-serialtabs]').serialtabs({ target: 'data-serialtabs' });

                    $('[data-serialbox]').serialbox();

                    //$('.tasks .notification-count-inner').text( $('.homepage-widget-content .task-list li').not('.checked').length );
                    $('.header-user-navigation .messages .notification-count-inner').text( $('.header-user-messages .message' ).length );

                    $.__COMPONENTS.iaResizeDashboard();

                    $( 'body' ).css( 'overflow', 'auto' );

                }








                //jSCAF plugins initialisation


                if ($.__cmp.JSinlineEdit && $.fn.JSinlineEdit !== undefined) {

                    $('.JS_inlineEdit').JSinlineEdit();

                }


                if ($.__cmp.JSwizard && $.fn.JSwizard !== undefined) {

                    $('.JS_wizard').JSwizard();

                }





                //init display of each registered components

                $._execFn($._pageComponentsInitDisplay(), true);


            },


            initDefaultTable: function () {
                //applying zebra style
                var i = 1;

                $('table.default:not(.dataTable) > tbody > tr').each(function () {
                    var $this = $(this);

                    if (!$this.hasClass('bg') && // allow different bg color for row
                        !($this.closest('table').hasClass('sub-table')) && // content of hidden hierarchical table are not taken into account
                        !($this.hasClass('sub-row')) && // main hidden row or hierarchical table are not taken into account
                        !($this.closest('table').hasClass('no-zebra'))) {
                        if (i % 2 === 0) {
                            $this.removeClass('zebra2').addClass('zebra1');
                        } else {
                            $this.removeClass('zebra1').addClass('zebra2');
                        }
                        i++;
                    }
                });

            }




        },


        /* ======================================================= */
        /* ===            MANUAL TRIGGERS                       == */
        /* ======================================================= */



        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _tabSwitch
         * @description todo
         */
        _tabSwitch: function (tabLinkId, tabsContainerId, tabContentClass) {
            if (tabContentClass !== undefined) {
                $('.' + tabContentClass).addClass('hidden');
            } else {
                $('.tabContent').addClass('hidden');
            }

            if (tabsContainerId !== undefined) {
                $('#' + tabsContainerId + ' li').removeClass('selected');
            } else {
                $('.tabLink').parent().removeClass('selected');
            }

            var $tabLink = $('#' + tabLinkId);

            $tabLink.parent().addClass('selected');
            $('#' + $tabLink.attr('id') + '_tabContent').removeClass('hidden');
        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _gridBoxSwitch
         * @description todo
         */
        _gridBoxSwitch: function($o) {
            $o.addClass('selected').siblings().removeClass('selected');
        },

        _activeWizardIndex: 0,


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _wizardStepSwitch
         * @description todo
         */
        _wizardStepSwitch: function (idx, step, isDynamicContent, wizardId) {

            var $wizardStepLi,
                $actionWizardNextPanel,
                $actionWizardPreviousPanel;

            if (wizardId === null || wizardId === undefined) {
                $wizardStepLi = $('.wizard-steps li');
                $actionWizardNextPanel = $('#action_wizard_next_panel');
                $actionWizardPreviousPanel = $('#action_wizard_previous_panel');
            } else {
                $wizardStepLi = $('#' + wizardId).find('li');
                $actionWizardNextPanel = $('#' + wizardId + '_action_wizard_next_panel');
                $actionWizardPreviousPanel = $('#' + wizardId + '_action_wizard_previous_panel');
            }

            if (step !== undefined && step !== null) {
                if ($._activeWizardIndex + step <= $wizardStepLi.length - 1 && $._activeWizardIndex + step >= 0) {
                    $._activeWizardIndex += step;
                }
            } else {
                $._activeWizardIndex = idx;
            }

            var wizardLink = $wizardStepLi.eq($._activeWizardIndex).find('a');

            $wizardStepLi.removeClass('active');
            wizardLink.parent().addClass('active');

            $._activeWizardIndex = $('.wizard-steps').find('.active').index();

            if ($._activeWizardIndex === $wizardStepLi.length - 1) {
                $actionWizardNextPanel.hide();
            } else {
                $actionWizardNextPanel.show();
            }

            if ($._activeWizardIndex === 0) {
                $actionWizardPreviousPanel.hide();
            } else {
                $actionWizardPreviousPanel.show();
            }

            if (!isDynamicContent || isDynamicContent === undefined) {
                $('.wizard-step-content').hide();
                $('#' + wizardLink.attr('id') + '_content').removeClass('hidden').fadeIn();
            }


        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _accordionPanelSwitch
         * @description todo
         */
        _accordionPanelSwitch: function (id) {
            var $this = $('#'+id);
            $this.removeClass('collapsed').addClass('expanded')
                .siblings().removeClass('expanded').addClass('collapsed');
        },





        /* ======================================================= */
        /* ===            ADDITIONAL VISUAL COMPONENTS          == */
        /* ======================================================= */

        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _createTopMessage
         * @description display a top message over the header to emphasize a validation client-side or server-side
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.type css class of the top message <br><i>DEFAULT : error</i>
         * @param opt.title title of the message
         * @param opt.subTitle subTitle of the message
         * @param opt.content content of the message in plain text
         * @param opt.contentArray if the content is null, then an array of message should be provided
         */

        _createTopMessage: function (opt) {
            var settings = $.extend({
                type: 'error',
                title: null,
                subTitle: null,
                content: null,
                contentArray: null,
                stay: true,
                displayTime: 6000
            }, opt);

            //Destroy top message first if already exists in the DOM
            $._destroyTopMessage();

            //Constructing the message content
            var content = '';
            content += '<div id="top_message" style="width:' + $._$body.width() + 'px;top:-200px;">';
            content += '<div class="close-message"></div>';
            content += '<div class="' + settings.type + ' message">';

            if (settings.title !== null) {
                content += '<h3>' + settings.title + '</h3>';
            }

            if (settings.subTitle !== null) {
                content += '<p>' + settings.subTitle;
            }

            if (settings.content !== null) {
                content += settings.content;
            } else if (settings.contentArray !== null) {
                var i;
                content += '<p>';
                for (i = 0; i < settings.contentArray.length; i++) {
                    content += '<li class="action_highlight_error cr-pointer">' + settings.contentArray[i].messageText + '</li>';
                }
                content += '</p';
            }
            content += '</div></div>';

            //pre-pending the top_message to the body : must be the first element (z-index...)
            $._$body.prepend(content);

            //do some funny things
            $('#top_message').animate({top: 0}, {duration: 750, easing: 'easeOutBounce'});

            //if displayTime provided as input param, the stay parameter is set to false,
            //this way only displayTime can be provided to force the message to disappear automatically
            if (opt.displayTime !== undefined) {
                settings.stay = false;
            }

            //remove the message after a displayTime period of time
            if (settings.displayTime !== null && !settings.stay) {
                setTimeout(function () {
                    $._removeTopMessage();
                }, settings.displayTime);
            }

            //bind the close event for direct closing the message
            $('.close-message').on('click', function () {
                $._removeTopMessage();
            });

        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _removeTopMessage
         * @description todo
         */
        _removeTopMessage: function () {
            //un-bind for safety
            $('.close-message').off('click');

            //fade and remove from DOM
            $('#top_message').fadeOut(500, function () {
                $('#top_message').remove();
            });
        },

        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _destroyTopMessage
         * @description todo
         */
        _destroyTopMessage: function () {
            //un-bind for safety
            $('.close-message').off('click');

            //fade and remove from DOM
            $('#top_message').remove();
        },



        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _highlightErrorOnScreen
         * @description todo
         */
        _highlightErrorOnScreen: function (errorId, errorsMap) {
            var tabId, errorTypeId, $parent, parentId, errorClass, scrollOffset, $topMessage;

            if (errorsMap[errorId] === null || errorsMap[errorId] === undefined) {
                return;
            }

            tabId = errorsMap[errorId].tabId;
            parentId = errorsMap[errorId].parentId;
            errorClass = errorsMap[errorId].errorClass;

            //todo auto switch tab and put highlight here under in callback fn

            $._log('DISPLAY.highlightErrorOnScreen : tabId:' + tabId + ' ' + 'errorTypeId:' + errorTypeId + ' element: parentId=' + parentId + ' errorClass=' + errorClass);

            //setting offset if top_message exists
            $topMessage = $('#top_message');
            if ($topMessage.length) {
                scrollOffset = $topMessage.height() - 60;
            } else {
                scrollOffset = 0;
            }

            if (parentId !== null) {

                $parent = $('#' + parentId);

                if (errorClass !== null) {
                    $._scrollToElement(
                        $parent.find('.validation_error').find('.' + errorClass), scrollOffset
                    ).effect("highlight", {color: "#ff0000"}, 1500);
                } else {
                    if ($parent.find('.validation_error').length) {
                        $._scrollToElement(
                            $parent.find('.validation_error'), scrollOffset
                        ).effect("highlight", {color: "#ff0000"}, 1500);
                    } else {
                        $._scrollToElement(
                            $parent, scrollOffset
                        ).effect("pulsate", { times: 2 }, 1000);
                    }
                }

            } else {

                $._scrollToElement(
                    $._$form.find('.' + errorClass), scrollOffset
                ).effect("highlight", {color: "#ff0000"}, 1500);

            }

        }















    });




}(jQuery));











/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF DISPLAY MODULE                   == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        __DISPLAY: {

            hdMarginWidth: null,
            initPhase: false,

            __$topNav: null,
            __topNavPosition: null,


            init: function() {
                $._log('DISPLAY.init');
                

                // DISPLAY SETTINGS EVENTS
                // -----------------------
                if ($._settings.isDisplaySettingsUsed) {
                    $._$document.on('click',
                        '.JS_fontsize_switcher',
                        function () {
                            $.__DISPLAY.switchFontsize($(this).attr('fontsize'));
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });

                    $._$document.on('click',
                        '.JS_theme_switcher',
                        function () {
                            $.__DISPLAY.switchTheme($(this).attr('themeName'));
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });

                    $._$document.on('click',
                        '.JS_hd_margin_switcher',
                        function () {
                            $.__DISPLAY.switchHdMarginWidth($(this).attr('marginWidth'));
                            $.__DISPLAY.initPhase = false;
                            $.__DISPLAY.initResponsiveness();
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });
                }


                // GLOBAL SETTINGS EVENTS
                // ----------------------
                if ($._settings.isKeepTopNavigationFixed) {
                    $.__DISPLAY.__$topNav = $('ul.top-nav'); 
                    $.__DISPLAY.__topNavPosition = $.__DISPLAY.__$topNav.offset();

                    $(window).scroll(function(){
                        $.__DISPLAY.refreshTopNavigation();  
                    });
                }



                //SETTING THE RESPONSIVE ENABLER
                //------------------------------
                $.__DISPLAY.initPhase = true;
                $.__DISPLAY.initResponsiveness();
                $.__DISPLAY.initPhase = false;


                //set displaySettings hd margin width visibility according to screen dimension detected
                if ($._hasClass($._html,'ishd')) {
                    $._removeClass($._getById('display-settings-container-hd'),'hidden');
                    $.__DISPLAY.initHdMarginWidth();
                }                



                //init display settings
                $.__DISPLAY.initTheme();
                $.__DISPLAY.initFontsize();

            },
            

            initResponsiveness: function() {
                $._log('DISPLAY.initResponsiveness');

                if ($.__DISPLAY.initPhase) {
                    
                    //INIT SYZE DETECTION AND CALLBACK
                    //--------------------------------

                    if ($._settings.isMinimalScreenWidthDetectionActive) {
                        syze.sizes(0, 480, 960, 1400, 2500).from('browser').names({ 0: 'unsupported', 480: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                    } else {
                        if ($._settings.isMinimalScreenWidthDetectionActiveForIE78Only && ($._isIE7() || $._isIE8())) {
                            syze.sizes(0, 480, 960, 1400, 2500).from('browser').names({ 0: 'unsupported', 480: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                        } else {
                            syze.sizes(0, 960, 1400, 2500).from('browser').names({ 0: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                        }
                    }

                    syze.debounceRate(250);

                } else {

                    $.__DISPLAY.refreshResponsiveness();
                }    

            },


            syze: function() {
                syze.callback(function () {
                    $.__DISPLAY.refreshResponsiveness();
                }); 
            },


            refreshResponsiveness: function () {

                $._log('DISPLAY.refreshResponsiveness : ENABLED');

                var isMobile = $._hasClass($._html,'ismobile'),
                    isHD = $._hasClass($._html,'ishd'),
                    isDoubleScreen = $._hasClass($._html,'isdoublescreen'),
                    isDesktop = $._hasClass($._html,'isdesktop'),
                    isUnsupported = $._hasClass($._html,'isunsupported'),
                    width = (window.innerWidth || document.documentElement.clientWidth),
                    toTop = $._getById('totop');


                $._$body.removeClass('mlr-0 mlr-sb-0 mlr-10 mlr-sb-10 mlr-20 mlr-sb-20 mlr-30 mlr-sb-30 no-margin width-sb-80');

                //HD resolution contraction : always keep 1280px width on higher resolution >1400px width, otherwise it's unreadable
                if (isHD && !isDoubleScreen) {
                    //add margins left and right of the body + adjust header elements position
                    var marginWidth = $.__DISPLAY.hdMarginWidth;

                    if (marginWidth === null) {
                        marginWidth = $.__DISPLAY.getHdMarginWidth();
                    }

                    if ($._settings.isSlidebarsActive && $._hasClass($._$html[0],'sb-active-left')) {

                        $._$body.addClass('hd mlr-sb-' + marginWidth);

                    } else {

                        $._$body.addClass('hd mlr-' + marginWidth);

                    }

                    $.__DISPLAY.hdMarginWidth = marginWidth;                    

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','15%');
                    }

                } else {
                    //go back to the 1280px width initial state
                    $._removeClass($._$body[0],'hd');

                    if ($._settings.isSlidebarsActive && $._hasClass($._$html[0],'sb-active-left')) {
                        $._addClass($._$body[0],'width-sb-80');
                    } else {
                        $._addClass($._$body[0],'no-margin');
                        $._removeClass($._$body[0],'width-sb-80');   
                    }

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','20%');
                    }

                    $.__DISPLAY.hdMarginWidth = null;
                }




                //check for mobile resolution or desktop between 960px and 1024px wide resolution
                if (isMobile || (isDesktop && width <= 1024)) {

                    //hide table column when resolution is less than minimum target (<1024px)
                    $('td.optional,th.optional').hide();
                    $('.table-menu.table-menu-hidden').find("input").trigger("updateCheck");

                    //transform tables td iconifiable elements
                    $('td.JS_iconifiable').each(function () {
                        var $this = $(this);
                        //setting the associated th width for icon display and remove column header text
                        if (!$this.find('span').length) {
                            $this.closest('table')
                                .find('th')
                                .eq($this.index())
                                .attr('width', '16px')
                                .text('');

                            //text content of td is put on the title attribute for tooltip display
                            $this.attr('title', $.trim($this.text()));

                            //text content is replaced by an icon
                            $this.html('<span class="icon-table icon-purpose"></span>');
                        }
                    });

                    //transform sub-rowable columns into alternate row display
                    $('td.JS_subrowable').each(function () {
                        var $this = $(this);

                        $._log($this);

                        var $th = $this.closest('table')
                            .find('th')
                            .eq($this.index())
                            .hide();

                        if (!$th.prev().find('.second-line').length) {
                            $th.prev()
                                .append('<span class="second-line">' + $th.text() + '</span>');
                        }

                        if (!$this.prev().find('.second-line').length) {
                            $this.hide().prev()
                                .append('<span class="second-line">' + $this.text() + '</span>');
                        }
                    });
                }


                //show table columns when resolution is normal target (> 1024px)
                if ((isDesktop && width > 1024) || isHD || isDoubleScreen) {
                    $('td.optional,th.optional').show();
                    $('.table-menu.table-menu-hidden').find("input").trigger("updateCheck");

                    //show column again
                    $('td.JS_subrowable').each(function () {
                        var $this = $(this);

                        $this.closest('table')
                            .find('th')
                            .eq($this.index())
                            .show()
                            .prev()
                            .find('.second-line')
                            .remove();

                        $this.show()
                            .prev()
                            .find('.second-line')
                            .remove();

                    });
                }

                //check for mobile resolutions : between 960px and less than 1024px wide
                if (isMobile) {

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','50%');
                    }                        


                    //normal floats for ul#dashboard-buttons if more than 400px width
                    if (width < 400) {
                        $('#dashboard-buttons').addClass('smallmobile');
                    } else {
                        $('#dashboard-buttons').removeClass('smallmobile');
                    }

                    //transform tabs into partial select box
                    var tabs = $('ul.tabs');

                    if (tabs.length) {


                        //check if a tab has been hidden due to the resize
                        var hiddenTab = false, prevOffsetLeft = 0;
                        tabs.find('li').each(function (idx) {
                            var $this = $(this),
                                thisOffsetLeft = $this.offset().left;
                            if (thisOffsetLeft < prevOffsetLeft) {
                                hiddenTab = true;
                            }
                            prevOffsetLeft = thisOffsetLeft;
                        });


                        //if at least a tab has been hidden, apply mobile transformation
                        if (hiddenTab) {

                            tabs.find('li:not(.selected)').addClass('hidden');

                            if (!$('#tab-previous').length) {
                                tabs.prepend('<i id="tab-previous" class="icon size24 icon-ft-arrow-left fl cr-pointer" style="margin-right:15px;"></i>');
                            }

                            if (!$('#tab-next').length) {
                                tabs.append('<i id="tab-next" class="icon size24 icon-ft-arrow-right cr-pointer" style="margin-left:10px;"></i>');
                            }

                        }

                    }

                } else {
                    //back to normal topnav
                    $('#topnav-menu').remove();
                    $('#topnav-menu-content').remove();
                    $('#topbar-menu').remove();
                    $('#topbar-menu-content').remove();
                    $('.top-nav').find('li').show();
                    $('.top-bar').find('li').show();

                    //reset all tabs
                    $('ul.tabs').find('li').removeClass('hidden');
                    $('#tab-previous').remove();
                    $('#tab-next').remove();

                }

                //check for less than highest mobile resolution supported : 600px
                var $unsupportedResolution = $('#unsupported_resolution');
                if (isUnsupported) {
                    $._$main.addClass('hidden');
                    $._$header.addClass('hidden');
                    $._$footer.addClass('hidden');
                    if (!$unsupportedResolution.length) {
                        var unsupportedContent = '';
                        unsupportedContent += '<div id="unsupported_resolution" style="text-align:center;font-family:Arial,sans-serif;font-size:20px;font-weight:bold;"><br>';
                        unsupportedContent += 'Unsupported resolution<br>';
                        unsupportedContent += '<span style="font-size:14px;font-weight: normal;">must be greater than 600px wide</span><br><br>';
                        unsupportedContent += '</div>';
                        $._$body.append(unsupportedContent);
                    }
                } else {
                    $unsupportedResolution.remove();
                    $._$main.removeClass('hidden');
                    $._$header.removeClass('hidden');
                    $._$footer.removeClass('hidden');
                }


                //filling empty space to avoid little screens reduced when no enough content to fill the screen height
                $.__fillClientHeight();        

                //when top-nav is fixed then refresh it it screen is resized
                if ($._settings.isKeepTopNavigationFixed) {
                    $.__DISPLAY.refreshTopNavigation();
                }


                //dashboard for flat design
                var $dashboardNav = $('#dashboard_nav');
                if ($dashboardNav.length) {
                    $( '.dashboard-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
                }

            },


            refreshTopNavigation: function() {
                if($(window).scrollTop() > $.__DISPLAY.__topNavPosition.top){
                   $.__DISPLAY.__$topNav.css('position','fixed').css('top','0').css({'width':$('#header').width()-19}).addClass('light');
                } else {
                   $.__DISPLAY.__$topNav.css('position','absolute').css('top',$.__DISPLAY.__topNavPosition.top).removeClass('light');
                }  
            },


                   


            /* ======================================================= */
            /* ===        DISPLAY SETTINGS FUNCTIONS                == */
            /* ======================================================= */
            initTheme: function () {
                $._log('DISPLAY.initTheme');

                var theme = $._getCookie("appTheme");

                if (theme === undefined || theme === null || theme === 'color' || theme === 'default-min.css') {    //keep old cookie entry for backward compat. reasons
                    theme = 'color';
                } else {
                    theme = 'bw';
                }

                $.__DISPLAY.switchTheme(theme);
            },

            switchTheme: function (theme) {
                $._log('DISPLAY.switchTheme');

                $('.JS_theme_switcher').removeClass('selected');
                $('.JS_theme_switcher[themeName="' + theme + '"]').addClass('selected');

                if (theme === 'bw') {
                    $._addClass($._html,'bw');
                } else {
                    $._removeClass($._html,'bw');
                }

                $._setCookie('appTheme', theme, 365);

            },







            initFontsize: function () {
                $._log('DISPLAY.initFontSize');

                var fontsize = $._getCookie("appFontsize");

                if (fontsize === null || fontsize === undefined || fontsize === 'font_default' || fontsize === 'default-min.css') {    //keep old cookie entry for backward compat. reasons
                    fontsize = 'font_default';
                }

                $.__DISPLAY.switchFontsize(fontsize);

            },

            switchFontsize: function (fontsize) {
                $._log('DISPLAY.switchFontsize');

                $('.JS_fontsize_switcher').removeClass('selected');
                $('.JS_fontsize_switcher[fontsize="' + fontsize + '"]').addClass('selected');

                $._removeClass($._html,'font_default');
                $._removeClass($._html,'font_max');
                $._removeClass($._html,'font_min');
                $._addClass($._html,fontsize);

                $._setCookie('appFontsize', fontsize, 365);

            },








            getHdMarginWidth: function() {
                $._log('DISPLAY.getHdMarginWidth');

                var marginWidth = $._getCookie("appHdMarginWidth");

                if (marginWidth === null || marginWidth === undefined || (marginWidth !== '0' && marginWidth !== '10' && marginWidth !== '20' && marginWidth !== '30')) {
                    marginWidth = '30';
                }

                return marginWidth;
            },


            initHdMarginWidth: function () {
                $._log('DISPLAY.initHdMarginWidth');
                $.__DISPLAY.switchHdMarginWidth($.__DISPLAY.getHdMarginWidth());
            },

            switchHdMarginWidth: function (marginWidth) {
                $._log('DISPLAY.switchHdMarginWidth');

                $('.JS_hd_margin_switcher').removeClass('selected');
                $('.JS_hd_margin_switcher[marginWidth="' + marginWidth + '"]').addClass('selected');
                
                $.__DISPLAY.hdMarginWidth = marginWidth;

                $._setCookie('appHdMarginWidth', marginWidth, 365);

            }


        }







    });    
            
}(jQuery));











/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/




/* ======================================================= */
/* ===                 JSCAF INIT                       == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({

        __initModuleIdentifier: function() {
            return true;
        },

        /* ======================================================= */
        /* ===             INIT FUNCTIONS                       == */
        /* ======================================================= */


        __startInit: function() {

            $._log('INIT.startInit <= this is where all begins');

            var initObject = null;

            //by default we look for a page object

            if ( window.p !== undefined ) {
                initObject = window.p;
            }


            //in the case of a modules-based page controller, a mod object is wrapping the modules definition
            if ( window.mod !== undefined ) {
                initObject = window.mod;
            }



            if ( initObject === null && !$._settings.isPrototypeMode ) {

                //init only the mandatory stuffs for a basic page (logout/etc...)
                $.__initCommon();
                $.__initPostCommon();

            } else {    

                // we are in the prototype mode !!
                if ( initObject === null && $._settings.isPrototypeMode ) {

                    $.__PROTOTYPE.init();
                    $._init();

                } else {

                    if ( initObject.__init === undefined ) {

                        // the page controller is empty, the default _init() function is called
                        $._init();

                    } else {                    

                        var initResult = initObject.__init(),
                            initResultType = typeof initResult;

                        //the returned value of the p.__init function must be either 
                        
                        //a function

                        if ( initResultType === 'function' ) {

                            //the function is executed, the function is assuming the call to the $._init() function
                            initResult();


                        //or an settings object         

                        } else if ( initResultType === 'object' ) {

                            //the result of the settings subclassed in the p.__init are injected into the core $._init() function called    
                            $._init( initResult );

                        } else {
                            
                            throw new Error('jscaf::ERROR -- your page controller __init function must return a settings object OR a function  - cannot initialise the page');

                        }

                        initResult = initResultType = null;

                    }
                }    

            } 

            initObject = null;

        },





        /* ======================================================= */
        /* ===             MAIN INIT FUNCTION                   == */
        /* ======================================================= */


        // DEFAULT INIT & PARAMETERS
        // -------------------------
        /**
         * @description
         * The page initialisation function, must be called during document.ready() on page controller to initialize the current page. This will ensure callbacks to own p.initDisplay() and p.bindEvents() on the page controller.
         *
         * @name _init
         * @memberOf $
         * @class .
         * @param opt the object literal parameter {object} <br><i>default : $._settings</i>
         * @example
         * //basic call on page controller document.ready()
         * var p = {
         *   __init: function() { return {}; },  //optional  
         *   initDisplay: function() { ... $._initDisplay(); }, //optional
         *   bindEvents: function() { ... } // optional
         * };
         *
         *
         * //dynamic component example
         * var p = {
         *   __init: function() { 
         *       return {
         *          JScomponents: {
         *              JStooltip: true,
         *              JSbuttonSet: true
         *          }
         *   },      
         *   initDisplay: function() { ... } // optional
         *   bindEvents: function() { ... } // optional
         * };
         *
         */
        _init: function (opt, fn) {
            $._log('INIT.init');

            //extending the jSCAF settings objects with override options
            $._settings = $._extend($._settings, opt);

            //checking some settings values
            if ($._settings.jscafRootUrl === null) {
                $._settings.jscafRootUrl = $._getContextPath();
            }


            //init pre call function
            if ($._settings.fnInitPreCall !== null) {
                $._execFn($._settings.fnInitPreCall);
            }
            
            //branching on init type : light/full
            if ($._settings.isLightInitialisationActive) {

                $.__initLight();

            } else {

                $.__initFull();

            }
        },


        __initLight: function() {
            $._log('INIT.init LIGHT');

            $.__initCommon();

            //dynamic initPage function, with load innerFragment or override function if provided
            $.__initPageLight();

            //calling callback function if provided
            if ($._settings.fnInitPostCall !== null) {
                $._execFn($._settings.fnInitPostCall, true);
            }

        },


        __initFull: function() {
            $._log('INIT.init FULL');


            //Do init only if browser is supported
            if ($.__modules.BROWSER) {
                if (!$.__BROWSER.isCheckBrowserSupported) {
                    return;
                }
            }

            $.__initCommon();

            //dynamic initPage function, with load innerFragment or override function if provided
            if ($._settings.isPageDefaultInit) {
                //init page => normal way
                $.__initPageFull();
            } else {
                //init page => the page has it's own defined init function
                if (null !== $._settings.fnPageInitOverrideCallback) {
                    $._execFn($._settings.fnPageInitOverrideCallback);
                } else {
                    $._initDisplay();
                }
            }

            //check if the onBeforeUnload must be bound, warning the user if the browser/tab is closed before saving the actual page
            if ($._settings.isOnBeforeUnloadActive) {
                $._addOnBeforeUnloadEvent();
            }

            //enable timeoutDialog for alerting user when the sessions will soon expire
            if (!$._isEmpty($._settings.timeoutDialogProperties) && $.__modules.UI_DIALOG) {
                $.__UI_DIALOG.initTimeoutDialog();
            }


        },


        __initCommon: function() {
            $._log('INIT.initCommon');


            //initialisation common events
            if ($.__modules.AJAX) {
                $.__AJAX.initAjaxEvents();
            }

            //set the browser class on top html tag for targeting special browser in css declarations
            if ($.__modules.BROWSER) {
                $._addClass($._body,$.__BROWSER.getBrowserShortName().toLowerCase());
            }    

            if ($._settings.isFlatThemeActive) {
                $._addClass($._body,'flat');
            } else {
                if ($._$body.hasClass('flat')) {
                    $._settings.isFlatThemeActive = true;
                }
            }

            if ($._settings.isBootstrapActive) {
                $._addClass($._body,'bootstrap');
            }            

            //components init
            if ($.__modules.COMPONENTS) {
                //define the components array
                $.__COMPONENTS.define();                
                //initialises event handlers for common JS components
                $.__COMPONENTS.bindEvents();
            }     

            //plugins init
            //if ($.__modules.PLUGINS) {
                //load the plugins defined in $._settings.JSplugins on app settings OR page settings definitions
            //    $.__PLUGINS.load();
            //}                

            //try to execute the application controller init and bindEvents
            if (typeof window[$._settings.appName].__init === 'function') {
                $._execFn(window[$._settings.appName].__init);         
            }
            if (typeof window[$._settings.appName].bindEvents === 'function') {
                $._execFn(window[$._settings.appName].bindEvents);
                $.__initAppComponents();
            }

            //call initEvents : events not part of components but general to jSCAF
            $.__initGlobalEvents();

        },


        __initGlobalEvents: function() {

            //bind events for slidebars if active
            if ($._settings.isSlidebarsActive) {
                $._$document.on('click','.JS_slidebar-toggle', function() {
                    $._slidebars.slidebars.toggle('left');
                    if ($.__modules.DISPLAY) {
                        $.__DISPLAY.refreshResponsiveness();
                    }                    
                });
            }


        },


        __initPageLight: function() {
            $._log('INIT.initPage LIGHT');

            $.__initPostCommon();

            //$._initDisplay(); //fix Sysper bug : called twice during light init, already called in initPostCommon

            $._execFn($._settings.fnPageBindEventsCallback);
        },

        __initPageFull: function() {
            $._log('INIT.initPage FULL');

            //checking if prototype-mode
            if ($._settings.isPrototypeMode) {
            
                $.__PROTOTYPE.includePageContent();

                $.__initPostCommon();
            
            } else {

                //load inner fragment is needed
                if ($._settings.hasPageInnerFragment) {
                    //for the $._initDisplay not being called twice, this is set to false by default on the Ajax stop common event,
                    // p.initDisplay(), called on ajax complete will handle the call to $._initDisplay() by itself
                    $._settings.isInitDisplayOnAjaxStop = false;
                    if ($.__modules.AJAX) {
                        $.__AJAX.loadInnerFragment();
                    }    
                } else {
                    if ($._settings.koActive) {
                        $.__initKnockoutJS();
                    }
                    $.__initPostCommon();
                }

                //callback to the p.bindEvents() function to apply bindings on proper page elements
                $._execFn($._settings.fnPageBindEventsCallback);

            }




        },


        __initKnockoutJS: function() {

            //init knockout view model and bindings
            $._log('INIT.KnockoutJS: active');

            //retrieving model and applying the viewModel bindings

            if ($._settings.koViewModel === null) {

                $._log('INIT.KnockoutJS: p.ViewModel is not defined, unable to init KnockoutJS');

            } else {

                if ($._settings.koModelJsonGet !== null && $.__modules.AJAX) {

                    $._log('INIT.KnockoutJS.getting page model : ' + $._settings.koModelJsonGet.callOverride);
                    
                    $._ajaxJsonGet({
                        callOverride: $._settings.koModelJsonGet.callOverride,
                        fnPostCall: function() {
                            $.__koModel = $._ajaxJsonArray;    
                            $._log('INIT.KnockoutJS.page setting model in $.__koModel : ' + JSON.stringify($.__koModel));

                            ko.applyBindings(new $._settings.koViewModel());                                
                        }
                    });

                //applying the viewModel bindings, assuming the model json get is done manually    
                } else {

                    ko.applyBindings(new $._settings.koViewModel());                                

                }

            }
        },


        __initPostCommon: function(isCalledAfterAjaxError) {

            $._L('INIT.initPostCommon');


            //reset the init display flag for future ajax calls
            $._settings.isInitDisplayOnAjaxStop = true;

            //reveal the body
            $._removeClass($._main,'hidden');
            $._unblockUI();

            //Display module init

            if (!isCalledAfterAjaxError) {  // no need to re-initialise the base display after an ajax error
                if ($.__modules.DISPLAY) {
                    $.__DISPLAY.init();
                }
            }

            //callback the p.initDisplay() function of the page controller
            $._initDisplay();

            //load the translation messages if active
            if ($._settings.isI18nActive) {
                $.__initI18n();
            }

            //setting generation time in the footer if enabled
            if ($._settings.isPageGenerationTimeDisplayed) {
                if (!$._$footer.children().hasClass('generation_time')) {
                    $._$footer.append(
                        '<div class="generation_time center very-small text-color-grey">' +
                            $._getData('jscaf_common_generated_in') + $._getGenerationTime() + ')' +
                            '</div>');
                }
            }

            //calling callback function if provided
            if ($._settings.fnInitPostCall !== null && !isCalledAfterAjaxError) {
                $._execFn($._settings.fnInitPostCall, true);
            }




        },




        /* ======================================================= */
        /* ===             APP COMPONENTS INITIALISATION        == */
        /* ======================================================= */


        __initAppComponents: function () {
            $._log('INIT.initAppComponents');

            if ($._settings.appComponents === null) {
                return;
            }

            var cmpArray = [],
                prop;
            
            window[$._settings.appName]._cmp = {};
            for (prop in $._settings.appComponents) {
                if ($._settings.appComponents.hasOwnProperty(prop)) {
                    //replacing timestamp for force caching @deploy time
                    window[$._settings.appName]._cmp[prop] = window[$._settings.appName].__cmpDefs[prop]; //.replace('@pagescript.timestamp@', $._settings.appBuildTimestamp);
                    //keep only the components for later registering
                    cmpArray.push(prop);
                }
            }

            //require base configuration
            require.config({
                paths: window[$._settings.appName]._cmp
            });

            //requesting components and registering the components objects once loaded
            require(cmpArray, function () {
                var i, cmpArrayLength = cmpArray.length;
                
                for (i = 0; i < cmpArrayLength; i++) {
                    $._pageComponents.push(window[cmpArray[i]]);
                }
                $._pageComponentsBindEvents();
                
                i = cmpArrayLength = null;
            });


        }







    });
}(jQuery));





/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF UI DIALOG MODULE                 == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __UI_DIALOG: {

            mainWidth: null,
            windowWidth: null,
            windowHeight: null,

            calculateDimensions: function() {
                var $window = $(window);

                $.__UI_DIALOG.mainWidth = $._$main.width();
                $.__UI_DIALOG.windowWidth = $window.width();
                $.__UI_DIALOG.windowHeight = $window.height();

                $window = null;

            },

            initTimeoutDialog: function() {

                $._log('UI_DIALOG.initTimeoutDialog');

                //checking properties
                if ($._settings.timeoutDialogProperties.refreshDelayS === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a refreshDelayS property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.timeoutDelayM === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a timeoutDelayM property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.timeoutForwardUrl === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a timeoutForwardUrl property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.fnKeepAliveCalled === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a fnKeepAliveCalled property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a alertBeforeTimeoutDelayS property should be provided');
                    return;
                }


                //enabling interval
                $._settings.timeoutDialogProperties.currentInterval = 0;

                $._settings.timeoutDialogProperties.interval = self.setInterval(function() {

                    var curInt = $._settings.timeoutDialogProperties.currentInterval;
                    var triggeringDelayS = $._settings.timeoutDialogProperties.timeoutDelayM*60 - $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS;
                    var timeoutExpireDelayS = triggeringDelayS + $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS - curInt;

                    curInt += $._settings.timeoutDialogProperties.refreshDelayS;

                    /*
                    $._log('====>timeoutDialog details');
                    $._log('----------current interval : ' + curInt);
                    $._log('----------timeout delay : ' + $._settings.timeoutDialogProperties.timeoutDelayM*60);
                    $._log('----------alert before timeout delay : ' + $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS);
                    $._log('----------triggering delay : ' + triggeringDelayS);
                    $._log('----------timeout expires delay : ' + timeoutExpireDelayS);
                    */

                    //checking if interval is approaching the timeout delay
                    if (curInt >= triggeringDelayS && timeoutExpireDelayS > 0) {

                        //create the first time, if it doesn't exist yet
                        if (!$('#timeoutDialog').length) {
                            //make the browser blink on the task bar if not focussed (only work on IE8+)
                            window.focus();

                            //display the countdown dialog
                            $._msgbox({
                                dialogId: 'timeoutDialog',
                                title: $._getData('jscaf_timeout_dialog_title'),
                                subTitle: $._getData('jscaf_timeout_dialog_subTitle'),
                                text: $._getData('jscaf_timeout_dialog_text').replace('@COUNTDOWN@',timeoutExpireDelayS),
                                msgboxType: 'yes_no',
                                fnCallback: function(r) {
                                    if (r) {
                                        $._execFn($._settings.timeoutDialogProperties.fnKeepAliveCalled);
                                        $._settings.timeoutDialogProperties.currentInterval = 0;
                                    } else {
                                        $._navigateTo({url: $._settings.timeoutDialogProperties.timeoutForwardUrl});
                                    }
                                }
                            });
                        } else {
                            //refreshing the countdown on msgbox dialog
                            $('#timeoutDialog_delay').text(timeoutExpireDelayS);
                        }

                    //if expire timeout reached, then forward to the provided url
                    } else if (timeoutExpireDelayS === 0) {
                        $._closeDialog({dialogId: 'timeoutDialog'});
                        $._navigateTo({url: $._settings.timeoutDialogProperties.timeoutForwardUrl});
                    }

                    $._settings.timeoutDialogProperties.currentInterval = curInt;


                }, $._settings.timeoutDialogProperties.refreshDelayS*1000);

            },


            displayErrorDialog: function (htmlData, message, full) {
                $._log('UI_DIALOG.displayErrorDialog');


                $.__UI_DIALOG.calculateDimensions();

                var closeOnEscape = true;
                var closeFn = function () {
                    $('#globalErrorDialog').dialog("destroy").remove();
                };

                //init screen and reveal the body in case of error
                $.__initPostCommon(true);


                if (htmlData !== undefined) {

                    //in case a back to home page button is provided on the error page, the dialog is not closable,
                    //the back to home button is responsible for the forward.
                    if (htmlData.indexOf('back2homePageButton') >= 0) {
                        closeOnEscape = false;
                        closeFn = function () {};
                    }


                    if (htmlData.indexOf('EXCEPTION:') >= 0 || message.indexOf('EXCEPTION:') >= 0) {

                        if (message === 'EXCEPTION:EcasRedirectFailed') {

                            window.location.href = $._getContextPath() + $._settings.ecasRedirectFailedUrl;

                        } else {

                            var exceptionAction = '';
                            if (htmlData.indexOf('EXCEPTION:OUT_OF_SESSION') > 0) {
                                exceptionAction = 'outOfSessionException';
                            } else if (htmlData.indexOf('EXCEPTION:INVALID_SESSION') > 0) {
                                exceptionAction = 'invalidSessionException';
                            } else if (htmlData.indexOf('EXCEPTION:SECURITY') > 0) {
                                exceptionAction = 'securityException';
                            }

                            $._openDialog({
                                dialogId: 'exception_dialogFragment',
                                dialogTitle: 'APPLICATION ERROR',
                                closeOnEscape: false,
                                isAutoGenerated: true,
                                dialogWidth: 600,
                                isFragmentRefreshedBeforeOpen: true,
                                refreshedFragmentDispatchValue: 'showExceptionDialog',
                                refreshedFragmentAction: exceptionAction,
                                fnDialogOpen: function () {
                                    $('#exception_dialogFragment').prev().addClass('red');
                                }
                            });

                        }

                    } else {

                        $('<div id="globalErrorDialog"></div>').dialog({
                            autoOpen: false,
                            closeOnEscape: closeOnEscape,
                            resizable: false,
                            draggable: false,
                            width: $.__UI_DIALOG.windowWidth - 60,
                            height: $.__UI_DIALOG.windowHeight - 30,
                            modal: true,
                            close: closeFn,
                            open: function () {
                                var $globalErrorDialog = $('#globalErrorDialog');
                                $globalErrorDialog.prev().addClass('red');
                                if (!closeOnEscape) {
                                    $globalErrorDialog.parent().children().find('.ui-dialog-titlebar-close').hide();
                                }
                            }
                        });

                        var arrayToDisplay = [];
                        var idx = 0;
                        var i = 0;

                        if (full === undefined) {
                            full = true;
                        }

                        if (full) {
                            for (i = 0; i < $._logStack.length; i++) {
                                arrayToDisplay[idx] = $._logStack[i];
                                idx++;
                            }
                        } else {
                            for (i = 0; i < $._logStack.length; i++) {
                                if (i >= $._logStack.length - 100) {
                                    arrayToDisplay[idx] = $._logStack[i];
                                    idx++;
                                }
                            }
                        }

                        var $globalErrorDialog = $('#globalErrorDialog');

                        //set the content of the dialog the html received (the [error].jsp page)
                        $globalErrorDialog.html(htmlData);

                        var $errorContent = $('#error-content');

                        //extracting the content of the error (taking off the head and main body structure).
                        if ($errorContent.length) {
                            $globalErrorDialog.html($errorContent);

                            $('#globalErrorMessage_JS_stacktrace').html(arrayToDisplay.join('<br>'));

                            //fill javascript data here, cause script inside page are not interpreted inside dialog
                            $('#globalErrorMessage_JS_url').html(window.document.location.toString());
                            $('#globalErrorMessage_JS_navigator_userAgent').html(navigator.userAgent);
                            $('#globalErrorMessage_JS_navigator_platform').html(navigator.platform);

                        } else {

                            //by default or in case of a pure javascript error, javascript stack trace is displayed

                            var htmlOutput = '<div class="title"><span class="icon icon-warning fl"></span><span class="fl" style="width:90%;">ERROR</span></span><span class="icon icon-warning fr"></span></div>';

                            htmlOutput += '<table class="default fixed no-hover">';
                            htmlOutput += '    <caption><h5>Technical information</h5></caption>';
                            htmlOutput += '<thead>';
                            htmlOutput += '    <tr>';
                            htmlOutput += '        <th width="150px">INFO TYPE</th>';
                            htmlOutput += '        <th>VALUE</th>';
                            htmlOutput += '    </tr>';
                            htmlOutput += '</thead>';
                            htmlOutput += '    <tbody>';
                            htmlOutput += '    <tr>';
                            htmlOutput += '    <td class="center">JAVASCRIPT ERROR STACKTRACE</td>';
                            htmlOutput += '    <td class="text-color-blue">';

                            htmlOutput += arrayToDisplay.join('<br>');

                            htmlOutput += '    </td>';
                            htmlOutput += '    </tr>';
                            htmlOutput += '    </tbody>';
                            htmlOutput += '</table>';

                            $globalErrorDialog.html(htmlOutput);
                        }


                        //open the dialog
                        $globalErrorDialog.dialog("open");

                    }

                }


            }


        },


        //SHORTHANDS
        //----------
        _OD: function (opt) {
            $._openDialog(opt);
        },
        _OAD: function (opt) {
            $._openAjaxDialog(opt);
        },
        _OIFD: function (opt) {
            $._openIframeDialog(opt);
        },
        _CD: function (opt) {
            $._closeDialog(opt);
        },
        _MB: function (opt) {
            $._msgbox(opt);
        },



        /* ======================================================= */
        /* ===              UI-DIALOG FUNCTIONS                 == */
        /* ======================================================= */

        __currentDialogId: null,
        _isDialogAutoGenerated: false,
        __isFirstDialogShowCloseButton: false,


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openAjaxDialog
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be created <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentPageUrl {string} the server url used for the ajax call <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentPageForm {string} the server dispatchValue called <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentDispatchValue {string} the server dispatchValue called <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentAction {string} the server action parameter transferred <br><i>default : null</i>
         * @param opt.dialogWidth {number} the dialog width <br><i>default : 800</i>
         * @param opt.dialogHeight {string} the dialog height <br><i>default : 'auto'</i>
         * @param opt.dialogTitle {string} the dialog header title <br><i>default : 'DIALOG'</i>
         * @param opt.isAjaxBlockerActive {boolean} If a blocker please wait message must be shown when the dialog is displayed <br><i>default : true</i>
         * @param opt.isShowButtons {boolean} if the buttons declared using buttonPrimary/buttonSecondary (see next params) must be created on the dialog <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if the close button up right must be visible  <br><i>default : false</i>
         * @param opt.isAsyncCall {boolean} if the ajax call should asynchronous  <br><i>default : true</i>
         * @param opt.isOneButtonOnly {boolean} if true, only the primary button and action will be displayed/active.  <br><i>default : false</i>
         * @param opt.buttonPrimaryTitle {string} The title of the primary button  <br><i>default : 'OK'</i>
         * @param opt.buttonPrimaryFn {function} The function called when the primary button is clicked  <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.buttonSecondaryTitle {string} The title of the secondary button  <br><i>default : 'cancel'</i>
         * @param opt.buttonSecondaryFn {function} The function called when the secondary button is clicked  <br><i>default : function() { $._closeDialog(); }</i>
         * @param opt.fnPreCall {function} called during $.__AJAX.ajax() fnPreCall step  <br><i>default : null</i>
         * @param opt.fnAfterCreatePostCall {function} called during $.__AJAX.ajax() fnAfterCreatePostCall step  <br><i>default : null</i>
         * @param opt.fnDialogClose {function} called during $._openDialog() fnDialogClose step  <br><i>default : null</i>
         * @description
         * Facade for $._openDialog() function when an AJAX call must be executed before the dialog is displayed and the content of this dialog is auto-generated when a server-side fragment is refreshed.
         * @example
         * //basic call
         * $._openAjaxDialog({
         *      dialogId: 'myDialogId',
         *      refreshedFragmentDispatchValue: 'aServerAction',
         *      buttonPrimaryFn: function() {
         *          //primary action function
         *      }
         * });
         */
        _openAjaxDialog: function (opt) {
            $._log('UI_DIALOG.openAjaxDialog');

            //get defaults settings and subclass by options if provided
            var settings = $.extend({

                dialogId: null,
                id:null,
                refreshedFragmentPageUrl: null,
                refreshedFragmentPageForm: null,
                refreshedFragmentDispatchValue: null,
                refreshedFragmentDispatchValueOverride: null,
                call:null,
                callOverride:null,
                paramValue: null,
                refreshedFragmentAction: null,
                dialogWidth: 800,
                dialogHeight: 'auto',
                dialogTitle: 'DIALOG',
                dialogDraggable: true,
                isAjaxBlockerActive: true,
                isShowButtons: true,
                isShowCloseButton: false,
                isAsyncCall: true,
                isOneButtonOnly: false,
                buttonPrimaryTitle: 'OK',
                buttonPrimaryFn: null,
                buttonSecondaryTitle: 'cancel',
                buttonSecondaryFn: function () {
                    $._closeDialog({});
                },
                fnPreCall: null,
                fnAfterCreatePostCall: null,
                fnDialogClose: null,
                isKeepCurrentDialogId: true,
                isCloseOnEscape: false,
                isAppSpecificDialogButtons: false,
                isSecondModalDialog: false,
                isDisableFirstDialog: false,
                firstDialogId: null

            }, opt);

            //replacing call and id by future deprecated values
            if (settings.dialogId === null && settings.id !== null) {
                settings.dialogId = settings.id;
            }

            if (settings.refreshedFragmentDispatchValue === null && settings.call !== null) {
                settings.refreshedFragmentDispatchValue = settings.call;
            }

            if (settings.refreshedFragmentDispatchValueOverride === null && settings.callOverride !== null) {
                settings.refreshedFragmentDispatchValueOverride = settings.callOverride;
            }



            //checking mandatory fields
            if (settings.dialogId === null || (settings.refreshedFragmentDispatchValue === null && settings.refreshedFragmentDispatchValueOverride === null && settings.refreshedFragmentPageUrl === null)) {
                $._log('ERROR:dialogId OR (refreshedFragmentDispatchValue AND refreshedFragmentDispatchValueOverride AND refreshedFragmentPageUrl) are MANDATORY');
                return;
            }

            if (settings.refreshedFragmentPageUrl === null) { settings.refreshedFragmentPageUrl = undefined; }
            if (settings.refreshedFragmentPageForm === null) { settings.refreshedFragmentPageForm = undefined; }


            var buttons = [];

            if (settings.isShowButtons) {
                if (settings.isOneButtonOnly || settings.buttonPrimaryFn === null) {

                    if (!settings.isShowCloseButton) {
                        buttons = [
                            {
                                text: settings.buttonSecondaryTitle,
                                click: settings.buttonSecondaryFn
                            }
                        ];
                    }

                } else {
                    buttons = [
                        {
                            text: settings.buttonSecondaryTitle,
                            click: settings.buttonSecondaryFn
                        },
                        {
                            text: settings.buttonPrimaryTitle,
                            click: settings.buttonPrimaryFn
                        }
                    ];
                }
            } else {
                settings.isShowCloseButton = true;
                buttons = null;
            }

            $._openDialog({
                dialogId: settings.dialogId,
                isAutoGenerated: true,
                dialogWidth: settings.dialogWidth,
                dialogHeight: settings.dialogHeight,
                dialogTitle: settings.dialogTitle,
                dialogDraggable: settings.dialogDraggable,
                isFragmentRefreshedBeforeOpen: true,
                refreshedFragmentPageUrl: settings.refreshedFragmentPageUrl,
                refreshedFragmentPageForm: settings.refreshedFragmentPageForm,
                refreshedFragmentDispatchValue: settings.refreshedFragmentDispatchValue,
                refreshedFragmentDispatchValueOverride: settings.refreshedFragmentDispatchValueOverride,
                refreshedFragmentAction: settings.refreshedFragmentAction,
                isAjaxBlockerActive: settings.isAjaxBlockerActive,
                isAsyncCall: settings.isAsyncCall,
                dialogButtons: buttons,
                isShowCloseButton: settings.isShowCloseButton,
                fnPreCall: settings.fnPreCall,
                fnAfterCreatePostCall: settings.fnAfterCreatePostCall,
                fnDialogClose: settings.fnDialogClose,
                isKeepCurrentDialogId: settings.isKeepCurrentDialogId,
                isCloseOnEscape: settings.isCloseOnEscape,
                isAppSpecificDialogButtons: settings.isAppSpecificDialogButtons,
                isSecondModalDialog: settings.isSecondModalDialog,
                isDisableFirstDialog: settings.isDisableFirstDialog,
                firstDialogId: settings.firstDialogId,
                paramValue: settings.paramValue
            });

        },


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openDialog
         * @description
         * Facade function jQueryUI open dialog on an element.
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be created, if the isAutogenerated parameter is FALSE, this element must exist on the page before the dialog can be created <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.dialogContainerId {string} the dialog container that will be used when the dialog content will be re-injected into the form using the $._injectDialog() function, this element must exist before the dialog can be injected <br><i>default : opt.dialogId + '_container'</i>
         * @param opt.isAutoGenerated {boolean} if TRUE, the elements dialogId provided and the containerId provided will be created dynamically on the page.
         * @param opt.dialogWidth {number} the dialog width <br><i>default : 800</i>
         * @param opt.dialogHeight {string} the dialog height <br><i>default : 'auto'</i>
         * @param opt.dialogTitle {string} the dialog header title <br><i>default : 'DIALOG'</i>
         * @param opt.dialogDraggable {boolean} if the dialog should be draggable <br><i>default : true</i>
         * @param opt.dialogModal (boolean) if the dialog should be modal <br><i>default : true</i>
         * @param opt.dialogPosition (string) A string representing the position within the viewport. <br><i>Possible values: "center", "left", "right", "top", "bottom".</i>
         * @param opt.dialogResizable (boolean) if the dialog should be resisable <br><i>default : false</i>
         * @param opt.dialogAutoOpen {boolean} if the dialog should be opened right after this call, if FALSE, the dialog will be pre-created and an additional open call will be necessary to display it <br><i>default : true</i>
         * @param opt.dialogButtons {array} the buttons array to be displayed in the dialog footer for actions <br><i>default : null</i>
         * @param opt.dialogButtonsIcons {array} the buttons icons array corresponding to the buttons array defined above <br><i>default : null</i>
         * @param opt.isCloseOnEscape {boolean} if true, the dialog can be closed using ESC keypress <br><i>default : false</i>
         * @param opt.isDialogButtonsMsgBox {boolean} if true, the dialog will react as a msgbox <br><i>default : false</i>
         * @param opt.isDialogButtonsShowIcons {boolean} if true, icons will be shown on dialog actions buttons <br><i>default : true</i>
         * @param opt.isKeepCurrentDialogId {boolean} if true, the dialogId will be stored in $.__currentDialogId var for later be reused in $._closeDialog(), $._centerDialog(), $._emptyDialogContainer() and $._injectDialog() <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if a close button upper right of the dialog must be displayed allowing to close the dialog without having a close footer action button <br><i>default : false</i>
         * @param opt.isDialogOverflowAllowed {boolean} If false, no scrollbars will appear on dialog overflow in height <br><i>default : true</i>
         * @param opt.isBodyOverflowHiddenOnCreate {boolean} Block the scrollbar on parent document body to force only the scrolling on the dialog <br><i>default : true</i>
         * @param opt.isBodyOverflowScrollOnClose {boolean} Same as above but reset on close <br><i>default : true</i>
         * @param opt.isFragmentRefreshedBeforeOpen {boolean} If an ajax call must be done to refresh the fragment container of the dialog before the dialog is effectively displayed and created  <br><i>default : false</i>
         * @param opt.refreshedFragmentDispatchValue {string} the server dispatchValue called <br><i>default : null</i>
         * @param opt.refreshedFragmentAction {string} the server action parameter transferred <br><i>default : null</i>
         * @param opt.isAjaxBlockerActive {boolean} If a blocker please wait message must be shown when the dialog is displayed <br><i>default : true</i>
         * @param opt.fnDialogOpen {function} provided function to be executed when dialog open event is triggered <br><i>default : null</i>
         * @param opt.fnAfterCreatePostCall {function} provided function to be executed when the dialog has been created <br><i>default : null</i>
         *
         * @example
         * //basic call - auto-generated - no action buttons
         * $._openDialog({
         *      dialogId: 'dialog_basic',
         *      isAutoGenerated: true
         * });
         */
        _openDialog: function (opt) {
            $._log('UI_DIALOG.openDialog');

            $.__UI_DIALOG.calculateDimensions();

            //get defaults settings and subclass by options if provided
            var settings = $.extend({

                dialogId: null,
                dialogContainerId: opt.dialogId + '_container',
                dialogContent: null,
                isAutoGenerated: false,
                dialogWidth: 800,
                dialogHeight: 'auto',
                dialogTitle: 'DIALOG',
                dialogDraggable: true,
                dialogModal: true,
                dialogResizable: false,
                dialogPosition: 'center',
                dialogAutoOpen: true,
                dialogButtons: null,
                dialogButtonsIcons: null,
                isCloseOnEscape: false,
                isDialogButtonsMsgbox: false,
                isDialogButtonsShowIcons: true,
                isKeepCurrentDialogId: true,
                isShowCloseButton: null,
                isDialogOverflowAllowed: true,
                isFragmentRefreshedBeforeOpen: false,
                refreshedFragmentPageUrl: null,
                refreshedFragmentPageForm: null,
                refreshedFragmentDispatchValue: null,
                refreshedFragmentDispatchValueOverride: null,
                refreshedFragmentAction: null,
                isAjaxBlockerActive: true,
                isAsyncCall: true,
                isCenterAfterCreate: false,
                fnDialogOpen: null,
                fnDialogClose: null,
                fnPreCall: null,
                fnAfterCreatePostCall: null,
                isEmptyOnClose: true,
                hasHeaderAction: false,
                headerActionClass: null,
                headerActionIconClass: 'icon-download',
                headerActionText: $._getData('jscaf_common_download_file'),
                headerActionUrl: null,
                notificationText: null,
                isAppSpecificDialogButtons: false,
                isSecondModalDialog: false,
                isDisableFirstDialog: false,
                firstDialogId: null,
                paramValue: null

            }, opt);

            var $dialogContainer = $('#' + settings.dialogContainerId);

            //checking parameters
            if (settings.dialogId === null) {
                $._log('====>ERROR : dialogId must be provided');
                return;
            }

            if (settings.isFragmentRefreshedBeforeOpen && (settings.refreshedFragmentDispatchValue === null && settings.refreshedFragmentDispatchValueOverride === null && settings.refreshedFragmentPageUrl === null)) {
                $._log('====>ERROR : if isFragmentRefreshedBeforeOpen is TRUE, refreshFragmentDispatchValue OR refreshedFragmentPageUrl cannot be NULL');
                return;
            }

            if (settings.refreshedFragmentPageUrl === null) { settings.refreshedFragmentPageUrl = undefined; }
            if (settings.refreshedFragmentPageForm === null) { settings.refreshedFragmentPageForm = undefined; }

            if (settings.isSecondModalDialog && settings.isDisableFirstDialog && settings.firstDialogId === null) {
                $._log('====>ERROR : if isSecondModalDialog is TRUE and isDisableFirstDialog is TRUE then a firstDialogId cannont be NULL');
            }



            //checking and emptying container if exists
            if ($dialogContainer.length) {
                $dialogContainer.empty();
            }

            //checking if the dialogId exists and create it if not
            if (settings.dialogId !== null && settings.dialogContent === null) {
                if (!$('#' + settings.dialogId).length && settings.isAutoGenerated) {
                    $._$body.append('<div id="' + settings.dialogId + '" class="hidden"></div>');
                }
            } else if (settings.dialogId !== null && settings.dialogContent !== null) {
                $._$body.append(settings.dialogContent);
            }

            //checking if the dialog container exists and create if it must be generated dynamically
            if (!$dialogContainer.length && settings.isAutoGenerated) {
                $._$form.append('<div id="' + settings.dialogContainerId + '" class="hidden"></div>');
            }

            //storing autogenerated value for close function
            $._isDialogAutoGenerated = settings.isAutoGenerated;


            //HD resolution transforming dialog auto width
            if (settings.dialogWidth > $.__UI_DIALOG.mainWidth) {
                if ($.__modules.DISPLAY) {
                    if ($.__DISPLAY.hdMarginWidth !== null) {
                        settings.dialogWidth -= 2 * $.__DISPLAY.hdMarginWidth;
                    }
                } else {
                    settings.dialogWidth -= 150;
                }
            }

            //Setup transition and disable for IE
            var show = {effect:'fade', duration: 200};
            if ($._isIE7() || $._isIE8()) {
                show = null;
            }

            //setup dialog creation function
            var fn = function (postCall) {

                //creating and opening the dialog
                $('#' + settings.dialogId).dialog({
                    autoOpen: settings.dialogAutoOpen,
                    closeOnEscape: settings.isCloseOnEscape,
                    resizable: settings.dialogResizable,
                    draggable: settings.dialogDraggable,
                    width: settings.dialogWidth,
                    height: settings.dialogHeight,
                    modal: settings.dialogModal,
                    title: settings.dialogTitle,
                    show: null,
                    create: function () {
                        var $this = $(this);

                        $._logStart('DIALOG.create : ' + this.id);

                        if (!settings.isDialogOverflowAllowed) {
                            this.style.overflow = 'hidden';
                        }
                        //removing the hidden class of the dialogId
                        $._removeClass(this,'hidden');

                        //fixing the dialog so it won't be affected by user scroll on window
                        $._addClass($this.parent()[0],'pos-fixed');

                        $this = null;

                        $._logEnd();
                    },
                    open: function () {
                        var $this = $(this);

                        $._logStart('DIALOG.open');

                        //if buttons array is empty then show close button by default
                        if (settings.dialogButtons === null && settings.isShowCloseButton === null) {
                            settings.isShowCloseButton = true;
                        }

                        if (settings.dialogButtons !== null && settings.isShowCloseButton === null) {
                            settings.isShowCloseButton = false;
                        }

                        //hide close button
                        if (!settings.isShowCloseButton) {
                            $this.parent().children().find('.ui-dialog-titlebar-close').hide();
                        }

                        //put buttons collection in cache
                        var buttons = $($this.parent().find('.ui-dialog-buttonset').children('button'));

                        //focus on the last button (to prevent activate with double-click)
                        if (buttons.length > 1) {
                            $(buttons[buttons.length - 1]).focus();
                        }

                        //updating button priority display classes

                        //if more than 1 button, the last button is always set as a primary button
                        if (!settings.isDialogButtonsMsgbox) {
                            if (buttons.length > 1) {
                                $(buttons[buttons.length - 1]).addClass('ui-primary-button');
                                $(buttons[0]).addClass('ui-secondary-button');

                                //if only one button is present, then this button is set as a primary button
                            } else if (buttons.length === 1) {
                                $(buttons[0]).addClass('ui-primary-button');
                            }
                            //special display for dialog msgbox dialog
                        } else {
                            //if yes, no, cancel buttons, cancel is set as a secondary action, yes and no as primary action
                            if (buttons.length === 3) {
                                $(buttons).addClass('ui-primary-button');
                                $(buttons[2]).addClass('ui-secondary-button');
                            } else {
                                //if one or two buttons (yes/no), all are set as primary action
                                $(buttons).addClass('ui-primary-button');
                            }
                        }


                        //when bootstrap is active, no icon are allowed on the dialog buttons to keep the flat theme straight
                        if ($._settings.isBootstrapActive) {
                            settings.dialogButtonsIcons = null;
                        }


                        //setting buttons icons if provided
                        if (settings.dialogButtonsIcons !== null) {
                            var i;
                            for (i = 0; i < settings.dialogButtonsIcons.length; i++) {
                                if (settings.dialogButtonsIcons[i] !== undefined) {
                                    $(buttons[i])
                                        .removeClass('ui-button-text-only')
                                        .addClass('ui-button-text-icon-primary ui-button-text-icon')
                                        .prepend('<span class="ui-button-icon-primary ui-icon ' + settings.dialogButtonsIcons[i] + '"></span>');
                                }
                            }

                            //if not provided explicetly, then the last button receive by default an ui-icon-check icon
                        } else {
                            if (!$._settings.isBootstrapActive) { // when bootstrap is active, no icon is set on the dialog buttons
                                if (settings.isDialogButtonsShowIcons) {
                                    if (buttons.length > 1) {
                                        $(buttons[buttons.length - 1])
                                            .removeClass('ui-button-text-only')
                                            .addClass('ui-button-text-icon-primary ui-button-text-icon')
                                            .prepend('<span class="ui-button-icon-primary ui-icon ui-icon-check"></span>');
                                    }
                                }
                            }
                        }

                        //checking the height and constrain it if more than visible window height
                        if ($this.height() > $.__UI_DIALOG.windowHeight - 150) {
                            $this.height($.__UI_DIALOG.windowHeight - 150);
                        }

                        //checking if a action button should be displayed on the header
                        if (settings.hasHeaderAction) {
                            $('.ui-dialog-titlebar').append('<span class="ui-dialog-titlebar-header-action ' + settings.headerActionClass + '"><a href="' + settings.headerActionUrl + '" target="_blank"><span class="icon-inline-text ' + settings.headerActionIconClass + '"></span>' + settings.headerActionText + '</a></span>');
                        }

                        //checking if open function provided
                        if (settings.fnDialogOpen !== null) {
                            $._execFn(settings.fnDialogOpen);
                        }

                        //checking if a notification must be displayed
                        if (settings.notificationText !== null) {
                            jQuery.noticeAdd({type: 'warning', text: settings.notificationText});
                        }

                        //force centering the dialog
                        $this.dialog("option", "position", settings.dialogPosition);

                        //force z-index in case of multiple dialogs opening
                        if (settings.isSecondModalDialog) {
                            $('#' + settings.dialogId).parent().css({'z-index':'1000'});

                            //check if the first dialog should be disabled
                            if (settings.isDisableFirstDialog) {
                               var $firstDialog = $('#'+settings.firstDialogId);
                               $._elementBlock($firstDialog.parent(),false);
                               $firstDialog.parent().children().find('.ui-dialog-titlebar-close').hide();
                            }

                        }

                        $._logEnd();

                    },
                    close: function () {
                        $._logStart('DIALOG.close : ' + this.id);

                        //checking if open function provided
                        if (settings.fnDialogClose !== null) {
                            $._execFn(settings.fnDialogClose);
                        } else {
                            if (settings.dialogButtons === null && !settings.isAppSpecificDialogButtons) {
                                $._closeDialog({dialogId: settings.dialogId, isEmptyOnClose: settings.isEmptyOnClose});
                            }
                        }

                        if (settings.notificationText !== null) {
                            jQuery.noticeRemoveAll();
                        }

                        //if it's a second dialog and the first dialog has been disabled reactivate it
                        if (settings.isSecondModalDialog) {

                            //check if the first dialog should be disabled
                            if (settings.isDisableFirstDialog) {
                               var $firstDialog = $('#'+settings.firstDialogId);
                               $._elementUnblock($firstDialog.parent());

                               if ($.__isFirstDialogShowCloseButton) {
                                   $firstDialog.parent().children().find('.ui-dialog-titlebar-close').show();
                               }
                            }

                        }

                        $._logEnd();

                        return false;
                    },
                    buttons: settings.dialogButtons
                });


                if (settings.isCenterAfterCreate) {
                    $._execFn(function () {
                        $('#' + settings.dialogId).dialog("option", "position", 'center');
                    });
                }

                $._execFn(settings.fnAfterCreatePostCall, true);

                $._execFn(postCall, true);

            };


            if (settings.isFragmentRefreshedBeforeOpen) {

                if ($.__modules.AJAX) {
                    $.__AJAX.ajax({
                        pageUrl: settings.refreshedFragmentPageUrl,
                        pageForm: settings.refreshedFragmentPageForm,
                        id: settings.dialogId,
                        call: settings.refreshedFragmentDispatchValue,
                        callOverride: settings.refreshedFragmentDispatchValueOverride,
                        paramValue: settings.paramValue,
                        action: settings.refreshedFragmentAction,
                        isBlockerActive: settings.isAjaxBlockerActive,
                        fnPreCall: settings.fnPreCall,
                        fnPostCall: function () {
                            $._execFn(fn);
                        }
                    });
                } else {
                    $._log('ERROR: AJAX module not loaded, unable to perform ajax call before opening the dialog' );
                }

            } else {
                $._execFn(fn);
            }

            //storing current opened dialog for further reuse (in close / center / empty functions)
            if (settings.isKeepCurrentDialogId && !settings.isSecondModalDialog) {
                $.__currentDialogId = settings.dialogId;
            }

            //storing the first dialog close button state
            if (!settings.isSecondModalDialog) {
                $.__isFirstDialogShowCloseButton = settings.isShowCloseButton;
            }
        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _closeDialog
         * @description
         * Close a previously opened dialog
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be closed, if not provided the $.__currentDialogId will be taken if exists <br><i>default : null</i>
         * @param opt.dialogContainerId {string} the dialog container that will be emptied (if the dialog has not been auto-generated) OR removed (if the dialog has been previously auto-generated <br><i>default : null</i>
         * @param opt.isBodyOverflowScrollOnClose {boolean} Reset scrolling of parent body element on close <br><i>default : true</i>
         *
         */
        _closeDialog: function (opt, fn) {
            $._log('UI_DIALOG.closeDialog');

            //get defaults settings and subclass by     options if provided
            var settings = $.extend({

                dialogId: null,
                dialogContainerId: null,
                isBodyOverflowScrollOnClose: true,
                isEmptyOnClose: true

            }, opt);

            //checking parameters
            if (settings.dialogId === null && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogId cannot be NULL');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (settings.dialogId === null && $.__currentDialogId !== null) {
                settings.dialogId = $.__currentDialogId;
                settings.dialogContainerId = $.__currentDialogId + '_container';
            }

            var $dialogContainer = $('#' + settings.dialogContainerId),
                $dialog = $('#' + settings.dialogId);

            //checking and emptying container if exists
            if ($dialogContainer.length && $._isDialogAutoGenerated) {
                $dialogContainer.remove();
            } else {
                if (settings.isEmptyOnClose) {
                    $dialogContainer.empty();
                }
            }

            //closing and destroying dialog DOM created elements
            $dialog.dialog("close").dialog("destroy");

            //checking and emptying the dialog content
            if ($dialog.length && $._isDialogAutoGenerated) {
                $dialog.remove();
            } else {
                if (settings.isEmptyOnClose) {
                    $dialog.empty();
                }
            }

            //destroying all poshytip that remains when validation occurs
            $('.JS_live-validation').poshytip('destroy');

            //just to be sure, back to the default value to avoid further highlighted validation error  if changed inside the previous process
            $._settings.isFieldValidationUpdateDisplay = false;

            //resetting the current dialog opened if it's the one kept (for multiple stacked dialogs)
            if (settings.dialogId === $.__currentDialogId) {
                $.__currentDialogId = null;
            }

            $._execFn(fn);

        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _centerDialog
         * @description
         * Center a previously opened dialog
         * @param dialogId {string} the id of the dialog that will be centered, if not provided the $.__currentDialogId will be taken if exists <br><i>default : undefined</i>
         *
         */
        _centerDialog: function (dialogId) {

            $.__UI_DIALOG.calculateDimensions();

            //checking parameters
            if (dialogId === undefined && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogId must be provided');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (dialogId === undefined && $.__currentDialogId !== null) {
                dialogId = $.__currentDialogId;
            }

            var $dialog = $('#' + dialogId);

            //checking if dialog height is greater than window height-150px, if yes contrain it
            if ($dialog.height() > $.__UI_DIALOG.windowHeight - 150) {
                $dialog.height($.__UI_DIALOG.windowHeight - 150);
            }

            $dialog.dialog("option", "position", 'center');

        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _emptyDialogContainer
         * @description
         * Empty a previously injected dialog container
         * @param dialogContainerId {string} the id of the container used for the injection, if not provided the $.__currentDialogId + '_container' will be taken if exists <br><i>default : undefined</i>
         *
         */
        _emptyDialogContainer: function (dialogContainerId) {

            //checking parameters
            if (dialogContainerId === undefined && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogContainerId must be provided');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (dialogContainerId === undefined && $.__currentDialogId !== null) {
                dialogContainerId = $.__currentDialogId + '_container';
            }

            $('#' + dialogContainerId).empty();
        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _injectDialog
         * @description
         * Inject the dialog into its form container (default) or provided container
         * @param dialogContentId {string} the id of the container of the dialog that will be injected into the form container <br><em>MANDATORY</em> <br><i>default : undefined</i>
         * @param dialogContainerId {string} the id of the container within the form where the dialog content will be injected, the $.__currentDialogId+'_container' will be taken in place if exists and no dialogContainerId is provided <br><i>default : undefined</i>
         * @param cloneDialog {boolean} if true, the dialog will be cloned into the container not moved (by default)  <br><i>default : undefined</i>
         *
         */
        _injectDialog: function (dialogContentId, dialogContainerId, cloneDialog) {
            //checking parameters
            if ((dialogContentId === undefined || dialogContentId === null) && $.__currentDialogId === null) {
                $._log('====>ERROR : a dialog content id must be provided for the injection');
                return;
            }
            if ((dialogContainerId === undefined || dialogContainerId === null) && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogContainerId must be provided');
                return;
            }

            var $dialogContent;

            //in case no dialogContentId is provided, the content is always the closest child of the dialog itself
            if ($.__currentDialogId !== null && (dialogContentId === undefined || dialogContentId === null)) {
                $dialogContent = $('#' + $.__currentDialogId).children().first();
            } else {
                $dialogContent = $('#' + dialogContentId);
            }

            //if a current dialog id has already been stored in the openDialog function, use it for inject the dialog content into this auto-generated container
            if ($.__currentDialogId !== null && (dialogContainerId === undefined || dialogContainerId === null)) {
                dialogContainerId = $.__currentDialogId + '_container';
            }


            if (cloneDialog === undefined || !cloneDialog) {
                //by default the dialog form is moved to the container
                $dialogContent.appendTo('#' + dialogContainerId);
            } else {
                $dialogContent.clone().appendTo('#' + dialogContainerId);
            }
        },


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _injectDialogClone
         * @description todo
         * @example todo
         */
        _injectDialogClone: function(dialogContentId, dialogContainerId) {

            $._injectDialog(dialogContentId, dialogContainerId, true);

        },




        _openUploadDialog: function( opt ) {

            var url = $._getContextPath() + '/' + opt.url + '.do';
            var submitUrl = opt.url;

            var settings = $.extend({

                dialogId:'upload_document_dialogContent',
                iFrameObject: true,
                dialogWidth: 550,
                dialogHeight: 250,
                dialogTitle: 'UPLOAD FILE DIALOG',
                isDialogOverflowAllowed: false,
                documentsArray: [{documentUrl:url}],
                iFrameHeight: 200,
                iFrameScrolling: 'no',
                dialogButtons: [
                    {
                        text:'cancel',
                        click:function () {
                            $._closeDialog({dialogId:'upload_document_dialogContent'})
                        }
                    } ,
                    {
                        text:'UPLOAD',
                        click:function () {
                            $._SF({
                                call: submitUrl,
                                formId: opt.submitFormId,
                                isFormInIframe: true,
                                isIframeInDialog: true,
                                fnPostCall:function() {
                                    if ($('iframe').contents().find('#uploadError').length) {
                                        $._elementUnblock($('.ui-dialog'));
                                    }
                                }
                            });
                        }
                    }
                ]

            }, opt);


            $._openIframeDialog( settings );


        },


        _closeUploadDialog: function() {

            $._closeDialog({dialogId:'upload_document_dialogContent'});
            $._elementUnblock($('#dialog').parent());

        },




        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openIframeDialog
         * @description
         * Open a dialog containing a iFrame url location to another page / multi-url locations
         * @param opt {object} the object literal parameters <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the generated dialog <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.dialogTitle {string} the title of the dialog <br><i>default : ''</i>
         * @param opt.dialogWidth {number} the width of the dialog, if not provided, window width -100px will be taken instead <br><i>default : null</i>
         * @param opt.dialogHeight {number} the height of the dialog, if not provided, window height -100px will be taken instead <br><i>default : null</i>
         * @param opt.dialogButtons {array} the buttons array that will be included in the dialog footers actions <br><i>default : null</i>
         * @param opt.isDialogOverflowAllowed {boolean} if the dialog can have a size greater than the viewport, then scrolling is allowed <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if the close top right button must be visible, if true this invalidate the button actions array <br><i>default : null</i>
         * @param opt.fnDialogOpen {function} the function that will be called on dialog open event <br><i>default : null</i>
         * @param opt.fnDialogClose {function} the function that will be called on dialog close event <br><i>default : null</i>
         * @param opt.documentsArray {array} the array of document object (documentId-documentFileName-documentDescription-documentUrl), can be a single entry, that'll be displayed in the dialog <br><em>ANDATORY</em> <br><i>default : []</i>
         * @param opt.selectedDocumentId {number} the selected documentId which will be visually selected if more than one documents are provided inside the array, if not provided, the first element in the array will be selected by default. <br><i>default : null</i>
         * @param opt.iFrameHeight {number} the height of the iframe inside the dialog must be specified for IE (not allowing 100%). <br><i>default : null</i>
         * @param opt.iFrameScrolling {string} specify if the iFrame must be scrollable, must be specified for IE. <br><i>default : 'yes'</i>
         * @param opt.iFrameObject (boolean) specity if the iFrame tag should be used instead of object tag. <br><i>default : false</i>
         *
         */
        _openIframeDialog: function (opt) {

            $.__UI_DIALOG.calculateDimensions();

            var settings = $.extend({

                dialogId: null,
                dialogTitle: '',
                iFrameObject: false,
                dialogWidth: null,
                dialogHeight: null,
                dialogButtons: null,
                isFullWidthOnHD: false,
                isDialogOverflowAllowed: true,
                isShowCloseButton: null,
                fnDialogOpen: null,
                fnDialogClose: null,
                documentsArray: [],
                selectedDocumentId: null,
                iFrameHeight: null,
                iFrameScrolling: 'yes',
                hasHeaderAction: false,
                headerActionClass: null,
                headerActionIconClass: 'icon-download',
                headerActionText: $._getData('jscaf_common_download_file'),
                headerActionUrl: null,
                notificationText: null,
                isPdfContent: false

            }, opt);

            var dialogContent, i, dialogHeight, dialogWidth, iFrameHeight, documentType, selectedArrayIdx = 0;


            //$._blockUI();


            //calculating dialog height for dimensioning manually the iframe (problem on IE when at 100%)
            if (settings.dialogHeight !== null) {
                dialogHeight = settings.dialogHeight;
            } else {
                dialogHeight = $.__UI_DIALOG.windowHeight - 50;
            }

            //calculating dialog width
            if (settings.dialogWidth === null) {
                if (!$.__modules.DISPLAY) {
                    dialogWidth = $.__UI_DIALOG.windowWidth - 100;
                } else {
                    if ($.__DISPLAY.hdMarginWidth === null) {
                        dialogWidth = $.__UI_DIALOG.windowWidth - 100;
                    } else {
                        if (settings.isFullWidthOnHD) {
                            dialogWidth = $.__UI_DIALOG.windowWidth;
                        } else {
                            dialogWidth = $.__UI_DIALOG.windowWidth - $.__DISPLAY.hdMarginWidth;
                        }
                    }
                }
            } else {
                dialogWidth = settings.dialogWidth;
            }

            //setting the iFrame height
            if (settings.iFrameHeight === null) {
                if (settings.documentsArray.length === 1) {
                    iFrameHeight = '99%';
                } else {
                    iFrameHeight = (dialogHeight - 50) + 'px';
                }

            } else {
                iFrameHeight = settings.iFrameHeight + 'px';
            }


            //constructing the dialog content
            dialogContent = '<div id="' + settings.dialogId + '" class="hidden">';

            //checking mandatory parameters
            if (settings.documentsArray.length === 0) {
                $._log('ERROR:provided documentsArray cannot be empty');
                return;
            }

            if (settings.documentsArray.length === 1) {
                if (settings.iFrameObject === true) {
                    //IMPORTANT when loading an iFrame, always first set the SRC as about:blank, otherwise this will fail in IE9,
                    //the effective SRC (the documentUrl) is set on fnAfterCreatePostCall of the openDialog function call
                    dialogContent += '<iframe style="width:100%;height:' + iFrameHeight + ';" src="about:blank" frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"></iframe>';
                } else {
                    //IMPORTANT IE8 requires the param with the SRC enclosed inside the object tag !!
                    if ($._isIE8()) {
                        documentType = '';
                        if (settings.isPdfContent) {
                            documentType = 'type="application/pdf"';
                        }
                        dialogContent += '<object style="width:100%;height:' + iFrameHeight + ';" data="' + settings.documentsArray[0].documentUrl + '" ' + documentType + ' frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"><param name="src" value="'+settings.documentsArray[0].documentUrl+'"></object>';
                    } else {
                        dialogContent += '<object style="width:100%;height:' + iFrameHeight + ';" data="' + settings.documentsArray[0].documentUrl + '" frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"></object>';
                    }
                }
            } else {
                dialogContent += '<div class="columns">';
                dialogContent += '  <div class="col-20" style="margin-right: 3px;">';
                dialogContent += '  <ul class="list-counter-small light with-arrow">';

                for (i = 0; i < settings.documentsArray.length; i++) {
                    if ((settings.documentsArray[i].documentId === settings.selectedDocumentId && settings.selectedDocumentId !== null) || (i === 0 && settings.selectedDocumentId === null)) {
                        dialogContent += '    <li id="' + i + '" class="iframe_selector parent selected">';
                        selectedArrayIdx = i;
                    } else {
                        dialogContent += '    <li id="' + i + '" class="iframe_selector parent">';
                    }
                    dialogContent += '        <div class="title" style="font-size:11px">' + settings.documentsArray[i].documentFileName + '</div>';
                    dialogContent += '    </li>';
                }

                dialogContent += '  </ul>';
                dialogContent += '  </div>';

                dialogContent += '<div class="col-80" id="pdf-preview-holder">';
                dialogContent += '<object id="pdf-preview" data="'+settings.documentsArray[selectedArrayIdx].documentUrl+'" type="application/pdf" width="100%" height="'+iFrameHeight+'" standby="Loading pdf..."><param name="src" value="'+settings.documentsArray[selectedArrayIdx].documentUrl+'"></object>';
                dialogContent += '</div>';

                dialogContent += '  <div class="cl"></div>';
                dialogContent += '</div>';
            }

            dialogContent += '</div>';


            //Creating the dialog
            $._openDialog({
                dialogId: settings.dialogId,
                dialogContent: dialogContent,
                isAutoGenerated: true,
                resizable: false,
                draggable: false,
                dialogHeight: dialogHeight,
                dialogWidth: dialogWidth,
                dialogTitle: settings.dialogTitle,
                dialogButtons: settings.dialogButtons,
                isDialogOverflowAllowed: settings.isDialogOverflowAllowed,
                isShowCloseButton: settings.isShowCloseButton,
                fnDialogOpen: function () {
                    $._log('openIframeDialog:fnDialogOpen');
                    if (settings.documentsArray.length !== 1) {
                        //bind the iframe selector event
                        $._$document.on('click', '.iframe_selector', function () {
                                $('#pdf-preview-holder').find('object').remove();
                                $('<object id="pdf-preview" data="'+settings.documentsArray[this.id].documentUrl+'" type="application/pdf" width="100%" height="'+iFrameHeight+'" standby="Loading pdf..." ><param name="src" value="'+settings.documentsArray[this.id].documentUrl+'"></param></object>').appendTo('#pdf-preview-holder');

                        });
                    }
                    $('object').contents().find('body').css({'overflow': 'hidden'});
                    $('object').contents().find('html').css({'overflow': 'hidden'});
                    $._execFn(settings.fnDialogOpen);
                },
                fnDialogClose: function () {
                    $._log('openIframeDialog:fnDialogClose');
                    if (settings.documentsArray.length !== 1) {
                        //unbind the iframe selector event
                        $._$document.off('click', '.iframe_selector');
                    }
                    $._execFn(settings.fnDialogClose);
                },
                fnAfterCreatePostCall: function () {
                    $._log('openIframeDialog:fnAfterCreatePostCall');

                    $._initDisplay();
                    $._unblockUI();
                    $._enableButtons();

                    //Important as if the src is set when declared, this break in IE9
                    if (!$._isIE8()) {
                       if (settings.documentsArray.length===1 && settings.iFrameObject) {
                           $('iframe').attr('src',settings.documentsArray[0].documentUrl);
                       }
                    }
                },
                hasHeaderAction: settings.hasHeaderAction,
                headerActionClass: settings.headerActionClass,
                headerActionIconClass: settings.headerActionIconClass,
                headerActionText: settings.headerActionText,
                headerActionUrl: settings.headerActionUrl,
                notificationText: settings.notificationText,
                isKeepCurrentDialogId: false
            });


        },


        _openDocumentDialog: function(opt) {

                var settings = $.extend({

                    documentArray: [], //to do implement multi content types documents display (image+pdf in the same viewer)
                    documentId: null,
                    documentDescription: null,
                    documentFilename: null,
                    documentUrl: null,
                    documentContentType: null

                }, opt);

                var documentsArray = [];

                if (settings.documentContentType === 'application/pdf') {

                    documentsArray.push({
                            documentId: settings.documentId,
                            documentFileName: settings.documentFilename,
                            documentDescription: settings.documentDescription,
                            documentUrl: $._getContextPath() + settings.documentUrl
                    });

                    $._openIframeDialog({
                        dialogId:'pdfDialog',
                        dialogTitle:'PDF - ' + settings.documentFilename,
                        documentsArray:documentsArray,
                        selectedDocumentId:null,
                        isPdfContent:true
                    });

                } else if (settings.documentContentType.indexOf('image') === 0) {


                      var $dialog = $('<div id="imageDialog"><img id="image" src=""/></div>');
                      $._$body.append($dialog);

                      var $image = $('#image');

                      $image.attr('src', $._getContextPath() + settings.documentUrl);

                      $image.load(function(){

                        $dialog.dialog({
                          modal: true,
                          title: 'IMAGE - ' + settings.documentFilename,
                          resizable: false,
                          draggable: false,
                          width: 'auto',
                          close: function(){
                             $(this).dialog("destroy");
                             $dialog.remove();
                          }
                        });
                      });

                }



        },






        /* ======================================================= */
        /* ===              MESSAGE BOXES                       == */
        /* ======================================================= */




        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _msgbox
         * @description
         * Open a jqueryUI dialog as an message box / alert box with different possible styles/behaviours
         * @param opt {object} the object literal parameter <br><em>MANDATORY</em>
         * @param opt.text {string} the message text of the alert box <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.title {string} either the title of the dialog (for prompt and email) or a sub-title displayed on top of the text message for other types <br><i>default : null</i>
         * @param opt.msgboxType {string} the content type of alert box to be displayed : 'ok' - 'yes_no' - 'yes_no_cancel' - 'prompt' - 'email' <br><i>default : 'ok'</i>
         * @param opt.alertType {string} the display type of alert box to be displayed : 'warning' - 'info' - 'error' - 'question' - 'confirm' <br><i>default : 'warning'</i>
         * @param opt.promptFieldLabel {string} in case of a alertType='prompt', this label will be displayed for the input field <br><i>default : null</i>
         * @param opt.isPromptFieldRequired {boolean} in case of alerType='prompt', if TRUE, the input field will be validated before success leaving the msgbox <br><i>default : false</i>
         * @param opt.fnCallback {function} the callback function that'll executed upon user action, if provided : when YES=>sends TRUE, when NO=>sends FALSE, when CANCEL=>sends null, when alertType='prompt'=>sends the input field result <br><i>default : null</i>
         *
         */
        _msgbox: function (opt) {

            //get defaults settings and subclass by options if provided

            var settings = $.extend({

                dialogId: 'msgbox_dialog',
                text: null,
                title: null,
                subTitle: null,
                msgboxType: 'ok',
                alertType: 'warning',
                promptFieldLabel: null,
                isPromptFieldRequired: false,
                fnCallback: null,
                isEnterKeyOnPrimaryActionEnabled: false

            }, opt);


            var dialogContent = '';

            dialogContent += '<div id="' + settings.dialogId + '" class="hidden">';
            dialogContent += '<div class="msgbox-' + settings.alertType + '"></div>';

            if (settings.msgboxType === 'prompt' || settings.msgboxType === 'email') {

                if (settings.msgboxType === 'email') {
                    dialogContent += '<div class="field required">';
                    dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + $._getData('jscaf_email_subject') + '</h6></div>';
                    dialogContent += '<input type="text" id="msgbox_input_subject" class="field-value" style="width:350px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';

                    dialogContent += '<div class="field required">';
                    dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + $._getData('jscaf_email_content') + '</h6></div>';
                    dialogContent += '<textarea id="msgbox_input_content" class="field-value" rows="6" style="width:350px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';

                } else {
                    if (!settings.isPromptFieldRequired) {
                        dialogContent += '<div class="field">';
                        dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;">' + settings.promptFieldLabel + '</h6></div>';
                    } else {
                        dialogContent += '<div class="field required">';
                        dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + settings.promptFieldLabel + '</h6></span></div>';
                    }
                    dialogContent += '<textarea id="msgbox_input" class="field-value" rows="3" style="width:250px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';
                }
            } else {
                dialogContent += '<div class="fl" style="width:260px">';
                if (settings.subTitle === null) {
                    dialogContent += '<h4 class="msgbox-text-' + settings.alertType + '">' + settings.text + '</h4>';
                } else {
                    dialogContent += '<h4 class="msgbox-text-' + settings.alertType + '">' + settings.subTitle + '</h4>';
                    dialogContent += '<p>' + settings.text + '</p>';
                }
                dialogContent += '</div>';
            }

            dialogContent += '</div>';


            //setting the msgbox title according to the alert type
            if (settings.title === null) {
                settings.title = $._getData('jscaf_common_msgbox_' + settings.alertType);
            }


            //setting the buttons according to the msgbox type
            var buttons = [];

            var fnAfterCreatePostCall = null;

            if (settings.msgboxType === 'ok') {   //default settings
                buttons = [
                    {
                        text: 'OK',
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    }
                ];
            }
            if (settings.msgboxType === 'yes_no') {
                buttons = [
                    {
                        text: $._getData('jscaf_common_YES'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    },
                    {
                        text: $._getData('jscaf_common_NO'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(false);
                            }
                        },
                        tabindex:2

                    }
                ];

                settings.isDialogButtonsMsgbox = true;  //disable show primary and secondary actions as default buttons dialog

            }
            if (settings.msgboxType === 'yes_no_cancel') {
                buttons = [
                    {
                        text: $._getData('jscaf_common_YES'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    },
                    {
                        text: $._getData('jscaf_common_NO'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(false);
                            }
                        },
                        tabindex:2
                    },
                    {
                        text: $._getData('jscaf_common_cancel'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(null);
                            }
                        },
                        tabindex:3
                    }
                ];

                settings.isDialogButtonsMsgbox = true;  //disable show primary and secondary actions as default buttons dialog
            }
            if (settings.msgboxType === 'prompt' || settings.msgboxType === 'email') {

                var primaryActionText = 'OK';

                if (settings.msgboxType === 'email') {
                    primaryActionText = $._getData('jscaf_email_send');
                }

                buttons = [
                    {
                        text: 'cancel',
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(null);
                            }
                        },
                        tabindex:1
                    },
                    {
                        text: primaryActionText,
                        click: function () {

                            var $dialog = $('#' + settings.dialogId);

                            if (settings.msgboxType === 'prompt') {

                                var $msgBoxInput = $('#msgbox_input');
                                var inputValue = $msgBoxInput.val();

                                if (settings.isPromptFieldRequired) {
                                    if ($.trim(inputValue) === '') {
                                        jQuery.noticeAdd({text: 'Field is required', type: 'error'});
                                        $msgBoxInput.focus();
                                    } else {
                                        $('#'+settings.dialogId).dialog("close");
                                        if (settings.fnCallback !== null) {
                                            settings.fnCallback(inputValue);
                                        }
                                    }
                                } else {
                                    $('#'+settings.dialogId).dialog("close");
                                    if (settings.fnCallback !== null) {
                                        settings.fnCallback(inputValue);
                                    }
                                }

                            } else {

                                var $msgBoxSubject = $('#msgbox_input_subject');
                                var $msgBoxContent = $('#msgbox_input_content');

                                if ($.trim($msgBoxSubject.val()) === '' || $.trim($msgBoxContent.val()) === '') {
                                    jQuery.noticeAdd({text: 'Please fill all the mandatory fields', type: 'error'});
                                    if ($.trim($msgBoxSubject.val()) === '') {
                                        $msgBoxSubject.focus();
                                    } else {
                                        $msgBoxContent.focus();
                                    }
                                } else {
                                    $('#'+settings.dialogId).dialog("close");
                                    if (settings.fnCallback !== null) {
                                        settings.fnCallback({subject: $msgBoxSubject.val(), content: $msgBoxContent.val()});
                                    }
                                }


                            }

                        },
                        tabindex:2,
                        id:'ui_dialog_primary_action'
                    }
                ];

                fnAfterCreatePostCall = function () {
                    if (settings.msgboxType === 'prompt') {
                        $('#msgbox_input').focus();
                    } else {
                        $('#msgbox_input_subject').focus();
                    }
                };
            }


            $._$body.append(dialogContent);

            var dialogWidth = 400;
            if (settings.msgboxType === 'email') {
                dialogWidth = 500;
            }


            $._openDialog({
                dialogId: settings.dialogId,
                dialogTitle: settings.title,
                dialogWidth: dialogWidth,
                isDialogOverflowAllowed: false,
                isBodyOverflowHiddenOnCreate: false,
                fnDialogOpen: function () {
                    var $dialog = $('#' + settings.dialogId);
                    $dialog.closest('.ui-dialog').addClass(settings.alertType);
                    $dialog.parent().find('.ui-dialog-buttonpane').addClass(settings.alertType);
                    $dialog.parent().find('.ui-dialog-title').addClass(settings.alertType);
                },
                fnDialogClose: function() {
                    jQuery.noticeRemoveAll();
                    $._closeDialog({dialogId: settings.dialogId});
                    $('#' + settings.dialogId).remove();
                },
                dialogButtons: buttons,
                isDialogButtonsMsgbox: settings.isDialogButtonsMsgbox,
                isDialogButtonsShowIcons: false,
                fnAfterCreatePostCall: fnAfterCreatePostCall,
                isKeepCurrentDialogId: false,
                isCloseOnEscape: true
            });


            //enabling keyboard action event on primary action button
            if (settings.isEnterKeyOnPrimaryActionEnabled) {
                $(document).one('keydown', function(e) {
                    var $this = $(this);
                    if (e.keyCode === $.ui.keyCode.ENTER) {
                        $('#ui_dialog_primary_action').trigger('click');
                        e.stopPropagation();
                    }
                });
            }




        }


    });




}(jQuery));











/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF VALIDATION MODULE                == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //Declaration
        __VALIDATION: {

            initValidationEvents: function() {

                //LIVE VALIDATION BOUND EVENTS
                //----------------------------

                //trigger on BLUR on INPUT fields
                $._$document
                    .on('blur', '.JS_live-validation',
                    function () {
                        var $this = $(this);
                        if ($this.get(0).nodeName !== 'SELECT') {
                            $.__liveValidation($this);
                        }
                    }
                );

                //trigger on CHANGE on SELECT fields
                $._$document
                    .on('change', '.JS_live-validation',
                    function () {
                        var $this = $(this);
                        if ($this.get(0).nodeName === 'SELECT' || $this.hasClass('JS_datetimepicker') || $this.hasClass('JS_datepicker')) {
                            $.__liveValidation($this);
                        }
                    }
                );

            },


            validateDatetimepicker: function(o) {

                $._log('VALIDATION.validateDateTimepicker');

                var $d = $('#' + o.attr('id') + '_d');
                var $h = $('#' + o.attr('id') + '_h');
                var $m = $('#' + o.attr('id') + '_m');

                if (o.hasClass('JS_dateonly')) {
                    $h.val('00');
                    $m.val('00');
                }

                if (!o.hasClass('JS_no-minutes-autofill')) {
                    if (($m.val() === '' || $m.val() === '__') && $h.val() !== '' && $h.val() !== '__') {
                        $m.val('00');
                    }
                }

                if ($d.val() === '__/__/____') {
                    $d.val('');
                }

                if ($d.val() !== '') {
                    var a = moment(
                        $d.val() + ' ' +
                            $h.val() + ':' +
                            $m.val(), 'DD/MM/YYYY HH:mm');

                    if (o.val() !== '' && a.format('YYYY') !== '0000') {
                        $d.val(a.format('DD/MM/YYYY'));
                        $h.val(a.format('HH'));
                        $m.val(a.format('mm'));
                    }

                    o.val(a.format('DD/MM/YYYY HH:mm'));

                    $._isFieldValid(o);

                    $._log('VALIDATION.validateDateTimePicker => trigger change event');
                    o.change();
                }

            }

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _isFieldValid
         * @description todo
         * @example todo
         */
        
        /*
         * @description
         * The general client-side validation function that can be called on demand and that will be called on every display refresh for field having the JS_live-validation class
         * @class .
         * @name _isFieldValid
         * @memberOf $
         * @param o {object} the jQuery element to be validated <br><i>default : undefined</i>
         * @param isUpdateDisplay {boolean} if the element should be visually marked invalid on the screen <br><i>default : undefined</i>
         * @param isValidForced {boolean} pass an outside condition into this parameters, this will bypass the validation of the _isFieldValid function itself but use the other actions (display invalidity) of the function <br><i>default : undefined</i>
         * @param o1 {object} if two elements must be validated against each other (this is the first element of the pair) <br><i>default : undefined</i>
         * @param o2 {object} if two elements must be validated against each other (this is the second element of the pair) <br><i>default : undefined</i>
         */
        _isFieldValid: function (o, isUpdateDisplay, isValidForced, o1, o2) {

            //check first if the display should be updated
            var updateDisplay = false;

            var isValid = false;

            // several options are possible :
            // 1. isUpdateDisplay parameter not specified => check for the global param
            if (isUpdateDisplay === undefined && $._settings.isFieldValidationUpdateDisplay) {
                updateDisplay = true;
            }
            // 2. isUpdateDisplay is TRUE, then this take the lead (used in the global validation on a form for example)
            if (isUpdateDisplay) {
                updateDisplay = true;
            }


            //field comparison validation
            if (o1 !== undefined && o2 !== undefined) {
                //options data are on the first field

                //first single field validity is checked
                var isValido1 = $._isFieldValidSingle(o1, updateDisplay);
                var isValido2 = $._isFieldValidSingle(o2, updateDisplay);

                var validationContentId = o1.attr('id') + '_' + o2.attr('id') + '_val_content';

                var $validationContent = $('#' + validationContentId);

                //then comparison validation occurs only if both fields passed their own validations
                if (!isValido1 || !isValido2) {

                    $('#' + validationContentId).remove();

                    //in case of one field or the other is invalid, invalidate both
                    o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                    o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');

                } else {

                    //comparison starts based on class provided ALWAYS ON FIELD1 !!
                    //checking equality
                    if (o1.hasClass('JS_comp-equal')) {
                        isValid = (o1.val() === o2.val());
                    }

                    //checking date period
                    if (o1.hasClass('JS_comp-datefromto') || o1.hasClass('JS_comp-datefromto-notequal')) {

                        var dateFrom;
                        var dateTo;

                        if (o1.hasClass('JS_datetimepicker')) {

                            dateFrom = $._getDate(o1.val());
                            dateTo = $._getDate(o2.val());

                        } else if (o1.hasClass('JS_datepicker')) {

                            dateFrom = $._getDate(o1.val());
                            dateTo = $._getDate(o2.val());

                        }

                        if (o1.hasClass('JS_comp-datefromto')) {
                            isValid = $._isPeriodValid(dateFrom, dateTo);
                        } else if (o1.hasClass('JS_comp-datefromto-notequal')) {
                            isValid = $._isPeriodValidNotEqual(dateFrom, dateTo);
                        }

                    }


                    //updating the required toggle icon
                    if (isValid) {
                        o1.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                        o2.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                    } else {
                        o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                        o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                    }


                    //updating the display
                    if (updateDisplay) {

                        //getting the validation message if exists
                        var validationMessage = '';
                        var $validationMessage = $('#' + o1.attr('id') + '_val_message');

                        if (o1.attr('alt') !== undefined) {
                            validationMessage = o1.attr('alt');
                        } else {
                            if ($validationMessage.length) {
                                validationMessage = $.trim($validationMessage.text());
                            } else {
                                if (o1.hasClass('JS_comp-equal')) {
                                    validationMessage = $._getData('jscaf_common_comp_invalid_equal');
                                }
                                if (o1.hasClass('JS_comp-datefromto')) {
                                    validationMessage = $._getData('jscaf_common_comp_invalid_datefromto');
                                }
                            }
                        }


                        //displaying the additional content
                        var validationContent = '';

                        if (o1.hasClass('JS_show_validation_error')) {

                            if (isValid) {
                                $('#' + validationContentId).remove();
                            } else {

                                if (!$('#' + validationContentId).length) {

                                    if (o2.hasClass('JS_error_bottom')) {
                                        validationContent += '<br>';
                                    }

                                    validationContent += '<div id="' + validationContentId + '" class="fl" style="margin-top:0;';

                                    if (o2.hasClass('JS_error_bottom')) {
                                        //finding top label width to left align
                                        var labelWidth = parseInt(o2.parent().find('.field-label').width(), 10);
                                        //adjust padding
                                        labelWidth += 2;
                                        validationContent += 'margin-bottom:10px; margin-left:' + labelWidth + 'px;';
                                    }
                                    validationContent += '">' +
                                        '   <div class="validation box">' +
                                        '     <p>' + validationMessage + '</p>' +
                                        '   </div>' +
                                        '</div>';

                                    if (o1.hasClass('JS_datetimepicker')) {
                                        o2.next().next().next().next().next().next().after(validationContent);
                                    } else {
                                        if (o1.hasClass('JS_datepicker')) {
                                            o2.next().next().after(validationContent);
                                        } else {
                                            o2.next().after(validationContent);
                                        }
                                    }

                                }
                            }

                        } else if (o1.hasClass('JS_show_validation_error_no_label')) {

                            if (isValid) {
                                $('#' + validationContentId).remove();
                            } else {

                                if (!$('#' + validationContentId).length) {

                                    validationContent = '<div id="' + validationContentId + '" title="' + validationMessage + '" class="fl cr-pointer" style="margin-top:0">' +
                                        '   <div class="validation">' +
                                        '     <p class="no-label">&nbsp;</p>' +
                                        '   </div>' +
                                        '</div>';

                                    if (o1.hasClass('JS_datetimepicker')) {
                                        o2.next().next().next().next().next().next().after(validationContent);
                                    } else {
                                        if (o1.hasClass('JS_datepicker')) {
                                            o2.next().next().after(validationContent);
                                        } else {
                                            o2.next().after(validationContent);
                                        }
                                    }

                                    $('#' + validationContentId).poshytip({
                                        className: 'tip-error',
                                        showOn: 'hover',
                                        alignTo: 'target',
                                        alignX: 'right',
                                        offsetX: -2,
                                        offsetY: -28
                                    });

                                }
                            }
                        }

                        //default display of a field : ui-state-error class added when not valid

                        if (isValid) {

                            if (o1.hasClass('JS_datetimepicker')) {
                                o1.parent().find('input').removeClass('ui-state-error');
                            } else {
                                o1.removeClass('ui-state-error');
                            }

                            if (o2.hasClass('JS_datetimepicker')) {
                                o2.parent().find('input').removeClass('ui-state-error');
                            } else {
                                o2.removeClass('ui-state-error');
                            }

                        } else {

                            if (o1.hasClass('JS_datetimepicker')) {
                                o1.parent().find('input').addClass('ui-state-error');
                            } else {
                                o1.addClass('ui-state-error');
                            }

                            if (o2.hasClass('JS_datetimepicker')) {
                                o2.parent().find('input').addClass('ui-state-error');
                            } else {
                                o2.addClass('ui-state-error');
                            }

                        }

                    }

                }

                //single field validation (default)
            } else {

                isValid = $._isFieldValidSingle(o, isUpdateDisplay, isValidForced);

            }

            return isValid;
        },


        _isFieldValidSingle: function (o, isUpdateDisplay, isValidForced) {

            var isSelect = false;
            var isDate = false;
            var isDateTime = false;
            var isSpan = false;
            var isApproveToggle = false;
            var isButtonSet = false;
            var isTextareaElastic = false;
            var isEmail = false;
            var isSelect2 = false;

            var isNumberField = false;
            var isZeroValidation = false;

            var isCreditCard = false;

            var isStarRating = false;

            var dateResult, creditCardResult;


            var isValid = false;

            //first check if the object exist
            if (!o.length) {
                //returning true to not break the client-side validation
                return true;
            }

            var nodeName = o.get(0).nodeName;

            if (nodeName === 'SELECT') {
                isSelect = true;
            } else {
                if (nodeName === 'INPUT') {
                    if (o.hasClass('JS_datepicker')) {
                        isDate = true;
                    }
                    if (o.hasClass('JS_datetimepicker')) {
                        isDateTime = true;
                    }
                    if (o.hasClass('JS_field-number') || o.hasClass('JS_field-number0to9')) {
                        isNumberField = true;
                        if (o.hasClass('JS_zero-validation')) {
                            isZeroValidation = true;
                        }
                    }
                    if (o.hasClass('JS_email')) {
                        isEmail = true;
                    }
                    if (o.hasClass('JS_field-credit-card-number')) {
                        isCreditCard = true;
                    }
                } else {
                    if (nodeName === 'SPAN') {
                        if (o.hasClass('JS_buttonSet')) {
                            isButtonSet = true;
                        } else if (o.hasClass('JS_approve-toggle')) {
                            isApproveToggle = true;
                        } else {
                            isSpan = true;
                        }
                    } else {
                        if (nodeName === 'TEXTAREA') {
                            if (o.hasClass('JS_elastic')) {
                                isTextareaElastic = true;
                            }
                        } else {
                            if (o.find('.ui-stars-star').length) {
                                isStarRating = true;
                            } else {
                                if (o.hasClass('select2-container')) {
                                    isSelect2 = true;
                                }
                            }
                        }
                    }
                }
            }


            if (isValidForced === undefined) {

                if (isSelect) {
                    if (o.val() === '-1' || o.val() === 'XXX' || o.val() === '' || o.val() === 'All' || o.val() === 'null / null') {
                        isValid = false;
                    } else {
                        isValid = o.children().length !== 0; //prevent validity of empty select (no options)
                    }
                } else {
                    if (isDate) {
                        dateResult = $._checkDate(o.val());
                        isValid = !(dateResult === -1 || dateResult === -2);
                    } else if (isDateTime) {
                        isValid = !($.trim(o.val()).length === 0 || o.val() === 'Invalid Date' || o.val() === 'NaN/NaN/NaN NaN:NaN' || o.val() === 'NaN');
                    } else {
                        if (isSpan) {
                            isValid = $.trim(o.text()) !== '';
                        } else if (isApproveToggle) {
                            isValid = !(o.hasClass('approve-toggle-disabled'));
                        } else if (isButtonSet) {
                            var buttonSetVal = $('[name="' + o.attr('id') + '"]:checked').attr('value');
                            isValid = buttonSetVal !== undefined;
                        } else {
                            if (isStarRating) {
                                isValid = o.find('input').val() !== '0';
                            } else {
                                if (isSelect2) {
                                    isValid = true; //forced to valid as the validation is done on the hidden linked select
                                } else {    
                                    if ($.trim(o.val()).length === 0) {
                                        if (isTextareaElastic) {
                                            isValid = false;
                                        } else {
                                            try {
                                                isValid = $.trim(o.html()).length !== 0;
                                            } catch (err) {
                                                isValid = false;
                                            }
                                        }
                                    } else {
                                        if (isEmail) {
                                            isValid = o.filter(
                                                function () {
                                                    return this.value.match('[a-zA-Z0-9-_]+[a-zA-Z0-9.-_]*@[a-zA-Z0-9-_]+.[a-zA-Z.-_]{1,}[a-zA-Z-_]+');
                                                }).length !== 0;
                                        } else {
                                            if (isCreditCard) {
                                                creditCardResult = $._checkCreditCard(o);
                                                isValid = (creditCardResult === 0);
                                            } else {
                                                isValid = true;
                                            }
                                        }
                                    }

                                    if (isValid) {
                                        if (isNumberField && isZeroValidation) {
                                            if (parseFloat(o.val()) === 0.0) {
                                                isValid = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                isValid = isValidForced;
            }


            //updating the required toggle icon
            if (!isSelect2) {
                if (isValid) {
                    o.closest('.field').find('span.required-toggle')
                        .removeClass('required-required')
                        .addClass('required-valid');
                } else {
                    o.closest('.field').find('span.required-toggle')
                        .removeClass('required-valid')
                        .addClass('required-required');
                }
            }

            //check first if the display should be updated
            var updateDisplay = false;

            // several options are possible :
            // 1. isUpdateDisplay parameter not specified => check for the global param
            if (isUpdateDisplay === undefined && $._settings.isFieldValidationUpdateDisplay) {
                updateDisplay = true;
            }
            // 2. isUpdateDisplay is TRUE, then this take the lead (used in the global validation on a form for example)
            if (isUpdateDisplay) {
                updateDisplay = true;
            }


            //updating the display - display the validation content close to the field
            if (updateDisplay) {

                var validationMessage = null;

                //setting validation message
                if (!isValid) {

                    if (o.attr('alt') !== undefined) {
                        validationMessage = o.attr('alt');
                    } else {

                        if ($('#' + o.attr('id') + '_val_message').length) {

                            validationMessage = $.trim($('#' + o.attr('id') + '_val_message').text());

                        } else {

                            if (!isDate && !isDateTime) {
                                if (isEmail) {
                                    validationMessage = $._getData('jscaf_common_email_invalid');
                                } else {
                                    if (isCreditCard && creditCardResult !== undefined) {
                                        if (creditCardResult === -1) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_type_not_matched');
                                        } else if (creditCardResult === -2) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_length');
                                        } else if (creditCardResult === -3) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_number_sequence');
                                        }
                                    } else {
                                        validationMessage = $._getData('jscaf_common_required');
                                    }
                                }
                            }
                            else {
                                if (isDate) {
                                    if (dateResult === -1) {
                                        validationMessage = $._getData('jscaf_common_date_invalid_format');
                                    } else {
                                        validationMessage = $._getData('jscaf_common_date_invalid');
                                    }
                                } else if (isDateTime) {
                                    validationMessage = $._getData('jscaf_common_date_invalid');
                                }
                            }
                        }
                    }

                }


                //VALIDATION DISPLAY set on extra content
                //---------------------------------------

                var validationContent = '';
                var validationContentId = o.attr('id') + '_val_content';
                var $validationContent = ('#' + validationContentId);

                if (o.hasClass('JS_show_validation_error')) {

                    if (isValid) {
                        $('#' + validationContentId).remove();
                    } else {

                        $('#' + validationContentId).remove();

                        if (!$('#' + validationContentId).length) {

                            if (o.hasClass('JS_error_bottom')) {
                                validationContent += '<br>';
                            }

                            validationContent += '<div id="' + validationContentId + '" class="fl" style="margin-top:0;';

                            if (o.hasClass('JS_error_bottom')) {
                                //finding top label width to left align
                                var labelWidth = parseInt(o.parent().find('.field-label').width(), 10);
                                //adjust padding
                                labelWidth += 2;
                                validationContent += 'margin-bottom:10px; margin-left:' + labelWidth + 'px;';
                            }
                            validationContent += '">' +
                                '   <div class="validation box">' +
                                '     <p>' + validationMessage + '</p>' +
                                '   </div>' +
                                '</div>';

                            if (isDateTime) {
                                o.next().next().next().next().next().next().after(validationContent);
                            } else {
                                if (isDate) {
                                    if (o.next().next().length) {
                                        o.next().next().after(validationContent);
                                    } else {
                                        o.next().after(validationContent);
                                    }
                                } else {
                                    o.next().after(validationContent);
                                }
                            }


                        }
                    }

                } else {

                    if (o.hasClass('JS_show_validation_error_no_label')) {

                        $('#' + validationContentId).remove();

                        if (isValid) {
                            $('#' + validationContentId).remove();

                        } else {

                            if (!$('#' + validationContentId).length) {

                                validationContent = '<div id="' + validationContentId + '" title="' + validationMessage + '" class="fl cr-pointer" style="margin-top:0">' +
                                    '   <div class="validation">' +
                                    '     <p class="no-label">&nbsp;</p>' +
                                    '   </div>' +
                                    '</div>';
                                if (isDateTime) {
                                    o.next().next().next().next().next().next().after(validationContent);
                                } else {
                                    if (isDate) {
                                        o.next().next().after(validationContent);
                                    } else {
                                        o.next().after(validationContent);
                                    }
                                }
                                $('#' + validationContentId).poshytip({
                                    className: 'tip-error',
                                    showOn: 'hover',
                                    alignTo: 'target',
                                    alignX: 'right',
                                    offsetX: -2,
                                    offsetY: -28
                                });
                            }
                        }

                    }
                }


                //default display of a field : ui-state-error class added when not valid

                if (isValid) {

                    if (o.hasClass('JS_datetimepicker')) {
                        o.parent().find('input').removeClass('ui-state-error');
                    } else {
                        if (o.next().hasClass('select2-container')) {
                            o.next().removeClass('ui-state-error');
                        } else {
                            if (!isSelect2) {
                                o.removeClass('ui-state-error');
                            }                                
                        }    
                    }

                } else {

                    if (o.hasClass('JS_datetimepicker')) {
                        o.parent().find('input').addClass('ui-state-error');
                    } else {
                        if (o.next().hasClass('select2-container')) {
                            o.next().addClass('ui-state-error');
                        } else {
                            if (!isSelect2) {
                                o.addClass('ui-state-error');
                            }    
                        }    
                    }

                }

            }

            return isValid;
        },


        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _checkCreditCard
         * @description todo
         * @example todo
         */
        _checkCreditCard: function (o) {
            var card_types,
                normalizedNumber,
                cardType;

            card_types = [
                {
                    name: 'amex',
                    pattern: /^3[47]/,
                    valid_length: [15]
                },
                {
                    name: 'diners_club_carte_blanche',
                    pattern: /^30[0-5]/,
                    valid_length: [14]
                },
                {
                    name: 'diners_club_international',
                    pattern: /^36/,
                    valid_length: [14]
                },
                {
                    name: 'jcb',
                    pattern: /^35(2[89]|[3-8][0-9])/,
                    valid_length: [16]
                },
                {
                    name: 'laser',
                    pattern: /^(6304|630[69]|6771)/,
                    valid_length: [16, 17, 18, 19]
                },
                {
                    name: 'visa_electron',
                    pattern: /^(4026|417500|4508|4844|491(3|7))/,
                    valid_length: [16]
                },
                {
                    name: 'visa',
                    pattern: /^4/,
                    valid_length: [16]
                },
                {
                    name: 'mastercard',
                    pattern: /^5[1-5]/,
                    valid_length: [16]
                },
                {
                    name: 'maestro',
                    pattern: /^(5018|5020|5038|6304|6759|676[1-3])/,
                    valid_length: [12, 13, 14, 15, 16, 17, 18, 19]
                },
                {
                    name: 'discover',
                    pattern: /^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)/,
                    valid_length: [16]
                }
            ];

            //normalize the input first
            normalizedNumber = o.val().replace(/[ \-]/g, '');

            //check credit card type
            var card_type, _i, _len;
            cardType = null;
            for (_i = 0, _len = card_types.length; _i < _len; _i++) {
                card_type = card_types[_i];
                if (normalizedNumber.match(card_type.pattern)) {
                    cardType = card_type;
                    break;
                }
            }
            if (cardType === null) {
                //credit card type cannot be matched
                return -1;
            }

            //checking number length
            var i, isLengthValid = false;

            for (i = 0; i < cardType.valid_length.length; i++) {
                if (cardType.valid_length[i] === normalizedNumber.length) {
                    isLengthValid = true;
                    break;
                }
            }

            if (!isLengthValid) {
                // length number not valid
                return -2;
            }


            //check credit card number validity
            var digit, n, sum, _ref;
            sum = 0;
            _ref = normalizedNumber.split('').reverse().join('');
            for (n = 0, _len = _ref.length; n < _len; n++) {
                digit = _ref[n];
                digit = +digit;
                if (n % 2) {
                    digit *= 2;
                    if (digit < 10) {
                        sum += digit;
                    } else {
                        sum += digit - 9;
                    }
                } else {
                    sum += digit;
                }
            }
            if (sum % 10 !== 0) {
                //invalid number
                return -3;
            }

            return 0;  //valid

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _validate
         * @description todo
         * @example todo
         */
        /*
         * @description
         * Validate all elements having the JS_live-validation class inside a provided container jQuery element
         * @class .
         * @name _validate
         * @memberOf $
         * @param $o {object} the jQuery element of the container <br><i>default : undefined</i>
         */
        _validate: function ($o) {

            var bValid = true;

            $._settings.isFieldValidationUpdateDisplay = true;

            $o.find('.JS_live-validation').each(function () {
                bValid = $._isFieldValid($(this), true) && bValid;
            });

            $._settings.isFieldValidationUpdateDisplay = false;

            return bValid;
        },





        __liveValidation: function ($o) {

            if ($o.attr('class').indexOf('JS_comp') > 0) {

                var linkedField;
                if ($o.hasClass('JS_comp-field1')) {
                    linkedField = $o.closest('.field').nextAll().filter('.field').find('.JS_comp-field2');
                    $._isFieldValid(null, null, null, $o, linkedField);

                } else if ($o.hasClass('JS_comp-field2')) {
                    linkedField = $o.closest('.field').prevAll().filter('.field').find('.JS_comp-field1');
                    $._isFieldValid(null, null, null, linkedField, $o);
                }

            } else {
                $._isFieldValid($o);
            }

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _checkDate
         * @description todo
         * @example todo
         */

        /*
         * @description
         * strictly checks if a String date is valid
         * @class .
         * @name _checkDate
         * @memberOf $
         * @param sDate {String} the date formatted as string to be checked <br><i>default : undefined</i>
         */
        _checkDate: function (sdate) {
            // basic regular expression : 1 or 2 digit / 1 or 2 digit / 1 to 4 digit to validate format
            var re = /^(\d{1,2}\/){2}\d{1,4}$/;
            if (!re.test(sdate)) {
                return(-1);
            }    // V?rification du format ? l'aide d'un masque de saisie
            var dateArray = sdate.split('/');
            if (dateArray.length !== 3) {
                return(-1);
            }
            var dd = parseInt(dateArray[0], 10); // extract day
            var mm = parseInt(dateArray[1], 10); // extract month
            var yyyy = parseInt(dateArray[2], 10); // extract year
            if (dd === 0 || mm === 0) {
                return(-2);
            } // day of month = 0 => bad format
            if (dd < 10 && dateArray[0].length === 1) {
                dateArray[0] = '0' + dateArray[0];
            }// day < 10 : concatenate with 0
            if (mm < 10 && dateArray[1].length === 1) {
                dateArray[1] = '0' + dateArray[1];
            }// month < 10 : concatenate with 0
            if (yyyy < 100) { // year <100
                yyyy += 2000;          // add 2000
                dateArray[2] = yyyy.toString(); // reformat input
            }
            if (yyyy < 2000 || yyyy > 9999 || mm < 1 || mm > 12 || dd < 1) {
                return(-2);
            } // irregular cases
            if (mm === 4 || mm === 6 || mm === 9 || mm === 11) {
                return (dd <= 30 ? 0 : -2);
            } // Mois de 30 jours
            if (mm === 1 || mm === 3 || mm === 5 || mm === 7 || mm === 8 || mm === 10 || mm === 12) {
                return (dd <= 31 ? 0 : -2);
            }  // Mois de 31 jours
            if ((yyyy % 4 !== 0) || ((yyyy % 4 === 0) && (yyyy % 400 === 0))) {
                return (dd <= 28 ? 0 : -2);
            }   // Mois de f?vrier, ann?e NON BISEXTILE
            return (dd <= 29 ? 0 : -2);   // Mois de f?vrier, ann?e BISEXTILE
        },


        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _isDateTimeValid
         * @description todo
         * @example todo
         */
        _isDateTimeValid: function (s) {

            //check the date only first
            if ($._checkDate(s) === 0) {
                return false;
            } else {

                var hours = s.substr(11, 2);
                var minutes = s.substr(14, 2);

                if (hours >= 0 && hours <= 23) {
                    return (minutes >= 0 && minutes <= 59);
                } else {
                    return false;
                }
            }
        }

    });  


}(jQuery));











/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===                 JSCAF RESOURCES MODULE           == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __RESOURCES: {

            load: function() {
                $._log('RESOURCES.load');

                // LOADING jSCAF COMMON TRANSLATION RESOURCES en/fr

                if ($._settings.language === 'fr') {
                    $.__globMessagesArray.push({key:'jscaf_common_home',value:'Accueil'});
                    $.__globMessagesArray.push({key:'jscaf_common_generated_in',value:'(Page g&eacute;n&eacute;r&eacute;e en '});
                    $.__globMessagesArray.push({key:'jscaf_common_cancel',value:'annuler'});
                    $.__globMessagesArray.push({key:'jscaf_common_close',value:'fermer'});
                    $.__globMessagesArray.push({key:'jscaf_common_YES',value:'OUI'});
                    $.__globMessagesArray.push({key:'jscaf_common_NO',value:'NON'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_warning',value:'ALERTE'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_error',value:'ERREUR'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_info',value:'INFORMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_confirm',value:'CONFIRMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_question',value:'QUESTION'});
                    $.__globMessagesArray.push({key:'jscaf_common_required_fields_missing',value:'Certains champs obligatoires (*) ne sont pas remplis ou sont invalides ...'});
                    $.__globMessagesArray.push({key:'jscaf_common_required',value:'Ce champ est obligatoire!'});
                    $.__globMessagesArray.push({key:'jscaf_common_email_invalid',value:'Adresse email invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid_format',value:'Format de date invalide (DD/MM/YYYY)!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid',value:'Date invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_period_invalid',value:'P&eacute;riode invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_wait',value:'Attendez svp...chargement'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_equal',value:'Les champs doivent &ecirc;tre identiques!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_datefromto',value:'P&eacute;riode invalide, la date de d&eacute;but doit &ecirc;tre inf&eacute;rieure &aacute; la date de fin!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_type_not_matched',value:'La s&eacute;quence introduite ne correspond a aucun type de carte connu!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_length',value:'La taille de la s&eacute;quence introduite est invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_number_sequence',value:'La validation de la séquence introduite est invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_page_zoom_warning', value:"MESSAGE IMPORTANT : Vous utilisez un zoom sur la page autre que 100%, ceci peut engendrer des erreurs d'affichage et/ou des erreurs lors de la sauvegarde."});
                    $.__globMessagesArray.push({key:'jscaf_common_browser_warning', value:"MESSAGE IMPORTANT : La version de votre browser n'est pas optimale pour l'utilisation de l'application, elle peut provoquer des problèmes d'affichage."});
                    $.__globMessagesArray.push({key:'jscaf_common_onbeforeunload_message', value:"Vous avez essayé de quitter cette page. Si vous avez déjà modifié des données et n'avez pas sauvé celles-ci au moyen du bouton SAUVER, ces données seront perdues. Etes-vous sûr de vouloir quitter cette page?"});
                    $.__globMessagesArray.push({key:'jscaf_email_subject',value:'Sujet'});
                    $.__globMessagesArray.push({key:'jscaf_email_content',value:'Contenu'});
                    $.__globMessagesArray.push({key:'jscaf_email_send',value:'Envoyer'});
                    $.__globMessagesArray.push({key:'jscaf_common_download_file',value:'Télécharger ce fichier'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_title',value:'Votre session va expirer !'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_subTitle',value:'Voulez-vous rester connecté ?'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_text',value:'Vous allez être déconnecté dans <span id="timeoutDialog_delay" class="text-color-blue very-big">@COUNTDOWN@</span> secondes.'});
                } else {
                    $.__globMessagesArray.push({key:'jscaf_common_home',value:'Home'});
                    $.__globMessagesArray.push({key:'jscaf_common_generated_in',value:'(Page generated in '});
                    $.__globMessagesArray.push({key:'jscaf_common_wait',value:'Please wait...loading'});
                    $.__globMessagesArray.push({key:'jscaf_common_cancel',value:'cancel'});
                    $.__globMessagesArray.push({key:'jscaf_common_close',value:'close'});
                    $.__globMessagesArray.push({key:'jscaf_common_YES',value:'YES'});
                    $.__globMessagesArray.push({key:'jscaf_common_NO',value:'NO'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_warning',value:'WARNING'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_error',value:'ERROR'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_info',value:'INFORMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_confirm',value:'CONFIRMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_question',value:'QUESTION'});
                    $.__globMessagesArray.push({key:'jscaf_common_required_fields_missing',value:'Some mandatory fields (*) are not filled or are invalid ...'});
                    $.__globMessagesArray.push({key:'jscaf_common_required',value:'This field is required!'});
                    $.__globMessagesArray.push({key:'jscaf_common_email_invalid',value:'This email address is invalid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid_format',value:'Invalid date format provided (DD/MM/YYYY)!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid',value:'Invalid date!'});
                    $.__globMessagesArray.push({key:'jscaf_common_period_invalid',value:'Invalid period!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_equal',value:'Fields must me equal!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_datefromto',value:'Invalid period, date from must be before date to!!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_type_not_matched',value:'Number sequence not matching any known credit card type!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_length',value:'Number sequence length is not valid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_number_sequence',value:'Number sequence validation is not valid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_page_zoom_warning', value:"IMPORTANT MESSAGE :  You're using a page zoom other than 100%, this might cause components to be incorrectly displayed and lead to errors during save operations."});
                    $.__globMessagesArray.push({key:'jscaf_common_browser_warning',value:'IMPORTANT MESSAGE : Your browser version is not optimal for the application usage, it might cause display problems.'});
                    $.__globMessagesArray.push({key:'jscaf_common_onbeforeunload_message',value:'You have attempted to leave this page.  If you have made any changes to the fields without clicking the SAVE button, your changes will be lost.  Are you sure you want to exit this page?'});
                    $.__globMessagesArray.push({key:'jscaf_email_subject',value:'Subject'});
                    $.__globMessagesArray.push({key:'jscaf_email_content',value:'Content'});
                    $.__globMessagesArray.push({key:'jscaf_email_send',value:'Send'});
                    $.__globMessagesArray.push({key:'jscaf_common_download_file',value:'Download this file'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_title',value:'Your session is about to expire !'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_subTitle',value:'Do you want to stay signed in ?'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_text',value:'You will be logged out in <span id="timeoutDialog_delay" class="text-color-blue very-big">@COUNTDOWN@</span> seconds.'});
                }                


                //LOADING TRANSFERED APP MESSAGES
                
                var $appMessages = $('#jscaf_app_i18n');
                if ($appMessages.length) {
                    $appMessages.find('li').each(function() {
                        var $data = $(this).data();
                        $.__globMessagesArray.push({key:$data.key, value:$data.value});
                        $._log('====>app message added : ' + $data.key + '=>' + $data.value);
                    });
                } else {
                    $._log('====>no app message found');
                }


                //LOADING TRANSFERED PAGE MESSAGES

                var $pageMessages = $('#jscaf_page_i18n');
                if ($pageMessages.length) {
                    $pageMessages.find('li').each(function() {
                        var $data = $(this).data();
                        $.__globMessagesArray.push({key:$data.key, value:$data.value});
                        $._log('====>page message added : ' + $data.key + '=>' + $data.value);
                    });
                } else {
                    $._log('====>no page message found');
                }


                //LOADING EXTRA RESOURCES IF PROVIDED

                if ($._settings.fnExtraResourcesLoader !== null && typeof($._settings.fnExtraResourcesLoader) === 'function') {
                    $._execFn($._settings.fnExtraResourcesLoader);
                }


            }



        }








    });
}(jQuery));


/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/

/**=======================================================
 * =======================================================
 * @fileOverview jSCAF Javascript API
 * @author ROELS Gregory - DIGIT.B.1
 * =======================================================
 * ======================================================= */


/* ======================================================= */
/* ===       INITIAL PAGE LOAD - EXECUTED ONCE !        == */
/* ======================================================= */

$(function() {

    //JSCAF VERSION
    //-------------
    $.__VERSION =  '1.10.05 - 07.04.2015';
    //-----------------------------------
    //-----------------------------------




    //console.profile(new Date());


    $._log('jSCAF:' +  $.__VERSION);
    $._log('-------------------------------------------------------------------------');



    $._log('MAIN.document.ready');


    //GETTING APP SETTINGS
    //--------------------
    var $appSettings = $('#jscaf_app_settings');
    $._log('APP.loading global settings');
    if ($appSettings.length) {
        $._log('====>APP settings exists, extending default $._settings');
        $._settings = $.extend( $._settings, $appSettings.data());
    } else {
        $._log('====>APP settings not found, taking default settings');
    }



    //MODULES DEFINITION
    //------------------
    $.__modules = {
        AJAX: $.__AJAX !== undefined,
        DISPLAY: $.__DISPLAY !== undefined,
        UI_DIALOG: $.__UI_DIALOG !== undefined,
        VALIDATION: $.__VALIDATION !== undefined,
        COMPONENTS: $.__COMPONENTS !== undefined,
        BROWSER: $.__BROWSER !== undefined,
        SPA: $.__SPA !== undefined,
        RESOURCES: $.__RESOURCES !== undefined,
    };

    
    // force to single page application detection if the SPA module has been loaded

    if ($.__modules.SPA) {
        $._settings.isSinglePageApp = true;
    }


    //CHECKING THE APP OBJECT DEFINITION
    //----------------------------------
    if (window[$._settings.appName] === undefined && !$._settings.isSinglePageApp) {

        alert('jscaf::ERROR -- MAIN.jSCAF-not-ready\n\nMESSAGE:\n' + $._settings.appName + ' object is not defined !');

        throw new Error('jscaf::ERROR -- MAIN.APP-not-defined');

    } 



    //checking modules definition
    if ( !$.__MAIN.is_jSCAF_ready() ) {

        alert('jscaf::ERROR -- MAIN.jSCAF-not-ready\n\nMESSAGE:\neither CORE or RESOURCES or INIT or UTILS modules are not defined !');

        throw new Error('jscaf::ERROR -- MAIN.jSCAF-not-ready');


    } else {

        //init jSCAF dictionnary
        $.__RESOURCES.load();

        //when the page is loaded, alert the user that the page is rendered
        if ($._settings.isBlockOnLoadActive) {
            $._blockUI();
        }

        //cache the most used dom elements first
        $.__initCachedElements();

        //===============================================================================
        $.__startInit();   // STARTING PAGE INITIALISATION
        //===============================================================================

    }






    //console.profileEnd();


 });




(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __MAIN: {

            // MODULES HELPER FUNCTIONS
            // ------------------------

            is_jSCAF_ready: function() {
                //mandatory modules
                return ( $.__coreModuleIdentifier !== undefined &&
                         $.__initModuleIdentifier !== undefined &&
                         $.__utilsModuleIdentifier !== undefined &&
                         $.__modules.RESOURCES);

            }

        }


    });
}(jQuery));


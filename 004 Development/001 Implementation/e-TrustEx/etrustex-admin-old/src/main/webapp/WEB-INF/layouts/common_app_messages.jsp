<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>


<%-- ===================================================== --%>
<%-- GLOBAL SCRIPT (put here only sent JSP => JS content)  --%>
<%-- ===================================================== --%>
<%-- THIS SCRIPT IS EXECUTED AUTOMATICALLY AT EACH PAGE REFRESH AND IN FIRST !--%>
<script type="text/javascript">
    <%-- Put here language properties that needs to be accessed inside JS all over the application --%>
    <%-- ========================================================================================= --%>
    $._setData('cipadmin.logout', '<j:bean_message message_key="cipadmin.logout"/>');
    $._setData('button.ok', '<j:bean_message message_key="button.ok"/>');
    $._setData('button.cancel', '<j:bean_message message_key="button.cancel"/>');
    $._setData('common.missing.mandatory.fields', '<j:bean_message message_key="common.missing.mandatory.fields"/>');
    $._setData('error.party.credentials.password.mismatch', '<j:bean_message message_key="error.party.credentials.password.mismatch"/>');
    $._setData('error.search.criteria.needed', '<j:bean_message message_key="error.search.criteria.needed"/>');
    $._setData('common.yes', '<j:bean_message message_key="common.yes"/>');
    $._setData('common.no', '<j:bean_message message_key="common.no"/>');
    $._setData('common.error.outOfSession.problemdescription', '<j:bean_message message_key="common.error.outOfSession.problemdescription"/>');
    $._setData('common.system.error', '<j:bean_message message_key="common.system.error"/>');

    $._setData('common.popup.title.partySearch', '<j:bean_message message_key="common.popup.title.partySearch"/>');
    $._setData('common.popup.title.partyCreation', '<j:bean_message message_key="common.popup.title.partyCreation"/>');
    $._setData('party.first.dialog.title', '<j:bean_message message_key="party.first.dialog.title"/>');
    $._setData('party.second.dialog.title', '<j:bean_message message_key="party.second.dialog.title"/>');
    $._setData('party.third.dialog.title', '<j:bean_message message_key="party.third.dialog.title"/>');
    $._setData('party.authorizing.dialog.title', '<j:bean_message message_key="party.authorizing.dialog.title"/>');
    $._setData('party.delegate.dialog.title', '<j:bean_message message_key="party.delegate.dialog.title"/>');
    $._setData('ica.multi.select.parties.dialog.title', '<j:bean_message message_key="ica.multi.select.parties.dialog.title"/>');
    $._setData('ica.multiple.error.leave.warning.text', '<j:bean_message message_key="ica.multiple.error.leave.warning.text"/>');
    $._setData('ica.multiple.new.parties.caption', '<j:bean_message message_key="ica.multiple.new.parties.caption"/>');

    $._setData('common.popup.title.certificateCreation', '<j:bean_message message_key="common.popup.title.certificateCreation"/>');
    $._setData('common.popup.title.certificateEdit', '<j:bean_message message_key="common.popup.title.certificateEdit"/>');
    $._setData('common.popup.title.certificates.party.view', '<j:bean_message message_key="common.popup.title.certificates.party.view"/>');
    $._setData('common.popup.title.certificates.party.manage', '<j:bean_message message_key="common.popup.title.certificates.party.manage"/>');
    $._setData('common.popup.title.certificate.revoke.confirmation', '<j:bean_message message_key="common.popup.title.certificate.revoke.confirmation"/>');
    $._setData('common.popup.text.certificate.revoke.confirmation', '<j:bean_message message_key="common.popup.text.certificate.revoke.confirmation"/>');
    
    $._setData('common.popup.title.password.change', '<j:bean_message message_key="common.popup.title.password.change"/>');
    $._setData('common.popup.password.mismatch', '<j:bean_message message_key="common.popup.password.mismatch"/>');

    $._setData('common.popup.title.log', '<j:bean_message message_key="common.popup.title.log"/>');
    
    $._setData('common.popup.title.documentSearch', '<j:bean_message message_key="common.popup.title.documentSearch"/>');
    $._setData('common.popup.title.transactionSearch', '<j:bean_message message_key="common.popup.title.transactionSearch"/>');
    $._setData('common.popup.title.icaSearch', '<j:bean_message message_key="common.popup.title.icaSearch"/>');
    $._setData('common.popup.title.credentials', '<j:bean_message message_key="common.popup.title.credentials"/>');
    $._setData('common.popup.title.auth.credentials', '<j:bean_message message_key="common.popup.title.auth.credentials"/>');
    $._setData('common.popup.title.proxyCredentials', '<j:bean_message message_key="common.popup.title.proxyCredentials"/>');

    $._setData('document.view.title', '<j:bean_message message_key="document.view.title"/>');
    $._setData('transaction.view.title', '<j:bean_message message_key="transaction.view.title"/>');
    $._setData('ica.view.title', '<j:bean_message message_key="ica.view.title"/>');
    $._setData('party.view.title', '<j:bean_message message_key="party.view.title"/>');

    $._setData('choose.please', '<j:bean_message message_key="choose.please"/>');
    $._setData('tooltip.button.delete', '<j:bean_message message_key="tooltip.button.delete"/>');


    <%-- blockUI messages used for ajax calls --%>
    $._setData('ica.reloading.roles', '<j:bean_message message_key="ica.reloading.roles"/>');
    $._setData('blockUI.ica.saving', '<j:bean_message message_key="blockUI.ica.saving"/>');
    $._setData('blockUI.party.saving', '<j:bean_message message_key="blockUI.party.saving"/>');
    $._setData('blockUI.partyAgreement.saving', '<j:bean_message message_key="blockUI.partyAgreement.saving"/>');
    $._setData('blockUI.ica.multiple.saving', '<j:bean_message message_key="blockUI.ica.multiple.saving"/>');
    $._setData('blockUI.certificates.adding', '<j:bean_message message_key="blockUI.certificates.adding"/>');
    $._setData('common.reloading.parties', '<j:bean_message message_key="common.reloading.parties"/>');
    $._setData('common.searching.results', '<j:bean_message message_key="common.searching.results"/>');
    $._setData('common.user.load', '<j:bean_message message_key="common.user.load"/>');
    $._setData('user.rights.remove.confirmation.message', '<j:bean_message message_key="user.rights.remove.confirmation.message"/>');
    $._setData('user.rights.remove.confirmation.title', '<j:bean_message message_key="user.rights.remove.confirmation.title"/>');
    $._setData('party.management.confirmation.message.title', '<j:bean_message message_key="party.management.confirmation.message.title"/>');
    $._setData('partyAgreement.management.confirmation.message.title', '<j:bean_message message_key="partyAgreement.management.confirmation.message.title"/>');
    $._setData('document.management.confirmation.message.title', '<j:bean_message message_key="document.management.confirmation.message.title"/>');
    $._setData('endpoint.management.confirmation.message.title', '<j:bean_message message_key="endpoint.management.confirmation.message.title"/>');
    $._setData('metadata.management.confirmation.message.title', '<j:bean_message message_key="metadata.management.confirmation.message.title"/>');
    $._setData('profile.management.confirmation.message.title', '<j:bean_message message_key="profile.management.confirmation.message.title"/>');
    $._setData('transaction.management.confirmation.message.title', '<j:bean_message message_key="transaction.management.confirmation.message.title"/>');
    $._setData('message.binaries.dialog.title', '<j:bean_message message_key="message.binaries.dialog.title"/>');
    $._setData('message.routing.dialog.title', '<j:bean_message message_key="message.routing.dialog.title"/>');
    $._setData('message.child.dialog.title', '<j:bean_message message_key="message.child.dialog.title"/>');
    $._setData('message.parent.dialog.title', '<j:bean_message message_key="message.parent.dialog.title"/>');
    $._setData('endpoint.view.title', '<j:bean_message message_key="endpoint.view.title"/>');
    $._setData('retentionPolicy.management.confirmation.message.title', '<j:bean_message message_key="retentionPolicy.management.confirmation.message.title"/>');
    $._setData('common.management.confirmation.message.title', '<j:bean_message message_key="common.management.confirmation.message.title"/>');
    $._setData('message.resubmit.management.confirmation.message', '<j:bean_message message_key="message.resubmit.management.confirmation.message"/>');
    $._setData('message.redispatch.management.confirmation.message', '<j:bean_message message_key="message.redispatch.management.confirmation.message"/>');
    $._setData('message.child.view.title', '<j:bean_message message_key="message.child.view.title"/>');
    $._setData('message.parent.view.title', '<j:bean_message message_key="message.parent.view.title"/>');
    $._setData('party.management.decrypt.message.title', '<j:bean_message message_key="party.management.decrypt.message.title"/>');
    $._setData('party.management.decrypt.message', '<j:bean_message message_key="party.management.decrypt.message"/>');
    
    $._setData('party.management.decrypt.message', '<j:bean_message message_key="party.management.decrypt.message"/>');
    


    <%-- Access keys for menus --%>
    $._setData('menu.home.accessKey', '<j:bean_message message_key="menu.home.accessKey"/>');
    $._setData('menu.party.accessKey', '<j:bean_message message_key="menu.party.accessKey"/>');

    $._setData('jscaf.common_comp.invalid.not_equal', '<j:bean_message message_key="jscaf.common_comp.invalid.not_equal"/>');

    $._setData('error.ica.parties.roles.mustBeDifferent', '<j:bean_message message_key="error.ica.parties.roles.mustBeDifferent"/>');
    
    $._setData('error.certificate.file.size', '<j:bean_message message_key="error.certificate.file.size"/>');
    $._setData('error.certificate.file.empty', '<j:bean_message message_key="error.certificate.file.empty"/>');
    
    $._setData('bulkredispatch.error.file.empty', '<j:bean_message message_key="bulkredispatch.error.file.empty"/>');
    $._setData('bulkredispatch.error.file.size', '<j:bean_message message_key="bulkredispatch.error.file.size"/>');
    $._setData('bulkredispatch.warning', '<j:bean_message message_key="bulkredispatch.warning"/>');
    $._setData('bulkredispatch.path', '<j:bean_message message_key="bulkredispatch.path"/>');
</script>


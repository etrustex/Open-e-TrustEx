package eu.europa.ec.etrustex.integration.config.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.europa.ec.etrustex.integration.business.AttachedDocument_2;
import eu.europa.ec.etrustex.integration.business.CreateInterchangeAgreementService_2_1;
import eu.europa.ec.etrustex.integration.business.CreatePartyService_2_1;
import eu.europa.ec.etrustex.integration.business.DeleteDocumentWrapper_2;
import eu.europa.ec.etrustex.integration.business.DeleteDocument_1;
import eu.europa.ec.etrustex.integration.business.InboxService_2;
import eu.europa.ec.etrustex.integration.business.QueryRequestService_2;
import eu.europa.ec.etrustex.integration.business.RetrieveDocumentService_2;
import eu.europa.ec.etrustex.integration.business.RetrieveDocumentWrapper_2;
import eu.europa.ec.etrustex.integration.business.RetrieveInterchangeAgreements_2;
import eu.europa.ec.etrustex.integration.business.RetrieveInterchangeAgreements_2_1;
import eu.europa.ec.etrustex.integration.business.StatusRequestService_2;
import eu.europa.ec.etrustex.integration.business.StoreDocumentWrapper_2;
import eu.europa.ec.etrustex.integration.business.SubmitApplicationResponse_2;
import eu.europa.ec.etrustex.integration.business.SubmitDocumentBundle_2;
import eu.europa.ec.etrustex.integration.business.ViewRequestService_2;
import eu.europa.ec.etrustex.integration.business.justice.QueryRequestJusticeService_2;
import eu.europa.ec.etrustex.integration.business.justice.SubmitDocumentBundleJustice_2;

@Configuration
public class PlatformServicesConfig {
	
	@Bean("SubmitInboxRequest-2.0")
	public InboxService_2 submitInboxRequest20() {
		return new InboxService_2();
	}

	@Bean("SubmitInboxRequest-2.1")
	public InboxService_2 submitInboxRequest21() {
		return new InboxService_2();
	}

	@Bean("StoreDocumentWrapper-2.0")
	public StoreDocumentWrapper_2 storeDocumentWrapper20() {
		return new StoreDocumentWrapper_2();
	}

	@Bean("DeleteDocumentWrapperRequest-2.0")
	public DeleteDocumentWrapper_2 deleteDocumentWrapperRequest20() {
		return new DeleteDocumentWrapper_2();
	}

	@Bean("SubmitDocumentBundleJustice-2.0")
	public SubmitDocumentBundleJustice_2 submitDocumentBundleJustice20() {
		return new SubmitDocumentBundleJustice_2();
	}

	@Bean("SubmitDocumentBundle-2.0")
	public SubmitDocumentBundle_2 submitDocumentBundle20() {
		return new SubmitDocumentBundle_2();
	}

	@Bean("SubmitDocumentBundle-2.1")
	public SubmitDocumentBundle_2 submitDocumentBundle21() {
		return new SubmitDocumentBundle_2();
	}

	@Bean("RetrieveDocumentWrapperRequest-2.0")
	public RetrieveDocumentWrapper_2 retrieveDocumentWrapperRequest20() {
		return new RetrieveDocumentWrapper_2();
	}

	@Bean("SubmitRetrieveRequest-2.0")
	public RetrieveDocumentService_2 retrieveDocumentService20() {
		return new RetrieveDocumentService_2();
	}

	@Bean("SubmitApplicationResponse-2.0")
	public SubmitApplicationResponse_2 submitApplicationResponse20() {
		return new SubmitApplicationResponse_2();
	}

	@Bean("SubmitStatusRequest-2.0")
	public StatusRequestService_2 statusRequestService20() {
		return new StatusRequestService_2();
	}

	@Bean("SubmitStatusRequest-2.2")
	public StatusRequestService_2 statusRequestService22() {
		return new StatusRequestService_2();
	}

	@Bean("SubmitQueryRequest-2.0")
	public QueryRequestService_2 queryRequestService20() {
		return new QueryRequestService_2();
	}

	@Bean("SubmitQueryRequestJustice-2.0")
	public QueryRequestJusticeService_2 queryRequestJusticeService20() {
		return new QueryRequestJusticeService_2();
	}

	@Bean("SubmitRetrieveInterchangeAgreements-2.0")
	public RetrieveInterchangeAgreements_2 ica20() {
		return new RetrieveInterchangeAgreements_2();
	}

	@Bean("SubmitAttachedDocument-2.0")
	public AttachedDocument_2 ad20() {
		return new AttachedDocument_2();
	}

	@Bean("SubmitViewRequest-2.0")
	public ViewRequestService_2 vr20() {
		return new ViewRequestService_2();
	}

	@Bean("CreateParty-2.1")
	public CreatePartyService_2_1 cp21() {
		return new CreatePartyService_2_1();
	}

	@Bean("CreateInterchangeAgreement-2.1")
	public CreateInterchangeAgreementService_2_1 cica21() {
		return new CreateInterchangeAgreementService_2_1();
	}

	@Bean("DeleteDocument-1.0")
	public DeleteDocument_1 dd1() {
		return new DeleteDocument_1();
	}

	@Bean("SubmitRetrieveInterchangeAgreements-2.1")
	public RetrieveInterchangeAgreements_2_1 rica21() {
		return new RetrieveInterchangeAgreements_2_1();
	}
}

package org.vitramu.master.definition;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.common.definition.FlowDefinitionXmlDocument;
import org.vitramu.common.definition.FlowDefinitionXmlDocumentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowDefinitionXmlDocumentRepositoryTest {

    static final String DEFINITION_ID = "Test-OSB-Machine-1";
    @Autowired
    private FlowDefinitionXmlDocumentRepository xmlDocumentRepository;
    @Test
    public void saveFlowDefinitionXmlDocument() {
        FlowDefinitionXmlDocument document = new FlowDefinitionXmlDocument();
        document.setDefinitionId(DEFINITION_ID);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<uml:Model xmi:version=\"20131001\" xmlns:xmi=\"http://www.omg.org/spec/XMI/20131001\" xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" xmlns:uml=\"http://www.eclipse.org/uml2/5.0.0/UML\" xmi:id=\"_AY_7QBaHEemdVtgy2H6EVw\" name=\"OSB-Machine\">\n" +
                "  <packageImport xmi:type=\"uml:PackageImport\" xmi:id=\"_AkHmIBaHEemdVtgy2H6EVw\">\n" +
                "    <importedPackage xmi:type=\"uml:Model\" href=\"pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0\"/>\n" +
                "  </packageImport>\n" +
                "  <packagedElement xmi:type=\"uml:StateMachine\" xmi:id=\"_AcIu0BaHEemdVtgy2H6EVw\" name=\"OSB-SM\">\n" +
                "    <region xmi:type=\"uml:Region\" xmi:id=\"_Acy2IBaHEemdVtgy2H6EVw\" name=\"Region1\">\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"_vSlXcBaHEemdVtgy2H6EVw\" name=\"REQUEST_SAVING\" source=\"_FF3TABaHEemdVtgy2H6EVw\" target=\"_ryD3ABaHEemdVtgy2H6EVw\"/>\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"__Y9lIBaHEemdVtgy2H6EVw\" source=\"_ryD3ABaHEemdVtgy2H6EVw\" target=\"_cYOCYBaLEemdVtgy2H6EVw\"/>\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"_Bu3QEBaJEemdVtgy2H6EVw\" source=\"_W6wbgBaIEemdVtgy2H6EVw\" target=\"_zwTqgBaIEemdVtgy2H6EVw\"/>\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"_GvSdABaJEemdVtgy2H6EVw\" source=\"_zwTqgBaIEemdVtgy2H6EVw\" target=\"_Fm6fsBaJEemdVtgy2H6EVw\"/>\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"_NqxocBaJEemdVtgy2H6EVw\" name=\"INITIALIZED\" source=\"_CZ-UgBaHEemdVtgy2H6EVw\" target=\"_FF3TABaHEemdVtgy2H6EVw\"/>\n" +
                "      <transition xmi:type=\"uml:Transition\" xmi:id=\"_EgxsIBaLEemdVtgy2H6EVw\" source=\"_ryD3ABaHEemdVtgy2H6EVw\" target=\"_aantQBaLEemdVtgy2H6EVw\"/>\n" +
                "      <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_CZ-UgBaHEemdVtgy2H6EVw\" name=\"REQUEST_ARRIVED\"/>\n" +
                "      <subvertex xmi:type=\"uml:State\" xmi:id=\"_FF3TABaHEemdVtgy2H6EVw\" name=\"REQUEST_SAVING\"/>\n" +
                "      <subvertex xmi:type=\"uml:State\" xmi:id=\"_q4o7sBaHEemdVtgy2H6EVw\" name=\"CREATE_BOOKING\">\n" +
                "        <region xmi:type=\"uml:Region\" xmi:id=\"_43bKwRaHEemdVtgy2H6EVw\" name=\"Region-OSB\">\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_fKRaoBaIEemdVtgy2H6EVw\" name=\"CREATE_OSB\" source=\"_cYOCYBaLEemdVtgy2H6EVw\" target=\"_eBMSsBaIEemdVtgy2H6EVw\"/>\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_vp3ZkBaIEemdVtgy2H6EVw\" source=\"_eBMSsBaIEemdVtgy2H6EVw\" target=\"_W6wbgBaIEemdVtgy2H6EVw\"/>\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_wb8PYBaLEemdVtgy2H6EVw\" source=\"_9aqscBaHEemdVtgy2H6EVw\" target=\"_cYOCYBaLEemdVtgy2H6EVw\"/>\n" +
                "          <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_9aqscBaHEemdVtgy2H6EVw\"/>\n" +
                "          <subvertex xmi:type=\"uml:State\" xmi:id=\"_eBMSsBaIEemdVtgy2H6EVw\" name=\"END_OSB\"/>\n" +
                "          <subvertex xmi:type=\"uml:State\" xmi:id=\"_cYOCYBaLEemdVtgy2H6EVw\" name=\"CREATE_OSB\"/>\n" +
                "        </region>\n" +
                "        <region xmi:type=\"uml:Region\" xmi:id=\"_59tdYBaHEemdVtgy2H6EVw\" name=\"Region-PUD\">\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_vEYt8BaIEemdVtgy2H6EVw\" name=\"CREATE_PUD\" source=\"_aantQBaLEemdVtgy2H6EVw\" target=\"_f4fykBaIEemdVtgy2H6EVw\"/>\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_wqRGoBaIEemdVtgy2H6EVw\" source=\"_f4fykBaIEemdVtgy2H6EVw\" target=\"_W6wbgBaIEemdVtgy2H6EVw\"/>\n" +
                "          <transition xmi:type=\"uml:Transition\" xmi:id=\"_uCGDIBaLEemdVtgy2H6EVw\" source=\"_G1C8MBaIEemdVtgy2H6EVw\" target=\"_aantQBaLEemdVtgy2H6EVw\"/>\n" +
                "          <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_G1C8MBaIEemdVtgy2H6EVw\"/>\n" +
                "          <subvertex xmi:type=\"uml:State\" xmi:id=\"_f4fykBaIEemdVtgy2H6EVw\" name=\"END_PUD\"/>\n" +
                "          <subvertex xmi:type=\"uml:State\" xmi:id=\"_aantQBaLEemdVtgy2H6EVw\" name=\"CREATE_PUD\"/>\n" +
                "        </region>\n" +
                "      </subvertex>\n" +
                "      <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_ryD3ABaHEemdVtgy2H6EVw\" name=\"CREATE_PARALLEL\" kind=\"fork\"/>\n" +
                "      <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_W6wbgBaIEemdVtgy2H6EVw\" name=\"CREATE_FINISH\" kind=\"join\"/>\n" +
                "      <subvertex xmi:type=\"uml:State\" xmi:id=\"_zwTqgBaIEemdVtgy2H6EVw\" name=\"REFRESH_STATUS\"/>\n" +
                "      <subvertex xmi:type=\"uml:FinalState\" xmi:id=\"_Fm6fsBaJEemdVtgy2H6EVw\" name=\"END\"/>\n" +
                "    </region>\n" +
                "  </packagedElement>\n" +
                "  <profileApplication xmi:type=\"uml:ProfileApplication\" xmi:id=\"_MtUzoBaHEemdVtgy2H6EVw\">\n" +
                "    <eAnnotations xmi:type=\"ecore:EAnnotation\" xmi:id=\"_MtWo0BaHEemdVtgy2H6EVw\" source=\"http://www.eclipse.org/uml2/2.0.0/UML\">\n" +
                "      <references xmi:type=\"ecore:EPackage\" href=\"pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ\"/>\n" +
                "    </eAnnotations>\n" +
                "    <appliedProfile xmi:type=\"uml:Profile\" href=\"pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage\"/>\n" +
                "  </profileApplication>\n" +
                "</uml:Model>\n";
        document.setXml(xml);
        xmlDocumentRepository.save(document);
    }

    @Test
    public void testFindAll() {
        System.out.println(xmlDocumentRepository.findAll());
    }
}
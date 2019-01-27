
package org.vitramu.engine.excution;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.common.definition.FlowDefinitionRepository;
import org.vitramu.common.definition.xml.FlowDefinitionXmlDocument;
import org.vitramu.common.definition.element.FlowDefinition;
import org.vitramu.common.definition.xml.FlowDefinitionXmlParser;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;

import static org.vitramu.common.constant.DefinitionState.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowDefinitionXmlParserTest {
    static String MACHINE_ID = "OSB-Machine-1";
    static final String XML = "<?content version=\"1.0\" encoding=\"UTF-8\"?> <uml:Model xmi:version=\"20131001\" xmlns:xmi=\"http://www.omg.org/spec/XMI/20131001\" xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" xmlns:uml=\"http://www.eclipse.org/uml2/5.0.0/UML\" xmi:id=\"_AY_7QBaHEemdVtgy2H6EVw\" name=\"OSB-Machine\"> <packageImport xmi:type=\"uml:PackageImport\" xmi:id=\"_AkHmIBaHEemdVtgy2H6EVw\"> <importedPackage xmi:type=\"uml:Model\" href=\"pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0\"/> </packageImport> <packagedElement xmi:type=\"uml:StateMachine\" xmi:id=\"_AcIu0BaHEemdVtgy2H6EVw\" name=\"OSB-SM\"> <region xmi:type=\"uml:Region\" xmi:id=\"_Acy2IBaHEemdVtgy2H6EVw\" name=\"Region1\"> <transition xmi:type=\"uml:Transition\" xmi:id=\"_vSlXcBaHEemdVtgy2H6EVw\" name=\"REQUEST_SAVING\" source=\"_FF3TABaHEemdVtgy2H6EVw\" target=\"_ryD3ABaHEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"__Y9lIBaHEemdVtgy2H6EVw\" source=\"_ryD3ABaHEemdVtgy2H6EVw\" target=\"_cYOCYBaLEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_Bu3QEBaJEemdVtgy2H6EVw\" source=\"_W6wbgBaIEemdVtgy2H6EVw\" target=\"_zwTqgBaIEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_GvSdABaJEemdVtgy2H6EVw\" source=\"_zwTqgBaIEemdVtgy2H6EVw\" target=\"_Fm6fsBaJEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_NqxocBaJEemdVtgy2H6EVw\" name=\"INITIALIZED\" source=\"_CZ-UgBaHEemdVtgy2H6EVw\" target=\"_FF3TABaHEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_EgxsIBaLEemdVtgy2H6EVw\" source=\"_ryD3ABaHEemdVtgy2H6EVw\" target=\"_aantQBaLEemdVtgy2H6EVw\"/> <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_CZ-UgBaHEemdVtgy2H6EVw\" name=\"REQUEST_ARRIVED\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_FF3TABaHEemdVtgy2H6EVw\" name=\"REQUEST_SAVING\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_q4o7sBaHEemdVtgy2H6EVw\" name=\"CREATE_BOOKING\"> <region xmi:type=\"uml:Region\" xmi:id=\"_43bKwRaHEemdVtgy2H6EVw\" name=\"Region-OSB\"> <transition xmi:type=\"uml:Transition\" xmi:id=\"_fKRaoBaIEemdVtgy2H6EVw\" name=\"CREATE_OSB\" source=\"_cYOCYBaLEemdVtgy2H6EVw\" target=\"_eBMSsBaIEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_vp3ZkBaIEemdVtgy2H6EVw\" source=\"_eBMSsBaIEemdVtgy2H6EVw\" target=\"_W6wbgBaIEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_wb8PYBaLEemdVtgy2H6EVw\" source=\"_9aqscBaHEemdVtgy2H6EVw\" target=\"_cYOCYBaLEemdVtgy2H6EVw\"/> <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_9aqscBaHEemdVtgy2H6EVw\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_eBMSsBaIEemdVtgy2H6EVw\" name=\"END_OSB\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_cYOCYBaLEemdVtgy2H6EVw\" name=\"CREATE_OSB\"/> </region> <region xmi:type=\"uml:Region\" xmi:id=\"_59tdYBaHEemdVtgy2H6EVw\" name=\"Region-PUD\"> <transition xmi:type=\"uml:Transition\" xmi:id=\"_vEYt8BaIEemdVtgy2H6EVw\" name=\"CREATE_PUD\" source=\"_aantQBaLEemdVtgy2H6EVw\" target=\"_f4fykBaIEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_wqRGoBaIEemdVtgy2H6EVw\" source=\"_f4fykBaIEemdVtgy2H6EVw\" target=\"_W6wbgBaIEemdVtgy2H6EVw\"/> <transition xmi:type=\"uml:Transition\" xmi:id=\"_uCGDIBaLEemdVtgy2H6EVw\" source=\"_G1C8MBaIEemdVtgy2H6EVw\" target=\"_aantQBaLEemdVtgy2H6EVw\"/> <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_G1C8MBaIEemdVtgy2H6EVw\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_f4fykBaIEemdVtgy2H6EVw\" name=\"END_PUD\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_aantQBaLEemdVtgy2H6EVw\" name=\"CREATE_PUD\"/> </region> </subvertex> <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_ryD3ABaHEemdVtgy2H6EVw\" name=\"CREATE_PARALLEL\" kind=\"fork\"/> <subvertex xmi:type=\"uml:Pseudostate\" xmi:id=\"_W6wbgBaIEemdVtgy2H6EVw\" name=\"CREATE_FINISH\" kind=\"join\"/> <subvertex xmi:type=\"uml:State\" xmi:id=\"_zwTqgBaIEemdVtgy2H6EVw\" name=\"REFRESH_STATUS\"/> <subvertex xmi:type=\"uml:FinalState\" xmi:id=\"_Fm6fsBaJEemdVtgy2H6EVw\" name=\"END\"/> </region> </packagedElement> <profileApplication xmi:type=\"uml:ProfileApplication\" xmi:id=\"_MtUzoBaHEemdVtgy2H6EVw\"> <eAnnotations xmi:type=\"ecore:EAnnotation\" xmi:id=\"_MtWo0BaHEemdVtgy2H6EVw\" source=\"http://www.eclipse.org/uml2/2.0.0/UML\"> <references xmi:type=\"ecore:EPackage\" href=\"pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ\"/> </eAnnotations> <appliedProfile xmi:type=\"uml:Profile\" href=\"pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage\"/> </profileApplication> </uml:Model>";

    @Autowired
    FlowDefinitionRepository definitionRepository;
    FlowDefinitionXmlParser xmlParser = new FlowDefinitionXmlParser();

    @Test
    public void testParse() throws IOException, JAXBException {
        FlowDefinitionXmlDocument doc = new FlowDefinitionXmlDocument();
        doc.setContent(XML);
        doc.setDefinitionId(MACHINE_ID);
        FlowDefinition def = xmlParser.parse(doc);

        definitionRepository.save(def);
    }


    @Autowired
    private StateMachineFactory<String, String> stateMachineFactory;

    @Test
    public void testLoadStateMachineFromMongo() throws Exception {
        StateMachine<String, String> sm1 = stateMachineFactory.getStateMachine(MACHINE_ID);
        String[] events = new String[]{
                "INITIALIZED",
                REQUEST_SAVING.getName(),
                CREATE_OSB.getName(),
                CREATE_PUD.getName(),
                REFRESH_STATUS.getName()
                /*, DefinitionState.CREATE_EW.getName() */};
        sm1.addStateListener(new FlowEngineEventListener());
        sm1.start();
        Arrays.stream(events)
                .forEach(event -> sm1.sendEvent(event));
    }
}
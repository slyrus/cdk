package org.openscience.cdk.qsar.descriptors.atomic;

import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.ISimpleChemObjectReader.Mode;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.atomic.RDFProtonDescriptor_GHR_topol;
import org.openscience.cdk.qsar.result.IDescriptorResult;

/**
 * @cdk.module test-qsaratomic
 */
public class RDFProtonDescriptor_GHR_topolTest extends AtomicDescriptorTest {

	public RDFProtonDescriptor_GHR_topolTest() {
    }

    public void setUp() throws Exception {
    	setDescriptor(RDFProtonDescriptor_GHR_topol.class);
    }
    
	public static Test suite() {
		return new TestSuite(RDFProtonDescriptor_GHR_topolTest.class);
	}
    
	public void testExample1() throws Exception {
		//firstly read file to molecule		
		String filename = "data/mdl/hydroxyamino.mol";
		InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
		MDLV2000Reader reader = new MDLV2000Reader(ins, Mode.STRICT);
		ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		IChemSequence seq = chemFile.getChemSequence(0);
		IChemModel model = seq.getChemModel(0);
		IMoleculeSet som = model.getMoleculeSet();
		IMolecule mol = som.getMolecule(0);

		for (int i=0; i < mol.getAtomCount(); i++) {
//			System.out.println("Atom: " + mol.getAtom(i).getSymbol());
			if(mol.getAtom(i).getSymbol().equals("H")){
				//secondly perform calculation on it.
				RDFProtonDescriptor_GHR_topol descriptor = new RDFProtonDescriptor_GHR_topol();
				DescriptorValue dv = descriptor.calculate(mol.getAtom(i),mol );
				IDescriptorResult result = dv.getValue();
//				System.out.println("array: " + result.toString());
				assertNotNull(result);
			}		

		}
	}
}
/*
 * $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2001  The Chemistry Development Kit (CDK) project
 *
 * Contact: steinbeck@ice.mpg.de, geelter@maul.chem.nd.edu, egonw@sci.kun.nl
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.applications;

import org.openscience.cdk.*;
import org.openscience.cdk.io.*;
import java.io.*;
import java.util.*;

/**
 * Program that converts a file from one format to a file with another format.
 * Supported formats are:
 *   input: CML, XYZ, MDLMolfile
 *  output: CML, MDL Molfile
 */
public class FileConvertor {

  private ChemObjectReader input;
  private ChemObjectWriter output;
  private ChemFile chemFile;

  private String iformat;
  private String oformat;

  public FileConvertor(String iformat, String oformat) {
    this.iformat = iformat;
    this.oformat = oformat;
  }

  public boolean convert(File input, File output) {
    boolean success = true;
    try {
      FileReader fr = new FileReader(input);
      FileWriter fw = new FileWriter(output);

      ChemObjectReader cor = getChemObjectReader(this.iformat, fr);
      if (cor == null) {
        System.err.println("Unsupported input format!");
	return false;
      }
      ChemObjectWriter cow = getChemObjectWriter(this.oformat, fw);
      if (cow == null) {
        System.err.println("Unsupported output format!");
	return false;
      }

      ChemFile content = (ChemFile)cor.read((ChemObject)new ChemFile());
      fr.close();
      cow.write(content.getChemSequence(0).getChemModel(0).getSetOfMolecules());
      fw.flush();
      fw.close();
    } catch (Exception e) {
      success = false;
      e.printStackTrace();
    }
    return success;
  }

  /**
   * actual program
   */
  public static void main(String[] args) {
    String input_format = "";
    String output_format = "";
    File input;
    File output;
    if (args.length == 4) {
      if (args[0].startsWith("-i")) {
        input_format = args[0].substring(2);
      }
      if (args[1].startsWith("-o")) {
        output_format = args[1].substring(2);
      }
      input = new File(args[2]);
      output = new File(args[3]);

      // do conversion
      FileConvertor fc = new FileConvertor(input_format, output_format);
      if (fc.convert(input, output)) {
        System.out.println("Conversion succeeded!");
      } else {
        System.out.println("Converstion failed!");
      }
    } else {
      System.err.println("syntax: FileConverter -i<format> -o<format> <input> <output>");
      System.exit(1);
    }

  }

  private ChemObjectReader getChemObjectReader(String format, FileReader f) {
    if (format.equalsIgnoreCase("CML")) {
      return new CMLReader(f);
    } else if (format.equalsIgnoreCase("XYZ")) {
      return new XYZReader(f);
    } else if (format.equalsIgnoreCase("MOL")) {
      return new MDLReader(f);
    }
    return null;
  }

  private ChemObjectWriter getChemObjectWriter(String format, FileWriter f) {
    if (format.equalsIgnoreCase("CML")) {
      return new CMLWriter(f);
    } else if (format.equalsIgnoreCase("MOL")) {
      return new MDLWriter(f);
    }
    return null;
  }
}




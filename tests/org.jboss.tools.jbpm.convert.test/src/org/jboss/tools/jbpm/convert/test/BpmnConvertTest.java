package org.jboss.tools.jbpm.convert.test;

import junit.framework.TestCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jbpm.convert.b2j.translate.*;

public class BpmnConvertTest extends TestCase {

	public void testConvert() throws IOException {
		String absolutePath = "";
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			absolutePath = Platform.getBundle(
					"org.jboss.tools.jbpm.convert.test").getLocation()
					.substring(16);
		} else {
			absolutePath = Platform.getBundle(
					"org.jboss.tools.jbpm.convert.test").getLocation()
					.substring(15);
		}
		String bpmnfilePath = absolutePath + "testfile";
		String expectedPath = absolutePath + "resultfile";
		File[] bpmnFiles = listAll(bpmnfilePath);

		BPMN2JPDL jpdltranformer = null;
		GraphicalFileGenerator gpdtranformer = null;
		String str1 = "";
		int count = 0;
		for (int i = 0; i < bpmnFiles.length; i++) {
			String name = bpmnFiles[i].getName();
			String path = bpmnFiles[i].getParentFile().getAbsolutePath();

			String tmpLocation = getTempDir().getAbsolutePath();

			jpdltranformer = new BPMN2JPDL(name, path);
			jpdltranformer.translateToFiles(tmpLocation);

			gpdtranformer = new GraphicalFileGenerator(jpdltranformer.getMap(),
					path, TranslateHelper.getBpmnDiagramName(name));
			gpdtranformer.translateToFiles(tmpLocation);

			try {
				if (!compareWithExpectedResult(tmpLocation + File.separator
						+ "jpdl" + File.separator + name, expectedPath
						+ File.separator + name)) {
					System.out.println("the testing bpmn named  /" + name
							+ " tranformation is failure");
					assertEquals(true, false);

				} else {
					count++;
					assertEquals(true, true);
				}
			} catch (Exception e) {
				System.out.println("the testing bpmn named  /" + str1
						+ " has error");
				e.printStackTrace();
				assertEquals(true, false);
				return;
			}
		}

		System.out.println("the test is over!!!  " + count
				+ " files have been examined.");
	}

	private boolean compareWithExpectedResult(String tmpLocation,
			String expectedPath) throws IOException {
		boolean isTrue = true;

		File tmp = new File(tmpLocation);
		File expectedFile = new File(expectedPath);

		isTrue = compareFile(tmp, expectedFile);

		if (!isTrue) {
			return isTrue;
		}
		return isTrue;
	}

	private boolean compareFile(File result, File expectedFile)
			throws IOException {
		boolean isTrue = true;
		if (result.isFile()) {
			isTrue = compareInputSteam(getInputStream(expectedFile),
					getInputStream(result));
		}
		if (!isTrue) {
			return isTrue;
		}
		if (result.isDirectory()) {
			for (int i = 0; i < result.listFiles().length; i++) {
				String name = result.listFiles()[i].getName();
				File tmp = new File(expectedFile, name);
				isTrue = compareFile(result.listFiles()[i], tmp);
				if (!isTrue) {
					return isTrue;
				}
			}
		}
		return isTrue;
	}

	private boolean compareInputSteam(InputStream expected, InputStream result)
			throws IOException {

		String expectedStr = "";
		String resultStr = "";

		while (true) {
			int i = expected.read();
			int j = result.read();
			expectedStr += (char) i;
			resultStr += (char) j;
			if (i != j) {
				System.err.println(expectedStr);
				System.out.println();
				return false;
			}
			if (i == -1)
				return true;
		}
	}

	private InputStream getInputStream(File file) throws FileNotFoundException {
		InputStream stream = null;
		stream = new FileInputStream(file);
		return stream;
	}

	// return the file array of the directory
	public static File[] listAll(String filename) {
		File file = new File(filename);
		ArrayList<File> list = new ArrayList<File>();
		File[] files;
		if (!file.exists() || file.isFile()) {
			return new File[0];
		}
		list(list, file);
		list.remove(file);
		files = new File[list.size()];
		list.toArray(files);
		return files;
	}

	// add the file to the file array
	private static void list(ArrayList<File> list, File file) {
		if (file.getName().endsWith("bpmn")) {
			list.add(file);
		}
		if (file.isFile()) {
			return;
		}

		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				list(list, files[i]);
			}
		}
	}

	public File getTempDir() {
		File tempdir = new File(System.getProperty("java.io.tmpdir"));
		File tmpdir = new File(tempdir, "tmp" + System.currentTimeMillis());
		if (tmpdir.exists()) {
			System.out.println("This dir is exist");
		}
		tmpdir.mkdirs();
		return tmpdir;
	}
}
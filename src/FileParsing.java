import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public interface FileParsing {
	public void runForParameters(String[] params, String filename);

	static void runForInputFolder(String folderName, FileParsing handler) {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String extension = "";

				int index = listOfFiles[i].getName().lastIndexOf('.');
				if (index > 0) {
					extension = listOfFiles[i].getName().substring(index + 1);
				}

				if (extension.equals("txt")) {
					try {
						BufferedReader reader = new BufferedReader(new FileReader(listOfFiles[i]));

						ArrayList<String> content = new ArrayList<>();
						String line;
						while ((line = reader.readLine()) != null) {
							content.add(line);
						}

						System.out.println("for File \"" + listOfFiles[i].getName() + "\": ");
						handler.runForParameters(content.toArray(new String[0]), listOfFiles[i].getName());

						System.out.println();

						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}

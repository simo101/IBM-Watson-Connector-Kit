// This file was generated by Mendix Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package watsonservices.actions;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.CreateClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import system.proxies.FileDocument;
import watsonservices.proxies.trainingImagesZipFile;

public class TrainNewClassifier extends CustomJavaAction<String>
{
	private String apikey;
	private IMendixObject __VRClassigier;
	private watsonservices.proxies.VRClassification VRClassigier;

	public TrainNewClassifier(IContext context, String apikey, IMendixObject VRClassigier)
	{
		super(context);
		this.apikey = apikey;
		this.__VRClassigier = VRClassigier;
	}

	@Override
	public String executeAction() throws Exception
	{
		this.VRClassigier = __VRClassigier == null ? null : watsonservices.proxies.VRClassification.initialize(getContext(), __VRClassigier);

		// BEGIN USER CODE
		
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_19);
		service.setApiKey(this.apikey);
		
		trainingImagesZipFile posTrainingImagesZipFile = VRClassigier.getpositives_trainingImages();
		FileDocument posZipFileDocument = posTrainingImagesZipFile;
		File posTempFile = new File(Core.getConfiguration().getTempPath() + posZipFileDocument.getName());
		InputStream stream = Core.getFileDocumentContent(getContext(), posTrainingImagesZipFile.getMendixObject());
		Files.copy(stream, posTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		stream.close();
		
		trainingImagesZipFile negTrainingImagesZipFile = VRClassigier.getnegative_trainingImages();
		FileDocument negZipFileDocument = negTrainingImagesZipFile;
		File negTempFile = new File(Core.getConfiguration().getTempPath() + negZipFileDocument.getName());
		stream = Core.getFileDocumentContent(getContext(), negTrainingImagesZipFile.getMendixObject());
		Files.copy(stream, negTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		stream.close();
		
	    CreateClassifierOptions options = new CreateClassifierOptions.Builder().
	    		classifierName(VRClassigier.getclassificationName())
	    		.addClass(posTrainingImagesZipFile.getName() + "_positive_examples", posTempFile)
	    		.negativeExamples(negTempFile)
	    		.build();
	    
		VisualClassifier classifier = service.createClassifier(options).execute();
	    return classifier.getId();

		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public String toString()
	{
		return "TrainNewClassifier";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

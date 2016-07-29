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
import java.util.ArrayList;
import java.util.List;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyVision;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageFace;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageFaces;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;

public class CallAlchemyVision extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private String apiKey;
	private IMendixObject __image;
	private system.proxies.Image image;

	public CallAlchemyVision(IContext context, String apiKey, IMendixObject image)
	{
		super(context);
		this.apiKey = apiKey;
		this.__image = image;
	}

	@Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.image = __image == null ? null : system.proxies.Image.initialize(getContext(), __image);

		// BEGIN USER CODE
		AlchemyVision service = new AlchemyVision();
		service.setApiKey(apiKey);
		
		File tempFile = new File(Core.getConfiguration().getTempPath() + "CallAlchemyVision");
		InputStream stream = Core.getFileDocumentContent(getContext(), __image);
		Files.copy(stream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		stream.close();
	

		ImageFaces faces = service.recognizeFaces(tempFile, true).execute();
		
		
		List<IMendixObject> results = new ArrayList<IMendixObject>();
		
		if(faces != null) {
			for (ImageFace face : faces.getImageFaces()) {
				IMendixObject result = Core.instantiate(getContext(), watsonservices.proxies.Face.entityName);
				result.setValue(getContext(), "Gender", face.getGender().getGender());
				result.setValue(getContext(), "AgeRange", face.getAge().getAgeRange());
				results.add(result);
			}
		}

		return results;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public String toString()
	{
		return "CallAlchemyVision";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

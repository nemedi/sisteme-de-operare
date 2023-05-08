package client;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;

public class TreeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object item) {
		File file = (File) item;
		if (!file.getName().isEmpty()) {
			return file.getName();
		} else {
			return file.getPath();
		}
	}

}

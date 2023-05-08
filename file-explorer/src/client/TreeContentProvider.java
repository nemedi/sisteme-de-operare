package client;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object item) {
		return ((File) item)
				.listFiles(f -> f.isDirectory())
				.length > 0;
	}
	
	@Override
	public Object getParent(Object item) {
		return ((File) item).getParentFile();
	}
	
	@Override
	public Object[] getElements(Object input) {
		return ((File[]) input);
	}
	
	@Override
	public Object[] getChildren(Object item) {
		return ((File) item)
				.listFiles(f -> f.isDirectory());
	}
}

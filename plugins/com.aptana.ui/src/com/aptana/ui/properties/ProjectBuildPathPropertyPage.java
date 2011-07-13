package com.aptana.ui.properties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

import com.aptana.index.core.RebuildIndexJob;

public class ProjectBuildPathPropertyPage extends PropertyPage implements IWorkbenchPropertyPage
{
	private IProject project;
	private CheckboxTableViewer tableViewer;

	/**
	 * ProjectBuildPathPropertyPage
	 */
	public ProjectBuildPathPropertyPage()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		// get project
		project = (IProject) getElement().getAdapter(IResource.class);

		// get entire list and selected items in that list
		List<BuildPathEntry> entries = getBuildPathEntries(project);
		List<BuildPathEntry> selectedEntries = getSelectedBuildPathEntries(project);

		// top-level composite
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		// labels
		setDescription("Project build path for '" + project.getName() + "':");
		Label description = createDescriptionLabel(composite);
		description.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// table composite
		Composite tableComposite = new Composite(composite, SWT.NONE);
		tableComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		tableComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		// table
		tableViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.TOP | SWT.BORDER);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		column1.setText("Library");
		column1.setWidth(165);

		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setText("Path");
		column2.setWidth(350);

		tableViewer.setContentProvider(getContentProvider());
		tableViewer.setLabelProvider(getLabelProvider());
		tableViewer.setComparator(getCompartor());
		tableViewer.setInput(entries);
		tableViewer.setCheckedElements(selectedEntries.toArray());

		return composite;
	}

	/**
	 * @return
	 */
	private ViewerComparator getCompartor()
	{
		return new ViewerComparator()
		{
			public int compare(Viewer viewer, Object e1, Object e2)
			{
				int result = 0;

				if (e1 instanceof BuildPathEntry && e2 instanceof BuildPathEntry)
				{
					BuildPathEntry bpe1 = (BuildPathEntry) e1;
					BuildPathEntry bpe2 = (BuildPathEntry) e2;

					result = bpe1.getDisplayName().compareTo(bpe2.getDisplayName());
				}

				return result;
			}
		};
	}

	/**
	 * getBuildPathEntries
	 * 
	 * @param project
	 * @return
	 */
	private List<BuildPathEntry> getBuildPathEntries(IProject project)
	{
		BuildPathManager manager = BuildPathManager.getInstance();

		return manager.getBuildPaths();
	}

	/**
	 * getSelectedBuildPathEntries
	 * 
	 * @param project
	 * @return
	 */
	private List<BuildPathEntry> getSelectedBuildPathEntries(IProject project)
	{
		BuildPathManager manager = BuildPathManager.getInstance();

		return manager.getSelectedBuildPathEntries(project);
	}

	/**
	 * getContentProvider
	 * 
	 * @return
	 */
	private IStructuredContentProvider getContentProvider()
	{
		return new BaseWorkbenchContentProvider()
		{
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#getChildren(java.lang.Object)
			 */
			@Override
			public Object[] getChildren(Object element)
			{
				if (element instanceof List<?>)
				{
					return ((List<?>) element).toArray();
				}
				else
				{
					return super.getChildren(element);
				}
			}
		};
	}

	/**
	 * getLabelProvider
	 * 
	 * @return
	 */
	private IBaseLabelProvider getLabelProvider()
	{
		return new ITableLabelProvider()
		{
			public void addListener(ILabelProviderListener listener)
			{
			}

			public void dispose()
			{
			}

			public boolean isLabelProperty(Object element, String property)
			{
				return false;
			}

			public void removeListener(ILabelProviderListener listener)
			{
			}

			public Image getColumnImage(Object element, int columnIndex)
			{
				return null;
			}

			public String getColumnText(Object element, int columnIndex)
			{
				String result = null;

				if (element instanceof BuildPathEntry)
				{
					BuildPathEntry entry = (BuildPathEntry) element;

					switch (columnIndex)
					{
						case 0:
							result = entry.getDisplayName();
							break;

						case 1:
							result = entry.getPath().toString();

							if (result != null && result.startsWith("file:"))
							{
								result = result.substring(5);
							}
							break;
					}
				}

				return result;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		Object[] items = tableViewer.getCheckedElements();
		List<BuildPathEntry> entries = new ArrayList<BuildPathEntry>();

		for (Object item : items)
		{
			if (item instanceof BuildPathEntry)
			{
				entries.add((BuildPathEntry) item);
			}
		}

		BuildPathManager manager = BuildPathManager.getInstance();

		// determine if the selection has changed
		Set<BuildPathEntry> currentEntries = new HashSet<BuildPathEntry>(manager.getSelectedBuildPathEntries(project));
		Set<BuildPathEntry> newEntries = new HashSet<BuildPathEntry>(entries);

		if (!newEntries.equals(currentEntries))
		{
			BuildPathManager.getInstance().setSelectedBuildPathEntries(project, entries);

			// rebuild project index
			RebuildIndexJob job = new RebuildIndexJob(project.getLocationURI());

			job.run(new NullProgressMonitor());
		}

		return true;
	}
}

/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.globals.client.editor;

import java.util.Arrays;
import java.util.Collections;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsEditorContent;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.guvnor.common.services.project.client.security.ProjectController;
import org.guvnor.common.services.project.context.ProjectContext;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.validation.ValidationService;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilderImpl;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
import org.kie.workbench.common.widgets.metadata.client.KieEditorWrapperView;
import org.kie.workbench.common.widgets.metadata.client.validation.AssetUpdateValidator;
import org.kie.workbench.common.widgets.metadata.client.widget.OverviewWidgetPresenter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.client.file.popups.DeletePopUpPresenter;
import org.uberfire.ext.editor.commons.client.file.popups.SavePopUpPresenter;
import org.uberfire.ext.editor.commons.client.history.VersionRecordManager;
import org.uberfire.ext.editor.commons.client.menu.BasicFileMenuBuilder;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.ParameterizedCommand;
import org.uberfire.workbench.model.menu.MenuItem;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class GlobalsEditorPresenterTest {

    @Mock
    private GlobalsEditorView view;

    @Mock
    private GlobalsEditorService globalsEditorService;

    @Mock
    private KieEditorWrapperView kieView;

    @Mock
    private VersionRecordManager versionRecordManager;

    @Mock
    private OverviewWidgetPresenter overviewWidget;

    @Mock
    private ValidationService validationService;

    @Mock
    private SavePopUpPresenter savePopUpPresenter;

    @Mock
    private DeletePopUpPresenter deletePopUpPresenter;

    @Mock
    private ValidationPopup validationPopup;

    @Mock
    private BasicFileMenuBuilder menuBuilder;

    @Spy
    @InjectMocks
    private FileMenuBuilderImpl fileMenuBuilder;

    @Mock
    private ProjectController projectController;

    @Mock
    private ProjectContext workbenchContext;

    @Mock
    private AssetUpdateValidator assetUpdateValidator;

    private GlobalsEditorPresenter presenter;

    @Before
    public void setUp() {
        presenter = new GlobalsEditorPresenter(view) {
            {
                kieView = GlobalsEditorPresenterTest.this.kieView;
                globalsEditorService = new CallerMock<>(GlobalsEditorPresenterTest.this.globalsEditorService);
                versionRecordManager = GlobalsEditorPresenterTest.this.versionRecordManager;
                overviewWidget = GlobalsEditorPresenterTest.this.overviewWidget;
                validationService = new CallerMock<>(GlobalsEditorPresenterTest.this.validationService);
                savePopUpPresenter = GlobalsEditorPresenterTest.this.savePopUpPresenter;
                deletePopUpPresenter = GlobalsEditorPresenterTest.this.deletePopUpPresenter;
                validationPopup = GlobalsEditorPresenterTest.this.validationPopup;
                fileMenuBuilder = GlobalsEditorPresenterTest.this.fileMenuBuilder;
                projectController = GlobalsEditorPresenterTest.this.projectController;
                workbenchContext = GlobalsEditorPresenterTest.this.workbenchContext;
                versionRecordManager = GlobalsEditorPresenterTest.this.versionRecordManager;
                assetUpdateValidator = GlobalsEditorPresenterTest.this.assetUpdateValidator;
            }
        };
    }

    @Test
    public void loadContent() {
        presenter.loadContent();

        verify(view,
               times(1)).showLoading();
        verify(globalsEditorService,
               times(1)).loadContent(any(Path.class));

        when(versionRecordManager.getCurrentPath()).thenReturn(mock(ObservablePath.class));

        // Emulate the globals being successfully loaded
        GlobalsEditorContent globalsEditorContent = mock(GlobalsEditorContent.class);
        Overview overview = mock(Overview.class);
        when(overview.getMetadata()).thenReturn(mock(Metadata.class));
        when(globalsEditorContent.getOverview()).thenReturn(overview);
        GlobalsModel globalsModel = mock(GlobalsModel.class);
        when(globalsEditorContent.getModel()).thenReturn(globalsModel);
        presenter.getModelSuccessCallback().callback(globalsEditorContent);

        verify(view,
               times(1)).setContent(anyListOf(Global.class),
                                    anyListOf(String.class),
                                    anyBoolean(),
                                    anyBoolean());
        verify(view,
               times(1)).hideBusyIndicator();
    }

    @Test
    public void saveNoValidationErrors() {
        when(validationService.validateForSave(any(Path.class),
                                               any(GlobalsModel.class))).thenReturn(Collections.emptyList());

        presenter.save();

        verify(validationService,
               times(1)).validateForSave(any(Path.class),
                                         any(GlobalsModel.class));
        verify(savePopUpPresenter,
               times(1)).show(any(Path.class),
                              any(ParameterizedCommand.class));
    }

    @Test
    public void saveValidationErrors() {
        when(validationService.validateForSave(any(Path.class),
                                               any(GlobalsModel.class))).thenReturn(Arrays.asList(new ValidationMessage()));

        presenter.save();

        verify(validationService,
               times(1)).validateForSave(any(Path.class),
                                         any(GlobalsModel.class));
        verify(validationPopup,
               times(1)).showSaveValidationMessages(any(Command.class),
                                                    any(Command.class),
                                                    anyListOf(ValidationMessage.class));
    }

    @Test
    public void deleteNoValidationErrors() {
        when(validationService.validateForDelete(any(Path.class))).thenReturn(Collections.emptyList());

        presenter.onDelete();

        verify(validationService,
               times(1)).validateForDelete(any(Path.class));
        verify(deletePopUpPresenter,
               times(1)).show(eq(assetUpdateValidator),
                              any(ParameterizedCommand.class));
    }

    @Test
    public void deleteValidationErrors() {
        when(validationService.validateForDelete(any(Path.class))).thenReturn(Arrays.asList(new ValidationMessage()));

        presenter.onDelete();

        verify(validationService,
               times(1)).validateForDelete(any(Path.class));
        verify(validationPopup,
               times(1)).showDeleteValidationMessages(any(Command.class),
                                                      any(Command.class),
                                                      anyListOf(ValidationMessage.class));
    }

    @Test
    public void testMakeMenuBar() {
        doReturn(mock(Project.class)).when(workbenchContext).getActiveProject();
        doReturn(true).when(projectController).canUpdateProject(any());

        presenter.makeMenuBar();

        verify(fileMenuBuilder).addSave(any(MenuItem.class));
        verify(fileMenuBuilder).addCopy(any(Path.class),
                                        any(AssetUpdateValidator.class));
//        verify(fileMenuBuilder).addRename(any(Path.class),
//                                          any(AssetUpdateValidator.class));
        verify(fileMenuBuilder).addDelete(any(Command.class));
    }

    @Test
    public void testMakeMenuBarWithoutUpdateProjectPermission() {
        doReturn(mock(Project.class)).when(workbenchContext).getActiveProject();
        doReturn(false).when(projectController).canUpdateProject(any());

        presenter.makeMenuBar();

        verify(fileMenuBuilder,
               never()).addSave(any(MenuItem.class));
        verify(fileMenuBuilder,
               never()).addCopy(any(Path.class),
                                any(AssetUpdateValidator.class));
//        verify(fileMenuBuilder,
//               never()).addRename(any(Path.class),
//                                  any(AssetUpdateValidator.class));
        verify(fileMenuBuilder,
               never()).addDelete(any(Command.class));
    }
}

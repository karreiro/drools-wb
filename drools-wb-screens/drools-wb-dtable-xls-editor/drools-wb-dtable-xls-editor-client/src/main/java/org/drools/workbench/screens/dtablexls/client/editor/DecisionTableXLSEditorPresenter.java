/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.dtablexls.client.editor;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.models.guided.dtable.shared.conversion.ConversionMessage;
import org.drools.workbench.models.guided.dtable.shared.conversion.ConversionResult;
import org.drools.workbench.screens.dtablexls.client.resources.i18n.DecisionTableXLSEditorConstants;
import org.drools.workbench.screens.dtablexls.client.type.DecisionTableXLSResourceType;
import org.drools.workbench.screens.dtablexls.client.type.DecisionTableXLSXResourceType;
import org.drools.workbench.screens.dtablexls.client.widgets.ConversionMessageWidget;
import org.drools.workbench.screens.dtablexls.client.widgets.PopupListWidget;
import org.drools.workbench.screens.dtablexls.service.DecisionTableXLSContent;
import org.drools.workbench.screens.dtablexls.service.DecisionTableXLSService;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.metadata.client.KieEditor;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartTitleDecoration;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.workbench.type.ClientResourceType;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;
import org.uberfire.workbench.model.menu.impl.BaseMenuCustom;

import static org.uberfire.ext.widgets.common.client.common.ConcurrentChangePopup.newConcurrentUpdate;

@Dependent
@WorkbenchEditor(identifier = "DecisionTableXLSEditor", supportedTypes = {DecisionTableXLSResourceType.class, DecisionTableXLSXResourceType.class})
public class DecisionTableXLSEditorPresenter
        extends KieEditor<DecisionTableXLSContent>
        implements DecisionTableXLSEditorView.Presenter {

    private Caller<DecisionTableXLSService> decisionTableXLSService;

    private Event<NotificationEvent> notification;

    private ValidationPopup validationPopup;

    private BusyIndicatorView busyIndicatorView;

    private DecisionTableXLSResourceType decisionTableXLSResourceType;

    private DecisionTableXLSXResourceType decisionTableXLSXResourceType;

    private DecisionTableXLSEditorView view;

    @Inject
    public DecisionTableXLSEditorPresenter(final DecisionTableXLSEditorView baseView,
                                           final DecisionTableXLSResourceType decisionTableXLSResourceType,
                                           final DecisionTableXLSXResourceType decisionTableXLSXResourceType,
                                           final BusyIndicatorView busyIndicatorView,
                                           final Event<NotificationEvent> notification,
                                           final ValidationPopup validationPopup,
                                           final Caller<DecisionTableXLSService> decisionTableXLSService) {
        super(baseView);
        view = baseView;
        this.decisionTableXLSResourceType = decisionTableXLSResourceType;
        this.decisionTableXLSXResourceType = decisionTableXLSXResourceType;
        this.decisionTableXLSService = decisionTableXLSService;
        this.busyIndicatorView = busyIndicatorView;
        this.notification = notification;
        this.validationPopup = validationPopup;
    }

    @OnStartup
    public void onStartup(final ObservablePath path,
                          final PlaceRequest place) {
        ClientResourceType type = getType(path);
        super.init(path,
                   place,
                   type);
        view.init(this);
        view.setupUploadWidget(type);
    }

    @Override
    public void onUpload() {
        if (concurrentUpdateSessionInfo != null) {
            busyIndicatorView.hideBusyIndicator();
            showConcurrentUpdateError();
        } else {
            submit();
        }
    }

    @Override
    public void onUploadSuccess() {
        versionRecordManager.reloadVersions(versionRecordManager.getPathToLatest());
    }

    void submit() {
        view.submit(versionRecordManager.getCurrentPath());
        concurrentUpdateSessionInfo = null;
    }

    void showConcurrentUpdateError() {
        newConcurrentUpdate(concurrentUpdateSessionInfo.getPath(),
                            concurrentUpdateSessionInfo.getIdentity(),
                            new Command() {
                                @Override
                                public void execute() {
                                    submit();
                                }
                            },
                            new Command() {
                                @Override
                                public void execute() {
                                    //cancel?
                                }
                            },
                            new Command() {
                                @Override
                                public void execute() {
                                    reload();
                                    concurrentUpdateSessionInfo = null;
                                }
                            }
        ).show();
    }

    ObservablePath.OnConcurrentUpdateEvent getConcurrentUpdateSessionInfo() {
        return this.concurrentUpdateSessionInfo;
    }

    private ClientResourceType getType(ObservablePath path) {
        if (decisionTableXLSXResourceType.accept(path)) {
            return decisionTableXLSXResourceType;
        } else {
            return decisionTableXLSResourceType;
        }
    }

    @Override
    protected void loadContent() {
        decisionTableXLSService.call(getModelSuccessCallback(),
                                     getNoSuchFileExceptionErrorCallback()).loadContent(versionRecordManager.getCurrentPath());
    }

    private RemoteCallback<DecisionTableXLSContent> getModelSuccessCallback() {
        return new RemoteCallback<DecisionTableXLSContent>() {
            @Override
            public void callback(final DecisionTableXLSContent content) {
                resetEditorPages(content.getOverview());
                addSourcePage();

                view.setPath(versionRecordManager.getCurrentPath());
                view.setReadOnly(isReadOnly);
            }
        };
    }

    @Override
    public void onSourceTabSelected() {
        decisionTableXLSService.call(
                new RemoteCallback<String>() {
                    @Override
                    public void callback(String source) {
                        updateSource(source);
                    }
                },
                getCouldNotGenerateSourceErrorCallback()
        ).getSource(versionRecordManager.getCurrentPath());
    }

    @Override
    protected Command onValidate() {
        return new Command() {
            @Override
            public void execute() {
                decisionTableXLSService.call(new RemoteCallback<List<ValidationMessage>>() {
                    @Override
                    public void callback(final List<ValidationMessage> results) {
                        if (results == null || results.isEmpty()) {
                            notification.fire(new NotificationEvent(CommonConstants.INSTANCE.ItemValidatedSuccessfully(),
                                                                    NotificationEvent.NotificationType.SUCCESS));
                        } else {
                            validationPopup.showMessages(results);
                        }
                    }
                }).validate(versionRecordManager.getCurrentPath(),
                            versionRecordManager.getCurrentPath());
            }
        };
    }

    @Override
    protected void makeMenuBar() {
        if (canUpdateProject()) {
            fileMenuBuilder
                    .addCopy(versionRecordManager.getCurrentPath(),
                             assetUpdateValidator)
                    .addRename(versionRecordManager.getPathToLatest(),
                               assetUpdateValidator)
                    .addDelete(versionRecordManager.getPathToLatest(),
                               assetUpdateValidator);
        }

        fileMenuBuilder
                .addValidate(onValidate())
                .addNewTopLevelMenu(getConvertMenu())
                .addNewTopLevelMenu(versionRecordManager.buildMenu());
    }

    protected MenuItem getConvertMenu() {
        return new MenuFactory.CustomMenuBuilder() {

            private Button button = new Button(DecisionTableXLSEditorConstants.INSTANCE.Convert()) {{
                setSize(ButtonSize.SMALL);
                addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                        convert();
                    }
                });
            }};

            @Override
            public void push(MenuFactory.CustomMenuBuilder element) {
                //Nothing to do. We don't support nested menus
            }

            @Override
            public MenuItem build() {
                return new BaseMenuCustom<IsWidget>() {
                    @Override
                    public IsWidget build() {
                        return button;
                    }

                    @Override
                    public boolean isEnabled() {
                        return button.isEnabled();
                    }

                    @Override
                    public void setEnabled(boolean enabled) {
                        button.setEnabled(enabled);
                    }
                };
            }
        }.build();
    }

    @OnClose
    public void onClose() {
        this.versionRecordManager.clear();
    }

    @WorkbenchPartTitleDecoration
    public IsWidget getTitle() {
        return super.getTitle();
    }

    @WorkbenchPartTitle
    public String getTitleText() {
        return super.getTitleText();
    }

    @WorkbenchPartView
    public IsWidget getWidget() {
        return super.getWidget();
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    private void convert() {
        busyIndicatorView.showBusyIndicator(DecisionTableXLSEditorConstants.INSTANCE.Converting());
        decisionTableXLSService.call(new RemoteCallback<ConversionResult>() {
            @Override
            public void callback(final ConversionResult response) {
                busyIndicatorView.hideBusyIndicator();
                if (response.getMessages().size() > 0) {
                    final PopupListWidget popup = new PopupListWidget();
                    for (ConversionMessage message : response.getMessages()) {
                        popup.addListItem(new ConversionMessageWidget(message));
                    }
                    popup.show();
                }
            }
        }).convert(versionRecordManager.getCurrentPath());
    }
}

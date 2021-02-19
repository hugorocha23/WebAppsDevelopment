import React from 'react'
import { BrowserRouter, Route, Redirect, Switch } from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

import Header from './header'
import Home from './home'
import ItemList from './item-list'
import ItemDetail from './item-detail'
import ItemCreate from './item-create'
import ItemEdit from './item-edit'
import ChecklistTemplateList from './checklist-template-list'
import ChecklistTemplateDetail from './checklist-template-detail'
import ChecklistTemplateCreate from './checklist-template-create'
import ChecklistTemplateEdit from './checklist-template-edit'
import ChecklistList from './checklist-list'
import ChecklistDetail from './checklist-detail'
import ChecklistCreate from './checklist-create'
import ChecklistEdit from './checklist-edit'
import ChecklistTemplateCreateChecklist from './checklist-create-from-template'
import TemplateItemList from './template-item-list'
import TemplateItemDetail from './template-item-detail'
import TemplateItemCreate from './template-item-create'
import TemplateItemEdit from './template-item-edit'

const login = new URITemplate('/login/{url}')

const checklists = new URITemplate('/checklist/{url}')
const checklistDetail = new URITemplate('/checklist/detail/{url}')
const checklistCreate = new URITemplate('/checklist/create/{url}')
const checklistEdit = new URITemplate('/checklist/edit/{url}')

const checklistCreateTemplate = new URITemplate('/checklist-template/create-checklist/{url}')

const checklistTemplates = new URITemplate('/checklist-template/{url}')
const checklistTemplateDetail = new URITemplate('/checklist-template/detail/{url}')
const checklistTemplateCreate = new URITemplate('/checklist-template/create/{url}')
const checklistTemplateEdit = new URITemplate('/checklist-template/edit/{url}')

const checklistItems = new URITemplate('/checklist/items/{url}')
const checklistItemDetail = new URITemplate('/checklist/items/detail/{url}')
const checklistItemCreate = new URITemplate('/checklist/items/create/{url}')
const checklistItemEdit = new URITemplate('/checklist/items/edit/{url}')

const checklistTemplateItems = new URITemplate('/checklist-template/items/{url}')
const checklistTemplateItemDetail = new URITemplate('/checklist-template/items/detail/{url}')
const checklistTemplateItemCreate = new URITemplate('/checklist-template/items/create/{url}')
const checklistTemplateItemEdit = new URITemplate('/checklist-template/items/edit/{url}')

const apiHome = 'http://35.230.151.142/api'
const homeURI = new URITemplate(`/home/{url}`).expand({ url: apiHome })
const apiUrlTemplate = new URITemplate(`${apiHome}/{url}`)

const homeApiRoutes = {
    '/checklist': href => checklists.expand({ url: apiHome + href }),
    '/checklist-template': href => checklistTemplates.expand({ url: apiHome + href }),
    '/get-user' : href => login.expand({ url: apiHome + href})
}

import { UserManager } from 'oidc-client'

var mitreIDsettings = {
  authority: 'http://35.197.234.113/openid-connect-server-webapp',
  client_id: 'client',
  redirect_uri: 'http://35.230.151.142/redirect.html',
  popup_redirect_uri: 'http://35.230.151.142/redirect.html',
  post_logout_redirect_uri: 'http://35.230.151.142/user-manager-sample.html',
  response_type: 'token id_token',
  scope: 'openid email profile',
  silent_redirect_uri: 'http://35.230.151.142/user-manager-sample-silent.html',
  automaticSilentRenew: true,
  filterProtocolClaims: true,
  loadUserInfo: true
}

const mgr = new UserManager(mitreIDsettings)

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = { user: null }
        this.protectedRoute = this.protectedRoute.bind(this)
        this.loginMitreId = this.loginMitreId.bind(this)
        this.removeUser = this.removeUser.bind(this)
    }

    protectedRoute({ render, ...rest }) {
        let redirectLink = { pathname: login.expand({ url: apiHome + this.state.loginUrl }) }
        if (!this.state.loginUrl) {
            redirectLink.pathname = homeURI
            redirectLink.state = { redirectPath: location.pathname }
        }

        return (
            <Route {...rest} render={props => (
                this.state.user
                    ? render(props)
                    : <Redirect to={redirectLink} />
            )} />
        )
    }

    componentDidMount() {
        console.log('Inside App Mount')
    }

    componentWillUnmount() {
        console.log('Inside App Unmount')
    }

    loginMitreId() {
        mgr.signinPopup()
            .then(user => {
              this.setState({ user: user })
        })
    }

    removeUser(history) {
        this.setState({user: null})
        history.replace({ pathname: homeURI })
    }

    render() {
        return (
            <BrowserRouter >
                <div>
                    <Header
                        user={this.state.user}
                        onLogin={this.loginMitreId}
                        onLogout={this.removeUser}
                    />

                    <Switch>

                        <Route exact path='/' render={() => <Redirect to={homeURI} />} />
                        <Route path='/home/:url'
                            render={({ match, history }) => (
                                <Home
                                    url={URI.decode(match.params.url)}
                                    onSelectDetail={(item, href) => history.push(homeApiRoutes[item](href))}
                                />
                            )}
                        />

                        {/*checklist create */}
                        <this.protectedRoute exact path='/checklist/create/:url'
                            render={({ match, history }) => (
                                <ChecklistCreate
                                    url={URI.decode(match.params.url)}
                                    onSubmit={href => history.push(checklistDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist create from template */}
                        <this.protectedRoute exact path='/checklist-template/create-checklist/:url'
                            render={({ match, history }) => (
                                <ChecklistTemplateCreateChecklist
                                    url={URI.decode(match.params.url)}
                                    onSubmit={href => history.push(checklistDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist*/}
                        <this.protectedRoute exact path='/checklist/:url'
                            render={({ match, history }) => (
                                <ChecklistList
                                    url={URI.decode(match.params.url)}
                                    getApiUrl={(url) => apiUrlTemplate.expand({ url })}
                                    onSelectDetail={href => history.push(checklistDetail.expand({ url: apiHome + href }))}
                                    onSelectCreate={href => history.push(checklistCreate.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />
                        {/*checklist detail*/}
                        <this.protectedRoute exact path='/checklist/detail/:url'
                            render={({ match, history }) => (
                                <ChecklistDetail
                                    url={URI.decode(match.params.url)}
                                    onSelectEdit={href => history.push(checklistEdit.expand({ url: apiHome + href }))}
                                    onSelectItems={href => history.push(checklistItems.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist edit */}
                        <this.protectedRoute exact path='/checklist/edit/:url'
                            render={({ match, history }) => (
                                <ChecklistEdit
                                    url={URI.decode(match.params.url)}
                                    onSave={href => history.push(checklistDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist template create */}
                        <this.protectedRoute exact path='/checklist-template/create/:url'
                            render={({ match, history }) => (
                                <ChecklistTemplateCreate
                                    url={URI.decode(match.params.url)}
                                    onSubmit={href => history.push(checklistTemplateDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist template */}
                        <this.protectedRoute exact path='/checklist-template/:url'
                            render={({ match, history }) => (
                                <ChecklistTemplateList
                                    url={URI.decode(match.params.url)}
                                    getApiUrl={(url) => apiUrlTemplate.expand({ url })}
                                    onSelectDetail={href => history.push(checklistTemplateDetail.expand({ url: apiHome + href }))}
                                    onSelectCreate={href => history.push(checklistTemplateCreate.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist template detail */}
                        <this.protectedRoute exact path='/checklist-template/detail/:url'
                            render={({ match, history }) => (
                                <ChecklistTemplateDetail
                                    url={URI.decode(match.params.url)}
                                    onSelectItems={href => history.push(checklistTemplateItems.expand({ url: apiHome + href }))}
                                    onSelectEdit={href => history.push(checklistTemplateEdit.expand({ url: apiHome + href }))}
                                    onSelectCreateChecklist={href => history.push(checklistCreateTemplate.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />
                        {/*checklist template edit */}
                        <this.protectedRoute exact path='/checklist-template/edit/:url'
                            render={({ match, history }) => (
                                <ChecklistTemplateEdit
                                    url={URI.decode(match.params.url)}
                                    onSave={href => history.push(checklistTemplateDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist items */}
                        <this.protectedRoute exact path='/checklist/items/:url'
                            render={({ match, history }) => (
                                <ItemList
                                    url={URI.decode(match.params.url)}
                                    getApiUrl={(url) => apiUrlTemplate.expand({ url })}
                                    onSelectDetail={href => history.push(checklistItemDetail.expand({ url: apiHome + href }))}
                                    onSelectCreate={href => history.push(checklistItemCreate.expand({ url: apiHome + href }))}
                                    onClickChecklist={href => history.push(checklistDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist item detail */}
                        <this.protectedRoute exact path='/checklist/items/detail/:url'
                            render={({ match, history }) => (
                                <ItemDetail
                                    url={URI.decode(match.params.url)}
                                    onSelectEdit={href => history.push(checklistItemEdit.expand({ url: apiHome + href }))}
                                    onSelectPrev={href => history.push(checklistItemDetail.expand({ url: apiHome + href }))}
                                    onSelectNext={href => history.push(checklistItemDetail.expand({ url: apiHome + href }))}
                                    onClickItems={href => history.push(checklistItems.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist item create */}
                        <this.protectedRoute exact path='/checklist/items/create/:url'
                            render={({ match, history }) => (
                                <ItemCreate
                                    url={URI.decode(match.params.url)}
                                    onSubmit={href => history.push(checklistItemDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*checklist item edit */}
                        <this.protectedRoute exact path='/checklist/items/edit/:url'
                            render={({ match, history }) => (
                                <ItemEdit
                                    url={URI.decode(match.params.url)}
                                    onSave={href => history.push(checklistItemDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*template items */}
                        <this.protectedRoute exact path='/checklist-template/items/:url'
                            render={({ match, history }) => (
                                <TemplateItemList
                                    url={URI.decode(match.params.url)}
                                    getApiUrl={(url) => apiUrlTemplate.expand({ url })}
                                    onSelectDetail={href => history.push(checklistTemplateItemDetail.expand({ url: apiHome + href }))}
                                    onSelectCreate={href => history.push(checklistTemplateItemCreate.expand({ url: apiHome + href }))}
                                    onClickChecklist={href => history.push(checklistTemplateDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*template item detail */}
                        <this.protectedRoute exact path='/checklist-template/items/detail/:url'
                            render={({ match, history }) => (
                                <TemplateItemDetail
                                    url={URI.decode(match.params.url)}
                                    onSelectEdit={href => history.push(checklistTemplateItemEdit.expand({ url: apiHome + href }))}
                                    onSelectPrev={href => history.push(checklistTemplateItemDetail.expand({ url: apiHome + href }))}
                                    onSelectNext={href => history.push(checklistTemplateItemDetail.expand({ url: apiHome + href }))}
                                    onClickItems={href => history.push(checklistTemplateItems.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*template item create */}
                        <this.protectedRoute exact path='/checklist-template/items/create/:url'
                            render={({ match, history }) => (
                                <TemplateItemCreate
                                    url={URI.decode(match.params.url)}
                                    onSubmit={href => history.push(checklistTemplateItemDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        {/*template item edit */}
                        <this.protectedRoute exact path='/checklist-template/items/edit/:url'
                            render={({ match, history }) => (
                                <TemplateItemEdit
                                    url={URI.decode(match.params.url)}
                                    onSave={href => history.push(checklistTemplateItemDetail.expand({ url: apiHome + href }))}
                                    authorization={`${this.state.user.token_type} ${this.state.user.access_token}`}
                                />
                            )}
                        />

                        <Route path='/' render={() =>
                            <div>
                                {console.log('Inside 404 Route')}
                                <h2>Route not found</h2>
                            </div>
                        } />
                    </Switch>
                </div>
            </BrowserRouter>
        )
    }
}

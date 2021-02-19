import React from 'react'
import URI from 'urijs'

import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import Paginator from './paginator'
import StateFilter from './state-filter'
import { deleteRequest } from '../http-request'

export default class extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            url: props.url,
            onSelectDetail: props.onSelectDetail,
            onSelectCreate: props.onSelectCreate,
            authorization: props.authorization,
            getApiUrl: props.getApiUrl,
            key: Math.random()
        }
        this.handleDelete = this.handleDelete.bind(this)
    }

    render() {
        return (
            <div key={this.state.key}>
                <HttpGet
                    url={this.state.url}
                    options={
                        {
                            headers: {
                                Authorization: this.state.authorization
                            }
                        }
                    }
                    render={result => (
                        <div>
                            {this.state.error ?
                                <p style={{ color: 'red' }}> {this.state.error.message} </p>
                                : ''
                            }
                            <Paginator response={result.response} onChange={url => result.setUrl(URI.decode(this.state.getApiUrl(url)))} />
                            <HttpGetSwitch result={result}
                                onJson={json => (
                                    <div>
                                        <ul>
                                            {json.items.map(item => (
                                                <li key={item.href}>
                                                    {item.data.map(information => (<p>{information.prompt} : {information.value}</p>))}
                                                    <button onClick={
                                                        () => this.state.onSelectDetail(
                                                            item.href
                                                        )}>
                                                        Detail
                                                    </button>
                                                    <button onClick={
                                                        () => this.handleDelete(
                                                            this.state.url.replace(json.href, item.href),
                                                            item.href
                                                        )}>
                                                        Delete
                                                    </button>
                                                </li>
                                            ))}
                                        </ul>
                                        <button onClick={() => this.state.onSelectCreate(json.href)}>Create Checklist Template</button>
                                    </div>

                                )}
                            />
                        </div>
                    )} />
            </div>
        )
    }

    handleDelete(url, href) {
        const request = deleteRequest(url, { Authorization: this.state.authorization })
        fetch(request)
            .then(resp => {
                if (resp.status !== 204) {
                    resp.json().then(err => {
                        this.setError(err.detail)
                    })
                    return
                }
                this.setState(oldState => {
                    const newState = oldState
                    newState.key = Math.random()
                    return newState
                })
            })
            .catch(err => {
                this.setError(err.message)
            })
    }

    setError(msg) {
        this.setState(oldState => {
            const newState = oldState
            newState.error = { message: msg }
            return newState
        })
    }
}
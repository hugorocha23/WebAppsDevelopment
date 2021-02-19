import React from 'react'
import fetch from 'isomorphic-fetch'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import { postRequest } from '../http-request'

class Create extends React.Component {
    constructor(props) {
        super(props)
        const form = {}
        props.templateForm.forEach(item => {
            form[item.name] = { value: item.value, prompt: item.prompt }
        })
        this.state = { 
            url: props.url, 
            form: form, 
            onSubmit: props.onSubmit, 
            authorization: props.authorization, 
            error: null
        }
        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleChange(event) {
        event.persist()
        this.setState(oldState => {
            const newState = oldState
            newState.form[event.target.name].value = event.target.value
            return newState
        })
    }

    handleSubmit(event) {
        const body = {}
        Object.keys(this.state.form).forEach(key => {
            const value = this.state.form[key].value
            if (value !== '') {
                body[key] = this.state.form[key].value
            }
        })
        const request = postRequest(this.state.url, body, { 
            'Content-Type': 'application/json', 
            Authorization: this.state.authorization 
        })
        fetch(request)
            .then(resp => {
                if (resp.status >= 400 && resp.status < 500) {
                    resp.json().then(err => {
                        this.setError(err.detail)
                    })
                    return
                }
                resp.text().then(res => {
                    this.state.onSubmit(res)
                })
            })
            .catch(err => {
                this.setError(err.message)
            })
    }

    render() {
        const form = this.state.form
        return (
            <div>
                <h2> Create new Item </h2>
                {this.state.error ?
                    <p style={{ color: 'red' }}> {this.state.error.message} </p>
                    : ''
                }
                {Object.keys(form).map(key => (
                    <div>
                        {form[key].prompt}: <input type="text" value={form[key].value}
                            onChange={this.handleChange} name={key} />
                        <br />
                        <br />
                    </div>
                ))}
                <button type="submit" onClick={this.handleSubmit}>Submit</button>
            </div>
        )
    }

    setError(msg) {
        this.setState(oldState => {
            const newState = oldState
            newState.error = { message: msg }
            return newState
        })
    }
}

export default ({ url, authorization, onSubmit }) => (
    <HttpGet
        url={url}
        options={
            {
                headers: {
                    Authorization: authorization
                }
            }
        }
        render={(result) => (
            <HttpGetSwitch result={result} onJson={(json) => {
                return (
                    <Create
                        url={url}
                        authorization={authorization}
                        onSubmit={onSubmit}
                        templateForm={json.template.data}
                    />
                )
            }} />
        )}
    />
)
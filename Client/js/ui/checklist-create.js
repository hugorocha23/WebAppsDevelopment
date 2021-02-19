import React from 'react'
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
        this.state = { url: props.url, form: form, onSubmit: props.onSubmit, authorization: props.authorization }
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

    handleSubmit() {
        const body = {}
        const form = this.state.form
        Object.keys(form).forEach(key => {
            if (form[key].value !== '') {
                body[key] = form[key].value
            }
        })
        const request = postRequest(
            this.state.url,
            body,
            {
                'Content-Type': 'application/json',
                Authorization: this.state.authorization
            }
        )
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

    setError(msg) {
        this.setState(oldState => {
            const newState = oldState
            newState.error = { message: msg }
            return newState
        })
    }

    render() {
        const form = this.state.form
        return (
            <div>
                {this.state.error ?
                    <p style={{ color: 'red' }}> {this.state.error.message} </p>
                    : ''
                }
                <h2> Create new Checklist </h2>
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
}

export default ({ url, authorization, onSubmit }) => (
    <HttpGet
        url={url}
        options={
            {
                headers: {
                    Authorization: authorization,
                    Accept: '*/*'
                }
            }
        }
        render={result => (
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
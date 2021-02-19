import React from 'react'
import fetch from 'isomorphic-fetch'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import { putRequest } from '../http-request'

const textareaStyle = {
	width: '500px',
	height: '300px'
}

class Edit extends React.Component {
	constructor(props) {
		super(props)
		this.showCheckbox = this.showCheckbox.bind(this)
		this.showTextbox = this.showTextbox.bind(this)
		const action = props.action
		const item = props.item
		action.fields = action.fields.map(field => {
			field.value = item[field.name] || ''
			return field
		})
		this.state = {
			url: props.url,
			href: props.hrefItem,
			onSave: props.onSave,
			authorization: props.authorization,
			action: action,
			alreadyCompleted: props.item.state === 'completed',
			inputTypes : {
				'boolean': this.showCheckbox,
				'text': this.showTextbox
			},
			error: null
		}
		this.handleTextboxChange = this.handleTextboxChange.bind(this)
		this.handleCheckboxChange = this.handleCheckboxChange.bind(this)
		this.handleSave = this.handleSave.bind(this)
		this.handleCancel = this.handleCancel.bind(this)
		
	}

	handleTextboxChange(event) {
		event.persist()
		this.setState(oldState => {
			const newState = oldState
			newState.action.fields.find(field => field.name === event.target.name).value = event.target.value
			return newState
		})
	}

	handleCheckboxChange(event) {
		event.persist()
		this.setState(oldState => {
			const newState = oldState
			newState.action.fields.find(field => field.name === event.target.name).value = !newState.action.fields.find(field => field.name === event.target.name).value
			return newState
		})
	}

	handleCancel() {
		this.state.onSave(this.state.href)
	}

	handleSave() {
		const item = {}
		this.state.action.fields.forEach(field => {
			item[field.name] = field.value
		})
		const request = putRequest(this.state.url, item, {
			'Content-Type': this.state.action.type,
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
				this.state.onSave(this.state.href)
			})
			.catch(err => {
				this.setError(err.message)
			})
	}

	render() {
		return (
			<div>
				<h2> {this.state.action.title} </h2>
				{this.state.error ?
					<p style={{ color: 'red' }}> {this.state.error.message} </p>
					: ''
				}
				<dl>
					{this.state.action.fields.map(field => this.state.inputTypes[field.type](field))}
				</dl>
				<button onClick={this.handleSave}>Save</button>
				<button onClick={this.handleCancel}>Cancel</button>
			</div>
		)
	}

	showCheckbox(field) {
		return (
			<div>
				<input type='checkbox' name={field.name} value={field.value === 'true'} onChange={this.handleCheckboxChange} disabled={this.state.alreadyCompleted}/>
				{field.title}
			</div>
		)
	}

	showTextbox(field) {
		return (
			<div>
				<dt>{field.title}</dt>
				<dd><textarea name={field.name} value={field.value} onChange={this.handleTextboxChange} style={textareaStyle} /></dd>
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

export default ({ url, authorization, onSave }) => (
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
		render={(result) => (
			<HttpGetSwitch result={result} onJson={(json) => {
				const action = json.actions.find(item => item.name === 'edit-checklist-item')
				const hrefSelf = json.links.find(item => item.rel.includes('self')).href
				return (
					<Edit
						url={url.replace(hrefSelf, action.href)}
						hrefItem={hrefSelf}
						item={json.properties}
						authorization={authorization}
						onSave={onSave}
						action={action}
					/>
				)
			}} />
		)}
	/>
)

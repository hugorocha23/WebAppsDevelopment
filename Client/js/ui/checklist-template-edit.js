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
		this.state = {
			url: props.url,
			href: props.hrefChecklist,
			item: { name: props.item.name, description: props.item.description },
			onSave: props.onSave,
			authorization: props.authorization,
			action: props.action
		}
		this.handleDescriptionChange = this.handleDescriptionChange.bind(this)
		this.handleSave = this.handleSave.bind(this)
		this.handleCancel = this.handleCancel.bind(this)
	}

	handleDescriptionChange(event) {
		event.persist()
		this.setState(oldState => {
			const newState = oldState
			newState.item.description = event.target.value
			return newState
		})
	}

	handleSave() {
		const request = putRequest(
			this.state.url,
			{
				description: this.state.item.description
			},
			{
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
			.catch(error => {
				this.setState({ error: error })
			})
	}

	handleCancel() {
		this.state.onSave(this.state.href)
	}

	render() {
		return (
			<div>
				<h2> {this.state.action.title} </h2>
				<dl>
					<dt>Title</dt>
					<dd>{this.state.item.name}</dd>
					<dt>Description</dt>
					<dd>
						<textarea
							value={this.state.item.description}
							onChange={this.handleDescriptionChange}
							style={textareaStyle}
						/>
					</dd>
				</dl>
				<button onClick={this.handleSave}>Save</button>
				<button onClick={this.handleCancel}>Cancel</button>
			</div>
		)
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
		render={result => (
			<HttpGetSwitch result={result} onJson={json => {
				console.log(json.actions)
				const action = json.actions.find(item => item.name === 'edit-checklist-template')
				const hrefSelf = json.links.find(item => item.rel.includes('self')).href
				return (
					<Edit
						url={url.replace(hrefSelf, action.href)}
						hrefChecklist={hrefSelf}
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

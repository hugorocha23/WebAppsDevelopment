import React from 'react'
import URI from 'urijs'

import StateFilter from './state-filter'
import Paginator from './paginator'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import { deleteRequest } from '../http-request'

export default class extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			url: props.url,
			onSelectDetail: props.onSelectDetail,
			onSelectCreate: props.onSelectCreate,
			onClickChecklist: props.onClickChecklist,
			authorization: props.authorization,
			getApiUrl: props.getApiUrl,
			key: Math.random(),
			error: null
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
								Authorization: this.state.authorization,
								Accept: '*/*'
							}
						}
					}
					render={(result) => (
						<div>
							{this.state.error ?
								<p style={{ color: 'red' }}> {this.state.error.message} </p>
								: ''
                			}
							<StateFilter onChange={value => result.setQuery(value)} />
							<Paginator response={result.response} onChange={url => result.setUrl(URI.decode(this.state.getApiUrl(url)))} />
							<HttpGetSwitch result={result}
								onJson={json => (
									<div>
										<h2> Checklist Items </h2>
										<ul>
											{json.items.map(item => (
												<li key={item.href}>
													{item.data.map(information => <p key={item.href}>{information.prompt} : {information.value}</p>)}
													<button onClick={() => this.state.onSelectDetail(item.href)}>Detail</button>
													<button onClick={() => this.handleDelete(this.state.url.replace(json.href, item.href), item.href)}>Delete</button>
												</li>
											))}
										</ul>
										<br />
										<button onClick={() => this.state.onSelectCreate(json.href)}>Create Item</button>
										<br /> <br />
										{
											json.links.find(link => link.rel === 'check-list') ?
												<div>
													<button onClick={() => this.state.onClickChecklist(json.links.find(link => link.rel === 'check-list').href)}>
														{json.links.find(link => link.rel === 'check-list').prompt}
													</button>
												</div>
												: ''
										}
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
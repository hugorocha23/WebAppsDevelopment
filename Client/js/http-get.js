import React from 'react'
import fetch from 'isomorphic-fetch'
import { makeCancellable } from './promises'

const acceptableContentTypes = {
	'application/json':'',
	'application/vnd.collection+json':'',
	'application/json-home':'',
	'application/vnd.siren+json':'',
	'application/problem+json':''
}

export default class extends React.Component {
	constructor(props) {
		super(props)
		this.setUrl = this.setUrl.bind(this)
		this.setQuery = this.setQuery.bind(this)
		this.state = {
			loading: true,
			url: props.url,
			options: props.options
		}
	}

	render() {
		const result = { ...this.state, setUrl: this.setUrl, setQuery: this.setQuery }
		return this.props.render(result)
	}

	static getDerivedStateFromProps(nextProps, prevState) {
		if (nextProps.url === prevState.url) return null
		return {
			loading: true,
			url: nextProps.url,
			response: undefined,
			error: undefined,
			json: undefined
		}
	}

	componentDidMount() {
		this.load(this.props.url)
	}

	componentDidUpdate(oldProps, oldState) {
		if (this.state.loading) {
			this.load(this.state.url)
		}
	}

	componentWillUnmount() {
		if (this.promise) {
			this.promise.cancel()
		}
	}

	setQuery(query) {
		console.log(`setQuery(${query})`)
		this.setState({
			url: this.props.url + '?' + query,
			loading: true
		})
	}

	setUrl(url) {
		this.setState({
			url: url,
			loading: true
		})
	}

	load(url) {
		console.log(`load(${url})`)
		if (this.promise) {
			this.promise.cancel()
		}
		this.promise = makeCancellable(fetch(url,this.state.options))
			.then(resp => {
				if (resp.status >= 400) {
					return resp.json().then(err => {
						throw new Error(err.detail)
					})
				}
				const ct = resp.headers.get('content-type') || ''
				if (ct === 'application/json' || ct.startsWith('application/json') || Object.keys(acceptableContentTypes).find(elem => ct.startsWith(elem))) {
					return resp.json().then(json => [resp, json])
				}
				throw new Error(`unexpected content type ${ct}`)
			})
			.then(([resp, json]) => {
				this.setState({
					loading: false,
					json: json,
					response: resp,
					error: undefined
				})
				this.promise = undefined
			})
			.catch(error => {
				this.setState({
					loading: false,
					error: error,
					json: undefined,
					response: undefined
				})
				this.promise = undefined
			})
	}
}

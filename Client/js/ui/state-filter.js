import React from 'react'

const names = {
	completed: 'completed',
	uncompleted: 'uncompleted'
}

const values = {
	completed: 'status=completed',
	uncompleted: 'status=uncompleted',
	all: 'status=all'
}

export default class extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			uncompleted: true,
			completed: true
		}
		this.onCompletedClick = this.onCompletedClick.bind(this)
        this.onUncompletedClick = this.onUncompletedClick.bind(this)
	}

	onCompletedClick(ev) {
		this.onClick('completed', 'uncompleted')
	}

    onUncompletedClick(ev) {
		this.onClick('uncompleted', 'completed')
	}

	onClick(cb1, cb2) {
		console.log(`onClick ${cb1} ${cb2}`)
		this.setState(old => ({
			[cb1]: !old[cb1],
			[cb2]: old[cb1] ? true : old[cb2]
		}))
	}

	render() {
		return (
			<div>
				{names.completed} <input type='checkbox' name='completed' checked={this.state.completed} onChange={this.onCompletedClick} />
                {names.uncompleted} <input type='checkbox' name='uncompleted' checked={this.state.uncompleted} onChange={this.onUncompletedClick} />
			</div>
		)
	}

	componentDidUpdate(oldProps, oldState) {
		const value = this.state.completed && this.state.uncompleted
			? values.all
			: this.state.completed ? values.completed : values.uncompleted
		if (this.state.completed !== oldState.completed || this.state.uncompleted !== oldState.uncompleted) {
			this.props.onChange(value)
		}
	}
}
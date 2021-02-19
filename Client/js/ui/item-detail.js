import React from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'

export default ({ url, onSelectEdit, onSelectPrev, onSelectNext, onClickItems, authorization }) => (
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
			<HttpGetSwitch result={result} onJson={(json) => (
				<div key={json.links.find(item => item.rel.includes('self')).href}>
					{
						json.entities.find(entity => entity.class.includes('checklist-items')) ?
							<button onClick={() => onClickItems(json.entities.find(entity => entity.class.includes('checklist-items')).href)}>Checklist Items</button>
							: ''
					}

					<h2>{json.properties.name}</h2>
					<p>Description : {json.properties.description}</p>
					<p>State : {json.properties.state}</p>

					{json.actions.find(action => action.name === 'edit-checklist-item') ?
						<button onClick={() => {
							onSelectEdit(json.links.find(item => item.rel.includes('self')).href)
						}}> Edit </button>
						: ''
					}
					<br /> <br />
					<button onClick={() => onSelectPrev(json.links.find(item => item.rel.includes('previous')).href)} disabled={!json.links.find(item => item.rel.includes('previous'))}>Previous</button>
					<button onClick={() => onSelectNext(json.links.find(item => item.rel.includes('next')).href)} disabled={!json.links.find(item => item.rel.includes('next'))}>Next</button>
				</div>
			)} />
		)}
	/>
)
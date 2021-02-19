import React from 'react'

import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import { Redirect } from 'react-router-dom'
import URI from 'urijs'

const loginComponent = "/get-user"
const showHomeComponents = ['/checklist', '/checklist-template']

export default ({ url, onSelectDetail }) => (
	<div>
		<HttpGet url={url} render={(result) => (
			<HttpGetSwitch result={result}
				onJson={json => 
					<ul>
						{showHomeComponents.map(item => {
							const href = json.resources[item].href
							return href ?
								(<li key={item}>
									{`${item}`}
									<button onClick={() => onSelectDetail(item, href)}> Go </button>
								</li>
								) 
								: <div key={item}/>
						})}
					</ul>
				}
			/>
		)}/>
	</div>
)
import React from 'react'
import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import { postRequest } from '../http-request'

export default ({ url, onSelectItems, onSelectEdit, onSelectCreateChecklist, authorization }) => (
    <div>
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
                <div>
                    <HttpGetSwitch result={result}
                        onJson={json => {
                            const self = json.links.find(item => item.rel.includes('self')).href
                            return (
                                <div key={self}>
                                    <p>Name : {json.properties.name}</p>
                                    <p>Description : {json.properties.description}</p>
                                    <p>
                                        <button onClick={
                                            () => onSelectItems(
                                                json.entities.find(item => item.rel.includes('collection')).href
                                            )}>
                                            Template Items
                                        </button>
                                    </p>
                                    <p>
                                        <button onClick={
                                            () => onSelectCreateChecklist(
                                                json.actions.find(action => action.name === 'create-checklist-from-template').href
                                            )}>
                                            Create Checklist based on this template
                                        </button>
                                    </p>
                                    <button onClick={
                                        () => onSelectEdit(
                                            self
                                        )}>
                                        Edit
                                    </button>
                                </div>
                            )
                        }}
                    />
                </div>
            )} />
    </div>
)

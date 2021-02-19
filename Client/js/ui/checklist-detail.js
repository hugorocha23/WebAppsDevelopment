import React from 'react'

import HttpGet from '../http-get'
import HttpGetSwitch from '../http-get-switch'
import ItemList from './item-list'
import { deleteRequest } from '../http-request'

export default ({ url, checklist, onSelectItems, onSelectEdit, authorization }) => (
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
            render={(result) => (
                <div>
                    <HttpGetSwitch result={result}
                        onJson={json => {
                            const self = json.links.find(item => item.rel.includes('self')).href
                            return (
                                <div key={self}>
                                    <p>Name : {json.properties.name}</p>
                                    <p>Description : {json.properties.description}</p>
                                    <p>Date to completion : {json.properties.dateToCompletion}</p>
                                    <p>Status : {json.properties.status}</p>
                                    <p>
                                        <button onClick={
                                            () => onSelectItems(
                                                json.entities.find(item => item.rel.includes('collection')).href
                                            )}>
                                            Checklist Items
                                        </button>
                                    </p>
                                    {
                                        self ?
                                            <button onClick={() => onSelectEdit(self)}> Edit </button>
                                            : ''
                                    }
                                </div>
                            )
                        }}
                    />
                </div>
            )} />
    </div >
)
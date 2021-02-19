import React from 'react'
import { withRouter } from 'react-router-dom'

export default withRouter(({ history, user, onLogin, onLogout}) => (
	<nav>
		<button onClick={() => history.push('/')} >Home</button>

		{
			history.location.pathname.includes('login') !== true ? 
				( user === null ?
					<button onClick={onLogin} >Login</button>
					: <button onClick={() => onLogout(history)} >Logout</button>
				)
				: <div/>
		}
	</nav>
))

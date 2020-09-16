import React from 'react'
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import Main from './components/Main'
import Score from './components/Score'
import InsertImage from './components/InsertImage'

class Routes extends React.Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route exact path='/' component={Main}/>
          <Route exact path='/score/:userId' component={Score}/>
          <Route exact path='/insert-image' component={InsertImage}/>
        </Switch>
      </Router>
    )
  }
}

export default Routes;
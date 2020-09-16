import React, {Component} from 'react';
import Routes from './Routes';

import Jumbotron from 'react-bootstrap/Jumbotron';
import Container from 'react-bootstrap/Container';


class App extends Component {
  render() {
    return (
      <Container className="p-3">
        <Jumbotron>
          <Routes/>
        </Jumbotron>
      </Container>
    )
  }
}

export default App;

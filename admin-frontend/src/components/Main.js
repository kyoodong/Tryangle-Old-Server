import React from "react";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";


class Main extends React.Component {
  render() {
    const name_list = ['동규', '강준', '유진']
    const name_a_list = name_list.map((name, index) =>
      <Button variant="outline-primary">
        <Link to={"score/" + index}>{name}</Link>
      </Button>
    );

    return (
      <div className="App">
        <ul>{name_a_list}</ul>
        <Button variant="outline-dark">
          <Link to="insert-image">사진추가</Link>
        </Button>

        <Button variant="outline-dark">
          <Link to="insert-spot-image">스팟사진추가</Link>
        </Button>
      </div>
    );
  }
}

export default Main
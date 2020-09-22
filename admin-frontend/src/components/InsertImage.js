import React from "react";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import {Form} from "react-bootstrap";


class InsertImage extends React.Component {
  render() {
    return (
      <div className="App">
        <Form method="post" action={window.springServerBaseUrl + "api/admin/image/insert"}
          encType="multipart/form-data">
          <Form.File
            multiple
            id="imageZip"
            name="imageZip"/>
          <Button variant="primary" type="submit">확인</Button>
        </Form>
      </div>
    );
  }
}

export default InsertImage
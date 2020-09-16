import React, {useEffect, useState} from "react";
import {Button} from "react-bootstrap";
import axios from "axios";

function LoadingButton(props) {
  const userId = props.userId
  const [isLoading, setLoading] = useState(false)

  const loadImageList = async (userId) => {
    axios.get(`${window.springServerBaseUrl}api/image/${userId}`)
      .then(({data}) => {
        setLoading(false);
        props.onLoadImage(data)
      })
      .catch(e => {
        console.error(e);
        setLoading(false);
      })
  }

  useEffect(() => {
    if (isLoading) {
      loadImageList(userId)
    }
  }, [isLoading]);

  const handleClick = () => setLoading(true);

  return (
    <Button
      variant="outline-dark"
      disabled={isLoading}
      onClick={!isLoading ? handleClick : null}>
      {isLoading ? '로딩 중...' : '새 사진 불러오기'}
    </Button>
  )
}

class Board extends React.Component {

  handleOnChange(e) {
  }

  render() {
    return (
      <div>
        <img src={window.springServerBaseUrl + this.props.image.url}/>
        <input onKeyPress={this.props.onKeyPress} type="number" value={this.props.image.score}
               onChange={this.handleOnChange} autoFocus="autoFocus"/>
      </div>
    );
  }
}

class Score extends React.Component {

  constructor(props) {
    super(props);

    this.handleLoadImageList = this.handleLoadImageList.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this)
    this.state = {
      imageList: [],
      index: 0
    }
  }

  handleKeyPress(e) {
    const image = this.state.imageList[this.state.index]
    image.score = parseInt(e.key)

    axios.post(`${window.springServerBaseUrl}api/image/score`, {
      imageId: image.id,
      score: image.score
    }).then(({data}) => {
      if (data === true) {
        this.setState({
          imageList: this.state.imageList,
          index: this.state.index + 1
        })
      }
    }).catch(e => {
      console.error(e)
    })
  }

  handleLoadImageList(imageList) {
    this.setState({
      imageList: imageList,
      index: 0
    })
  }

  render() {
    const {match: {params}} = this.props;
    this.userId = params.userId
    let board = <div></div>

    if (this.state.imageList.length > 0 && this.state.imageList.length > this.state.index) {
      board = <Board image={this.state.imageList[this.state.index]}
                     onKeyPress={this.handleKeyPress}/>
    }

    return (
      <div className="App">
        <LoadingButton userId={this.userId}
          onLoadImage={this.handleLoadImageList}/>
        {board}
      </div>
    );
  }
}

export default Score
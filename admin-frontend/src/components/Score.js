import React, {useEffect, useState} from "react";
import {Button} from "react-bootstrap";
import axios from "axios";

function LoadingButton(props) {
  const userId = props.userId
  const [isLoading, setLoading] = useState(false)

  const loadImageList = async (userId) => {
    axios.get(`${process.env.REACT_APP_SPRING_SERVER_URL}api/admin/image/${userId}`)
      .then(({data}) => {
        console.log(data.length)
        if (data.length === 0) {
          alert('이미지가 없습니다. 다른 유저로 이동하세요')
        } else {
          setLoading(false);
          props.onLoadImage(data)
        }
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

  render() {
    return (
      <div>
        <img src={process.env.REACT_APP_SPRING_SERVER_URL + this.props.image.url} className="image-score"/>
        <input type="number" value=""
               onChange={this.props.onKeyPress} autoFocus="autoFocus" pattern="\d*"/>
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
      index: 0,
      count: 0
    }

    axios.get(`${process.env.REACT_APP_SPRING_SERVER_URL}api/admin/image/score`)
      .then(({data}) => {
        this.setState({
          imageList: this.state.imageList,
          index: this.state.index,
          count: data
        })
      })
  }

  handleKeyPress(e) {
    const image = this.state.imageList[this.state.index]
    const score = parseInt(e.target.value)

    if (score < 0 || score > 5) {
      alert('0~5점 사이만 입력하세요');
      return;
    }

    image.score = score
    axios.post(`${process.env.REACT_APP_SPRING_SERVER_URL}api/admin/image/score`, {
      imageId: image.id,
      score: image.score
    }).then(({data}) => {
      if (data === true) {
        this.setState({
          imageList: this.state.imageList,
          index: this.state.index + 1,
          count: this.state.count + 1
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
        <div>{this.state.count} 개 채점</div>
        {board}
      </div>
    );
  }
}

export default Score
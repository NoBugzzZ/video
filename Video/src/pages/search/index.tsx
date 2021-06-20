import { useState } from 'react'
import { Table, message } from 'antd'
import { PlayCircleOutlined } from '@ant-design/icons'
import { useEffect } from 'react'
import { VideoRQ } from '@/requests'

export default function () {

    const [data, setData] = useState([])

    useEffect(() => {
        VideoRQ.getAll().then(data => {
            //console.log(data)
            message.success('读取成功', 0.5)
            setData(data)
        })
    }, [])

    function download(uri: string) {
        //console.log(uri)
        VideoRQ.get(uri).then(data => {
            //console.log(data)
            message.success('下载成功', 0.5)
        })
    }

    const column = [
        {
            title: '名称',
            dataIndex: 'name',
            render: (text: string, record: any, index: number) => { return <>{text.substring(text.indexOf(']') + 1, text.length)}</> }
        },
        {
            title: '链接',
            dataIndex: 'name',
            render: (text: string, record: any, index: number) => { return <a onClick={(e) => download(e.currentTarget.text)}>{text}</a> }
        },
        {
            title: '',
            dataIndex: '',
            render: () => <PlayCircleOutlined />
        }
    ]

    return (
        <Table rowKey='name' columns={column} dataSource={data} />
    )
}
import { IRouteComponentProps, history } from 'umi'
import { Layout, Menu } from 'antd'
import './index.less'

const { Header, Content } = Layout

const pathMap = {
    '': '主页',
    'upload': '上传视频',
    'search': '查看视频'
} as any

const { Item } = Menu

export default function ({ children }: IRouteComponentProps) {

    const items = Object.keys(pathMap).map(k => {
        return (
            <Item key={k} onClick={() => history.push('/' + k)}>
                {pathMap[k]}
            </Item>
        )
    })

    return (
        <Layout>
            <Header>
                <Menu mode='horizontal'>
                    <Item key={'home'} className='logo' onClick={() => history.push('/')}>Video</Item>
                    {items}
                </Menu>
            </Header>
            <Content>{children}</Content>
        </Layout>
    )
}
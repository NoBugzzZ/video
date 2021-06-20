import axios from 'axios'
import { ConvertRes2Blob } from '.'

export default {
    async getAll() {
        const { data } = await axios.get('/api/videos')
        return data
    },
    async get(uri: string) {
        const res = await axios.request({
            method: 'get',
            url: '/api/video',
            headers: {
                accept: '*/*'
            },
            responseType: 'blob',
            params: {
                uri: encodeURI(uri)
            }
        })
        //console.log(res)
        ConvertRes2Blob(res)
        return 'success'
    }
}
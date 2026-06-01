import { ElMessage } from 'element-plus'

const ALLOWED_IMAGE_TYPES = [
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp',
  'image/bmp',
  'image/x-ms-bmp'
]

const ALLOWED_IMAGE_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif', 'webp', 'bmp']
const MAX_IMAGE_SIZE = 5 * 1024 * 1024

export const getUploadHeaders = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  return user.token ? { token: user.token } : {}
}

export const beforeImageUpload = (file) => {
  const fileName = file?.name || ''
  const extension = fileName.includes('.') ? fileName.split('.').pop().toLowerCase() : ''
  const isAllowedType = ALLOWED_IMAGE_TYPES.includes(file.type) || ALLOWED_IMAGE_EXTENSIONS.includes(extension)

  if (!isAllowedType) {
    ElMessage.error('只能上传 JPG、JPEG、PNG、GIF、WEBP、BMP 图片')
    return false
  }

  if (file.size > MAX_IMAGE_SIZE) {
    ElMessage.error('图片大小不能超过5MB')
    return false
  }

  return true
}

export const handleUploadError = (error) => {
  try {
    const response = JSON.parse(error.message)
    ElMessage.error(response.msg || '上传失败')
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

import 'react-toastify/dist/ReactToastify.css'
import './styles/globals.css'
import Root from './components/Root'

export const metadata = {
  title: 'MineTower',
  description: 'MineTower Console',
  icons: {
    icon: ['/favicon.ico?v=4'],
    apple: ['/apple-touch-icon.png?v=4'],
    shortcut: ['/apple-touch-icon.png'],
  },
  manifest: '/site.webmanifest'
}

export default function RootLayout({children}) {

  return (
    <html lang="en" className='md:text-lg'>
      <body>
        <Root children={children} />
      </body>
    </html>
  )

}

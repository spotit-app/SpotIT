import { Route, Routes } from 'react-router-dom';
import { Home, ProtectedPage, Profile, PageNotFound } from './pages';
import { Navbar } from './components';

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/profile"
          element={
            <ProtectedPage>
              <Profile />
            </ProtectedPage>
          }
        />
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </>
  );
}

export default App;

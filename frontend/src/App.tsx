import { Route, Routes } from 'react-router-dom';
import {
  Home,
  ProtectedPage,
  Profile,
  PageNotFound,
  PersonalData,
  Courses,
  Education,
  Experience,
  ForeignLang,
  Interests,
  Other,
  Projects,
  Socials,
  SoftSkills,
  TechSkills
} from './pages';
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
        >
          <Route path="dane-osobowe" element={<PersonalData />} />
          <Route path="konta-spolecznosciowe" element={<Socials />} />
          <Route path="edukacja" element={<Education />} />
          <Route path="doswiadczenie" element={<Experience />} />
          <Route path="umiejetnosci-techniczne" element={<TechSkills />} />
          <Route path="umiejetnosci-miekkie" element={<SoftSkills />} />
          <Route path="jezyki-obce" element={<ForeignLang />} />
          <Route path="projekty" element={<Projects />} />
          <Route path="zainteresowania" element={<Interests />} />
          <Route path="kursy" element={<Courses />} />
          <Route path="inne" element={<Other />} />
        </Route>
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </>
  );
}

export default App;

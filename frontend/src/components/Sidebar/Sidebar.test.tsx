import { render, screen, act, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { Sidebar } from './';
import { RouterProvider } from '../../providers';

describe('Sidebar', () => {
  beforeEach(() => {
    const mockProps = {
      picture: 'https://example.com/picture.png',
      name: 'John Doe'
    };

    act(() =>
      render(
        <RouterProvider>
          <Sidebar {...mockProps} />
        </RouterProvider>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('renders Sidebar component', () => {
    const nameElement = screen.getByText('John Doe');
    expect(nameElement).toBeInTheDocument();

    const imageElement = screen.getByAltText('Profile Picture');
    expect(imageElement).toBeInTheDocument();

    const navigation = [
      'Dane osobowe',
      'Konta społecznościowe',
      'Edukacja',
      'Doświadczenie',
      'Umiejętności techniczne',
      'Umiejętności miękkie',
      'Języki obce',
      'Projekty',
      'Zainteresowania',
      'Kursy',
      'Inne'
    ];
    navigation.forEach((el) => {
      expect(screen.getByText(el)).toBeInTheDocument();
    });
  });

  test('Sidebar navigation working', async () => {
    const personalDataLink = screen.getByText('Dane osobowe');
    fireEvent.click(personalDataLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profile/dane-osobowe');
    });

    const experienceLink = screen.getByText('Doświadczenie');
    fireEvent.click(experienceLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profile/doswiadczenie');
    });

    const othersLink = screen.getByText('Inne');
    fireEvent.click(othersLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profile/inne');
    });
  });
});
